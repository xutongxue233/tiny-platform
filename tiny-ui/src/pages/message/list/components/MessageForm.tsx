import {
  ModalForm,
  ProFormDependency,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import type { ProFormInstance } from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Alert, Button, message, Space, Tag, Typography } from 'antd';
import React, { FC, ReactElement, useMemo, useRef, useState } from 'react';
import { sendMessage, getTemplateList } from '@/services/ant-design-pro/message';
import { getUserListAll } from '@/services/ant-design-pro/api';

const { Text } = Typography;

interface MessageFormProps {
  trigger?: ReactElement;
  onOk?: () => void;
}

const MessageForm: FC<MessageFormProps> = (props) => {
  const { trigger, onOk } = props;
  const formRef = useRef<ProFormInstance>();
  const [open, setOpen] = useState(false);
  const [channel, setChannel] = useState<string>('site');
  const [broadcast, setBroadcast] = useState<boolean>(false);
  const [selectedTemplateCode, setSelectedTemplateCode] = useState<string | undefined>();
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { data: templateData, run: fetchTemplates } = useRequest(getTemplateList, {
    manual: true,
    formatResult: (res) => res.data || [],
  });

  const { data: userData, run: fetchUsers } = useRequest(getUserListAll, {
    manual: true,
    formatResult: (res) => res.data || [],
  });

  const { run: sendRun, loading: sendLoading } = useRequest(sendMessage, {
    manual: true,
    onSuccess: () => {
      setOpen(false);
      messageApi.success(intl.formatMessage({ id: 'pages.message.list.sendSuccess' }));
      onOk?.();
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.list.sendFailed' }));
    },
  });

  // 获取当前选中的模板
  const selectedTemplate = useMemo(() => {
    if (!selectedTemplateCode || !templateData) return null;
    return templateData.find((t: API.MsgTemplate) => t.templateCode === selectedTemplateCode);
  }, [selectedTemplateCode, templateData]);

  // 解析模板变量
  const templateVariables = useMemo(() => {
    if (!selectedTemplate?.variables) return [];
    try {
      return JSON.parse(selectedTemplate.variables);
    } catch {
      return [];
    }
  }, [selectedTemplate]);

  const handleFinish = async (formValues: any) => {
    // 构建变量对象
    const variables: Record<string, string> = {};
    templateVariables.forEach((v: string) => {
      if (formValues[`var_${v}`]) {
        variables[v] = formValues[`var_${v}`];
      }
    });

    const submitData: API.SendMessageDTO = {
      channel: formValues.channel,
      title: formValues.title,
      content: formValues.content,
      priority: formValues.priority,
      templateCode: formValues.templateCode,
      variables: Object.keys(variables).length > 0 ? variables : undefined,
      broadcast: formValues.broadcast,
    };

    if (formValues.channel === 'site') {
      // 广播模式不需要指定用户
      if (!formValues.broadcast) {
        submitData.userIds = formValues.userIds;
      }
    } else {
      submitData.emails = formValues.emails?.split(',').map((e: string) => e.trim());
    }

    await sendRun(submitData);
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
        {intl.formatMessage({ id: 'pages.message.list.send' })}
      </Button>
    );
  }, [trigger, intl]);

  const templateOptions = useMemo(() => {
    if (!templateData || !Array.isArray(templateData)) {
      return [];
    }
    return templateData
      .filter((t: API.MsgTemplate) => t.templateType === channel)
      .map((t: API.MsgTemplate) => ({
        label: `${t.templateName} (${t.templateCode})`,
        value: t.templateCode,
      }));
  }, [templateData, channel]);

  const userOptions = useMemo(() => {
    return (userData || []).map((u: API.SysUser) => ({
      label: `${u.realName || u.username} (${u.username})`,
      value: u.userId,
    }));
  }, [userData]);

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm
        formRef={formRef}
        title={intl.formatMessage({ id: 'pages.message.list.sendTitle' })}
        open={open}
        onOpenChange={(visible) => {
          setOpen(visible);
          if (visible) {
            setChannel('site');
            setBroadcast(false);
            setSelectedTemplateCode(undefined);
            fetchTemplates();
            fetchUsers();
          }
        }}
        width={650}
        grid
        rowProps={{ gutter: [16, 0] }}
        initialValues={{
          channel: 'site',
          priority: 0,
          broadcast: false,
        }}
        modalProps={{
          destroyOnHidden: true,
          maskClosable: false,
        }}
        submitter={{
          submitButtonProps: {
            loading: sendLoading,
          },
        }}
        onFinish={handleFinish}
      >
        <ProFormSelect
          name="channel"
          label={intl.formatMessage({ id: 'pages.message.list.channel' })}
          colProps={{ span: 12 }}
          rules={[{ required: true }]}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.list.channel.site' }),
              value: 'site',
            },
            {
              label: intl.formatMessage({ id: 'pages.message.list.channel.email' }),
              value: 'email',
            },
          ]}
          fieldProps={{
            onChange: (value) => {
              setChannel(value);
              setBroadcast(false);
              setSelectedTemplateCode(undefined);
              formRef.current?.setFieldValue('templateCode', undefined);
              formRef.current?.setFieldValue('broadcast', false);
            },
          }}
        />
        <ProFormSelect
          name="priority"
          label={intl.formatMessage({ id: 'pages.message.list.priority' })}
          colProps={{ span: 12 }}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.message.list.priority.normal' }),
              value: 0,
            },
            {
              label: intl.formatMessage({ id: 'pages.message.list.priority.important' }),
              value: 1,
            },
            {
              label: intl.formatMessage({ id: 'pages.message.list.priority.urgent' }),
              value: 2,
            },
          ]}
        />
        <ProFormSelect
          name="templateCode"
          label={intl.formatMessage({ id: 'pages.message.list.templateCode' })}
          colProps={{ span: 24 }}
          options={templateOptions}
          placeholder={intl.formatMessage({ id: 'pages.message.list.templateCodePlaceholder' })}
          fieldProps={{
            onChange: (value) => setSelectedTemplateCode(value),
            allowClear: true,
          }}
        />

        {/* 模板信息展示 */}
        {selectedTemplate && (
          <div style={{ marginBottom: 16, width: '100%' }}>
            <Alert
              type="info"
              showIcon
              message={intl.formatMessage({ id: 'pages.message.list.templateInfo' })}
              description={
                <div style={{ marginTop: 8 }}>
                  {selectedTemplate.templateSubject && (
                    <div style={{ marginBottom: 8 }}>
                      <Text strong>{intl.formatMessage({ id: 'pages.message.list.templateSubject' })}: </Text>
                      <Text>{selectedTemplate.templateSubject}</Text>
                    </div>
                  )}
                  <div style={{ marginBottom: 8 }}>
                    <Text strong>{intl.formatMessage({ id: 'pages.message.list.templateContent' })}: </Text>
                    <div
                      style={{
                        background: '#fafafa',
                        padding: '8px 12px',
                        borderRadius: 4,
                        marginTop: 4,
                        whiteSpace: 'pre-wrap',
                        wordBreak: 'break-word',
                        maxHeight: 120,
                        overflow: 'auto',
                      }}
                    >
                      {selectedTemplate.templateContent}
                    </div>
                  </div>
                  {templateVariables.length > 0 && (
                    <div>
                      <Text strong>{intl.formatMessage({ id: 'pages.message.list.templateVariables' })}: </Text>
                      <Space size={4} wrap style={{ marginTop: 4 }}>
                        {templateVariables.map((v: string) => (
                          <Tag key={v} color="blue">${'{' + v + '}'}</Tag>
                        ))}
                      </Space>
                    </div>
                  )}
                </div>
              }
            />
          </div>
        )}

        {/* 模板变量输入 */}
        {templateVariables.length > 0 && (
          <>
            {templateVariables.map((variable: string) => (
              <ProFormText
                key={variable}
                name={`var_${variable}`}
                label={`${intl.formatMessage({ id: 'pages.message.list.variable' })}: ${variable}`}
                colProps={{ span: 12 }}
                rules={[
                  {
                    required: true,
                    message: intl.formatMessage(
                      { id: 'pages.message.list.variableRequired' },
                      { name: variable }
                    ),
                  },
                ]}
                placeholder={intl.formatMessage(
                  { id: 'pages.message.list.variablePlaceholder' },
                  { name: variable }
                )}
              />
            ))}
          </>
        )}

        {channel === 'site' && (
          <ProFormSwitch
            name="broadcast"
            label={intl.formatMessage({ id: 'pages.message.list.broadcast' })}
            colProps={{ span: 24 }}
            fieldProps={{
              onChange: (checked) => {
                setBroadcast(checked);
                if (checked) {
                  formRef.current?.setFieldValue('userIds', undefined);
                }
              },
            }}
            extra={intl.formatMessage({ id: 'pages.message.list.broadcastExtra' })}
          />
        )}

        {channel === 'site' ? (
          !broadcast && (
            <ProFormSelect
              name="userIds"
              label={intl.formatMessage({ id: 'pages.message.list.recipients' })}
              colProps={{ span: 24 }}
              rules={[
                {
                  required: true,
                  message: intl.formatMessage({ id: 'pages.message.list.recipientsRequired' }),
                },
              ]}
              options={userOptions}
              fieldProps={{
                mode: 'multiple',
                showSearch: true,
                filterOption: (input, option) =>
                  (option?.label as string)?.toLowerCase().includes(input.toLowerCase()),
              }}
              placeholder={intl.formatMessage({ id: 'pages.message.list.recipientsPlaceholder' })}
            />
          )
        ) : (
          <ProFormTextArea
            name="emails"
            label={intl.formatMessage({ id: 'pages.message.list.emails' })}
            colProps={{ span: 24 }}
            rules={[
              {
                required: true,
                message: intl.formatMessage({ id: 'pages.message.list.emailsRequired' }),
              },
            ]}
            fieldProps={{
              rows: 2,
            }}
            placeholder={intl.formatMessage({ id: 'pages.message.list.emailsPlaceholder' })}
          />
        )}

        {/* 使用模板时隐藏标题和内容 */}
        {!selectedTemplateCode && (
          <>
            <ProFormText
              name="title"
              label={intl.formatMessage({ id: 'pages.message.list.title.label' })}
              colProps={{ span: 24 }}
              rules={[
                {
                  required: true,
                  message: intl.formatMessage({ id: 'pages.message.list.titleRequired' }),
                },
              ]}
              placeholder={intl.formatMessage({ id: 'pages.message.list.titlePlaceholder' })}
            />
            <ProFormTextArea
              name="content"
              label={intl.formatMessage({ id: 'pages.message.list.content' })}
              colProps={{ span: 24 }}
              rules={[
                {
                  required: true,
                  message: intl.formatMessage({ id: 'pages.message.list.contentRequired' }),
                },
              ]}
              fieldProps={{
                rows: 4,
                maxLength: 2000,
                showCount: true,
              }}
              placeholder={intl.formatMessage({ id: 'pages.message.list.contentPlaceholder' })}
            />
          </>
        )}
      </ModalForm>
    </>
  );
};

export default MessageForm;
