import { ModalForm, ProFormText } from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { message } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useState } from 'react';
import { resetUserPassword } from '@/services/ant-design-pro/api';

interface ResetPasswordFormProps {
  trigger: ReactElement;
  userId: number;
}

const ResetPasswordForm: FC<ResetPasswordFormProps> = (props) => {
  const { trigger, userId } = props;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);

  const { run, loading } = useRequest(resetUserPassword, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({
          id: 'pages.user.resetPasswordSuccess',
          defaultMessage: '密码重置成功',
        }),
      );
      setOpen(false);
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({
          id: 'pages.user.resetPasswordFailed',
          defaultMessage: '密码重置失败',
        }),
      );
    },
  });

  const onOpen = useCallback(() => {
    setOpen(true);
  }, []);

  const handleFinish = useCallback(
    async (values: { newPassword: string }) => {
      await run({ userId, newPassword: values.newPassword });
      return true;
    },
    [userId, run],
  );

  return (
    <>
      {contextHolder}
      {cloneElement(trigger, { onClick: onOpen })}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.user.resetPasswordTitle',
          defaultMessage: '重置密码',
        })}
        open={open}
        onOpenChange={setOpen}
        width={400}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading },
        }}
        onFinish={handleFinish}
      >
        <ProFormText.Password
          name="newPassword"
          label={intl.formatMessage({
            id: 'pages.user.newPassword',
            defaultMessage: '新密码',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.user.newPasswordPlaceholder',
            defaultMessage: '请输入新密码',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.user.passwordRequired',
                defaultMessage: '请输入密码',
              }),
            },
            {
              min: 6,
              max: 20,
              message: intl.formatMessage({
                id: 'pages.user.passwordLength',
                defaultMessage: '密码长度为6-20个字符',
              }),
            },
          ]}
        />
        <ProFormText.Password
          name="confirmPassword"
          label={intl.formatMessage({
            id: 'pages.user.confirmPassword',
            defaultMessage: '确认密码',
          })}
          placeholder={intl.formatMessage({
            id: 'pages.user.confirmPasswordPlaceholder',
            defaultMessage: '请再次输入新密码',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.user.confirmPasswordRequired',
                defaultMessage: '请确认密码',
              }),
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('newPassword') === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error(
                    intl.formatMessage({
                      id: 'pages.user.passwordMismatch',
                      defaultMessage: '两次输入的密码不一致',
                    }),
                  ),
                );
              },
            }),
          ]}
        />
      </ModalForm>
    </>
  );
};

export default ResetPasswordForm;
