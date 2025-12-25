import { LockOutlined, SafetyCertificateOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import { Helmet, history } from '@umijs/max';
import { Alert, App, Spin } from 'antd';
import { createStyles } from 'antd-style';
import React, { useCallback, useEffect, useState } from 'react';
import { Footer } from '@/components';
import { usePublicConfig } from '@/hooks/usePublicConfig';
import { getCaptcha, register } from '@/services/ant-design-pro/api';
import Settings from '../../../../config/defaultSettings';

const useStyles = createStyles(() => {
  return {
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

const Register: React.FC = () => {
  const [submitting, setSubmitting] = useState(false);
  const [registerError, setRegisterError] = useState<string>('');
  const [captchaKey, setCaptchaKey] = useState<string>('');
  const [captchaImage, setCaptchaImage] = useState<string>('');
  const [captchaLoading, setCaptchaLoading] = useState(false);
  const { styles } = useStyles();
  const { message } = App.useApp();
  const { registerEnabled, appName } = usePublicConfig();

  const refreshCaptcha = useCallback(async () => {
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
  }, []);

  useEffect(() => {
    refreshCaptcha();
  }, [refreshCaptcha]);

  useEffect(() => {
    if (!registerEnabled) {
      message.warning('系统暂未开放注册');
      history.push('/user/login');
    }
  }, [registerEnabled, message]);

  const handleSubmit = async (values: API.RegisterParams) => {
    setSubmitting(true);
    setRegisterError('');
    try {
      const registerParams: API.RegisterParams = {
        ...values,
        captchaKey,
      };
      const response = await register(registerParams);
      if (response.code === 200) {
        message.success('注册成功，请登录');
        history.push('/user/login');
        return;
      }
      setRegisterError(response.message || '注册失败');
      refreshCaptcha();
    } catch (error: any) {
      const errorMsg = error?.info?.message || error?.message || '注册失败，请重试';
      setRegisterError(errorMsg);
      refreshCaptcha();
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className={styles.container}>
      <Helmet>
        <title>注册 - {Settings.title}</title>
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
          subTitle="用户注册"
          submitter={{
            searchConfig: {
              submitText: '注册',
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
            await handleSubmit(values as API.RegisterParams);
          }}
        >
          {registerError && (
            <Alert
              style={{ marginBottom: 24 }}
              message={registerError}
              type="error"
              showIcon
            />
          )}

          <ProFormText
            name="username"
            fieldProps={{
              size: 'large',
              prefix: <UserOutlined />,
            }}
            placeholder="请输入用户名"
            rules={[
              { required: true, message: '请输入用户名！' },
              { min: 3, message: '用户名至少3个字符' },
              { max: 20, message: '用户名最多20个字符' },
              {
                pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/,
                message: '用户名必须以字母开头，只能包含字母、数字和下划线',
              },
            ]}
          />

          <ProFormText.Password
            name="password"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
            }}
            placeholder="请输入密码"
            rules={[
              { required: true, message: '请输入密码！' },
              { min: 6, message: '密码至少6个字符' },
              { max: 20, message: '密码最多20个字符' },
            ]}
          />

          <ProFormText.Password
            name="confirmPassword"
            fieldProps={{
              size: 'large',
              prefix: <LockOutlined />,
            }}
            placeholder="请确认密码"
            rules={[
              { required: true, message: '请确认密码！' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('两次输入的密码不一致！'));
                },
              }),
            ]}
          />

          <div style={{ display: 'flex', alignItems: 'flex-start', marginBottom: 24 }}>
            <div style={{ flex: 1 }}>
              <ProFormText
                name="captcha"
                fieldProps={{
                  size: 'large',
                  prefix: <SafetyCertificateOutlined />,
                }}
                placeholder="请输入验证码"
                rules={[{ required: true, message: '请输入验证码！' }]}
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

          <div style={{ marginBottom: 24, textAlign: 'center' }}>
            已有账号？
            <a onClick={() => history.push('/user/login')}>立即登录</a>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
