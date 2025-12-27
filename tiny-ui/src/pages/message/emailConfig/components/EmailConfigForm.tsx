import {
  ModalForm,
  ProFormDigit,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import React, { FC, ReactElement, useMemo, useState } from 'react';
import { addEmailConfig, updateEmailConfig } from '@/services/ant-design-pro/message';

interface EmailConfigFormProps {
  trigger?: ReactElement;
  values?: API.MsgEmailConfig;
  onOk?: () => void;
}

const EmailConfigForm: FC<EmailConfigFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.configId;
  const [open, setOpen] = useState(false);
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: addRun, loading: addLoading } = useRequest(addEmailConfig, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      messageApi.success(intl.formatMessage({ id: 'pages.message.emailConfig.addSuccess' }));
      onOk?.();
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.emailConfig.addFailed' }));
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateEmailConfig, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      messageApi.success(intl.formatMessage({ id: 'pages.message.emailConfig.updateSuccess' }));
      onOk?.();
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.emailConfig.updateFailed' }));
    },
  });

  const handleFinish = async (formValues: API.MsgEmailConfigDTO) => {
    if (isEdit) {
      formValues.configId = values?.configId;
      await updateRun(formValues);
    } else {
      await addRun(formValues);
    }
    return true;
  };

  const triggerDom = useMemo(() => {
    if (trigger) {
      return React.cloneElement(trigger, {
        onClick: () => setOpen(true),
      });
    }
    return (
      <Button type="primary" onClick={() => setOpen(true)}>
        {intl.formatMessage({ id: 'pages.message.emailConfig.add' })}
      </Button>
    );
  }, [trigger, intl]);

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.MsgEmailConfigDTO>
        title={
          isEdit
            ? intl.formatMessage({ id: 'pages.message.emailConfig.editTitle' })
            : intl.formatMessage({ id: 'pages.message.emailConfig.addTitle' })
        }
        open={open}
        onOpenChange={setOpen}
        width={600}
        grid
        rowProps={{ gutter: [16, 0] }}
        initialValues={{
          sslEnable: '1',
          isDefault: '0',
          status: '0',
          port: 465,
          ...values,
          password: undefined,
        }}
        modalProps={{
          destroyOnHidden: true,
          maskClosable: false,
        }}
        submitter={{
          submitButtonProps: {
            loading: addLoading || updateLoading,
          },
        }}
        onFinish={handleFinish}
      >
        <ProFormText
          name="configName"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.configName' })}
          colProps={{ span: 24 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.configNameRequired' }),
            },
          ]}
          placeholder={intl.formatMessage({
            id: 'pages.message.emailConfig.configNamePlaceholder',
          })}
        />
        <ProFormText
          name="host"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.host' })}
          colProps={{ span: 16 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.hostRequired' }),
            },
          ]}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.hostPlaceholder' })}
        />
        <ProFormDigit
          name="port"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.port' })}
          colProps={{ span: 8 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.portRequired' }),
            },
          ]}
          fieldProps={{
            min: 1,
            max: 65535,
          }}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.portPlaceholder' })}
        />
        <ProFormText
          name="username"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.username' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.usernameRequired' }),
            },
          ]}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.usernamePlaceholder' })}
        />
        <ProFormText.Password
          name="password"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.password' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: !isEdit,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.passwordRequired' }),
            },
          ]}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.passwordPlaceholder' })}
          tooltip={isEdit ? intl.formatMessage({ id: 'pages.message.emailConfig.passwordTooltip' }) : undefined}
        />
        <ProFormText
          name="fromAddress"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.fromAddress' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.emailConfig.fromAddressRequired' }),
            },
            {
              type: 'email',
              message: intl.formatMessage({ id: 'pages.user.emailInvalid' }),
            },
          ]}
          placeholder={intl.formatMessage({
            id: 'pages.message.emailConfig.fromAddressPlaceholder',
          })}
        />
        <ProFormText
          name="fromName"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.fromName' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.fromNamePlaceholder' })}
        />
        <ProFormRadio.Group
          name="sslEnable"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable' })}
          colProps={{ span: 8 }}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable.yes' }),
              value: '1',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable.no' }),
              value: '0',
            },
          ]}
        />
        <ProFormRadio.Group
          name="isDefault"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.isDefault' })}
          colProps={{ span: 8 }}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.isDefault.yes' }),
              value: '1',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.isDefault.no' }),
              value: '0',
            },
          ]}
        />
        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.status' })}
          colProps={{ span: 8 }}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.status.normal' }),
              value: '0',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.emailConfig.status.disabled' }),
              value: '1',
            },
          ]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.message.emailConfig.remark' })}
          colProps={{ span: 24 }}
          fieldProps={{
            rows: 3,
            maxLength: 500,
            showCount: true,
          }}
          placeholder={intl.formatMessage({ id: 'pages.message.emailConfig.remarkPlaceholder' })}
        />
      </ModalForm>
    </>
  );
};

export default EmailConfigForm;
