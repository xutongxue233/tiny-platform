import {
  ModalForm,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import React, { FC, ReactElement, useMemo, useState } from 'react';
import { addTemplate, updateTemplate } from '@/services/ant-design-pro/message';

interface TemplateFormProps {
  trigger?: ReactElement;
  values?: API.MsgTemplate;
  onOk?: () => void;
}

const TemplateForm: FC<TemplateFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.templateId;
  const [open, setOpen] = useState(false);
  const [templateType, setTemplateType] = useState<string>(values?.templateType || 'site');
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: addRun, loading: addLoading } = useRequest(addTemplate, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      messageApi.success(intl.formatMessage({ id: 'pages.message.template.addSuccess' }));
      onOk?.();
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.template.addFailed' }));
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateTemplate, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      messageApi.success(intl.formatMessage({ id: 'pages.message.template.updateSuccess' }));
      onOk?.();
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.template.updateFailed' }));
    },
  });

  const handleFinish = async (formValues: API.MsgTemplateDTO) => {
    if (isEdit) {
      formValues.templateId = values?.templateId;
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
        {intl.formatMessage({ id: 'pages.message.template.add' })}
      </Button>
    );
  }, [trigger, intl]);

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.MsgTemplateDTO>
        title={
          isEdit
            ? intl.formatMessage({ id: 'pages.message.template.editTitle' })
            : intl.formatMessage({ id: 'pages.message.template.addTitle' })
        }
        open={open}
        onOpenChange={(visible) => {
          setOpen(visible);
          if (visible) {
            setTemplateType(values?.templateType || 'site');
          }
        }}
        width={700}
        grid
        rowProps={{ gutter: [16, 0] }}
        initialValues={{
          templateType: 'site',
          status: '0',
          ...values,
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
          name="templateCode"
          label={intl.formatMessage({ id: 'pages.message.template.templateCode' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.template.templateCodeRequired' }),
            },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'pages.message.template.templateCodePattern' }),
            },
          ]}
          placeholder={intl.formatMessage({ id: 'pages.message.template.templateCodePlaceholder' })}
          disabled={isEdit}
        />
        <ProFormText
          name="templateName"
          label={intl.formatMessage({ id: 'pages.message.template.templateName' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.template.templateNameRequired' }),
            },
          ]}
          placeholder={intl.formatMessage({ id: 'pages.message.template.templateNamePlaceholder' })}
        />
        <ProFormSelect
          name="templateType"
          label={intl.formatMessage({ id: 'pages.message.template.templateType' })}
          colProps={{ span: 12 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.template.templateTypeRequired' }),
            },
          ]}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.template.templateType.site' }),
              value: 'site',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.template.templateType.email' }),
              value: 'email',
            },
          ]}
          fieldProps={{
            onChange: (value) => setTemplateType(value),
          }}
        />
        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.message.template.status' })}
          colProps={{ span: 12 }}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.template.status.normal' }),
              value: '0',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.template.status.disabled' }),
              value: '1',
            },
          ]}
        />
        {templateType === 'email' && (
          <ProFormText
            name="templateSubject"
            label={intl.formatMessage({ id: 'pages.message.template.templateSubject' })}
            colProps={{ span: 24 }}
            placeholder={intl.formatMessage({
              id: 'pages.message.template.templateSubjectPlaceholder',
            })}
          />
        )}
        <ProFormText
          name="variables"
          label={intl.formatMessage({ id: 'pages.message.template.variables' })}
          colProps={{ span: 24 }}
          placeholder={intl.formatMessage({ id: 'pages.message.template.variablesPlaceholder' })}
          tooltip={intl.formatMessage({ id: 'pages.message.template.variablesTooltip' })}
        />
        <ProFormTextArea
          name="templateContent"
          label={intl.formatMessage({ id: 'pages.message.template.templateContent' })}
          colProps={{ span: 24 }}
          rules={[
            {
              required: true,
              message: intl.formatMessage({ id: 'pages.message.template.templateContentRequired' }),
            },
          ]}
          fieldProps={{
            rows: 6,
            maxLength: 2000,
            showCount: true,
          }}
          placeholder={intl.formatMessage({
            id: 'pages.message.template.templateContentPlaceholder',
          })}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.message.template.remark' })}
          colProps={{ span: 24 }}
          fieldProps={{
            rows: 3,
            maxLength: 500,
            showCount: true,
          }}
          placeholder={intl.formatMessage({ id: 'pages.message.template.remarkPlaceholder' })}
        />
      </ModalForm>
    </>
  );
};

export default TemplateForm;
