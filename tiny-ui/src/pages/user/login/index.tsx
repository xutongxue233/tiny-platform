import { LockOutlined, SafetyCertificateOutlined, UserOutlined } from '@ant-design/icons';
import {
  LoginForm,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import { Helmet, history, useIntl, useModel } from '@umijs/max';
import { Alert, App, Image, Input, Spin } from 'antd';
import { createStyles } from 'antd-style';
import React, { useCallback, useEffect, useState } from 'react';
import { flushSync } from 'react-dom';
import { Footer } from '@/components';
import { usePublicConfig } from '@/hooks/usePublicConfig';
import { getCaptcha, login, TokenUtil } from '@/services/ant-design-pro/api';
import Settings from '../../../../config/defaultSettings';

const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});

const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

const Login: React.FC = () => {
  const [loginError, setLoginError] = useState<string>('');
  const [submitting, setSubmitting] = useState(false);
  const [captchaKey, setCaptchaKey] = useState<string>('');
  const [captchaImage, setCaptchaImage] = useState<string>('');
  const [captchaLoading, setCaptchaLoading] = useState(false);
  const { initialState, setInitialState } = useModel('@@initialState');
  const { styles } = useStyles();
  const { message } = App.useApp();
  const intl = useIntl();
  const { captchaEnabled, registerEnabled, appName, loading: configLoading } = usePublicConfig();

  const refreshCaptcha = useCallback(async () => {
    if (!captchaEnabled) return;
    setCaptchaLoading(true);
    try {
      const res = await getCaptcha();
      if (res.code === 200 && res.data) {
        setCaptchaKey(res.data.captchaKey);
        setCaptchaImage(res.data.captchaImage);
      }
    } finally {
      setCaptchaLoading(false);
    }
  }, [captchaEnabled]);

  useEffect(() => {
    if (captchaEnabled) {
      refreshCaptcha();
    }
  }, [captchaEnabled, refreshCaptcha]);

  const fetchUserInfo = async () => {
    const userInfo = await initialState?.fetchUserInfo?.();
    if (userInfo) {
      flushSync(() => {
        setInitialState((s) => ({
          ...s,
          currentUser: userInfo,
        }));
      });
    }
  };

  const handleSubmit = async (values: API.LoginParams) => {
    setSubmitting(true);
    setLoginError('');
    try {
      const loginParams: API.LoginParams = {
        ...values,
      };
      if (captchaEnabled) {
        loginParams.captchaKey = captchaKey;
      }
      const response = await login(loginParams);
      if (response.code === 200 && response.data) {
        const { token } = response.data;
        TokenUtil.setToken(token);
        message.success(
          intl.formatMessage({
            id: 'pages.login.success',
            defaultMessage: '登录成功！',
          }),
        );
        await fetchUserInfo();
        const urlParams = new URL(window.location.href).searchParams;
        window.location.href = urlParams.get('redirect') || '/';
        return;
      }
      setLoginError(response.message || '登录失败');
      refreshCaptcha();
    } catch (error: any) {
      const errorMsg = error?.info?.message || error?.message || '登录失败，请重试';
      setLoginError(errorMsg);
      refreshCaptcha();
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {intl.formatMessage({
            id: 'menu.login',
            defaultMessage: '登录页',
          })}
          {Settings.title && ` - ${Settings.title}`}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/logo.svg" />}
          title={appName}
          subTitle={intl.formatMessage({
            id: 'pages.layouts.userLayout.title',
            defaultMessage: '企业级中后台管理系统',
          })}
          initialValues={{
            autoLogin: true,
          }}
          submitter={{
            searchConfig: {
              submitText: intl.formatMessage({
                id: 'pages.login.submit',
                defaultMessage: '登录',
              }),
            },
            submitButtonProps: {
              loading: submitting,
              size: 'large',
              style: {
                width: '100%',
              },
            },
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.LoginParams);
          }}
        >
          {loginError && <LoginMessage content={loginError} />}

          <ProFormText
            name="username"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined />,
            }}
            placeholder={intl.formatMessage({
              id: 'pages.login.username.placeholder',
              defaultMessage: '请输入用户名',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.login.username.required',
                  defaultMessage: '请输入用户名！',
                }),
              },
            ]}
          />
          <ProFormText.Password
            name="password"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
            }}
            placeholder={intl.formatMessage({
              id: 'pages.login.password.placeholder',
              defaultMessage: '请输入密码',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.login.password.required',
                  defaultMessage: '请输入密码！',
                }),
              },
            ]}
          />

          {captchaEnabled && (
            <div style={{ display: 'flex', alignItems: 'flex-start', marginBottom: 24 }}>
              <div style={{ flex: 1 }}>
                <ProFormText
                  name="captcha"
                  fieldProps={{
                    size: 'large',
                    prefix: <SafetyCertificateOutlined />,
                  }}
                  placeholder="请输入验证码"
                  rules={[
                    {
                      required: true,
                      message: '请输入验证码！',
                    },
                  ]}
                />
              </div>
              <div
                style={{
                  marginLeft: 8,
                  cursor: 'pointer',
                  width: 120,
                  height: 40,
                  flexShrink: 0,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  border: '1px solid #d9d9d9',
                  borderRadius: 6,
                  overflow: 'hidden',
                }}
                onClick={refreshCaptcha}
                title="点击刷新验证码"
              >
                {captchaLoading ? (
                  <Spin size="small" />
                ) : captchaImage ? (
                  <img
                    src={captchaImage}
                    alt="验证码"
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                  />
                ) : (
                  <span style={{ color: '#999', fontSize: 12 }}>加载中...</span>
                )}
              </div>
            </div>
          )}

          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              {intl.formatMessage({
                id: 'pages.login.rememberMe',
                defaultMessage: '自动登录',
              })}
            </ProFormCheckbox>
            <div style={{ float: 'right' }}>
              {registerEnabled && (
                <a
                  style={{ marginRight: 16 }}
                  onClick={() => history.push('/user/register')}
                >
                  立即注册
                </a>
              )}
              <a>
                {intl.formatMessage({
                  id: 'pages.login.forgotPassword',
                  defaultMessage: '忘记密码',
                })}
              </a>
            </div>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;