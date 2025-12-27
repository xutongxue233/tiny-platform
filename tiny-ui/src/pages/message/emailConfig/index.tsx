import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, message, Popconfirm, Space, Switch, Tag } from 'antd';
import React, { useCallback, useRef } from 'react';
import {
  deleteEmailConfig,
  getEmailConfigList,
  setDefaultEmailConfig,
  testEmailConfig,
} from '@/services/ant-design-pro/message';
import EmailConfigForm from './components/EmailConfigForm';

const EmailConfigManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);

  const access = useAccess();
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun, loading: delLoading } = useRequest(deleteEmailConfig, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.emailConfig.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.emailConfig.deleteFailed' }));
    },
  });

  const { run: setDefaultRun } = useRequest(setDefaultEmailConfig, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.emailConfig.setDefaultSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.emailConfig.setDefaultFailed' }));
    },
  });

  const { run: testRun, loading: testLoading } = useRequest(testEmailConfig, {
    manual: true,
    onSuccess: () => {
      messageApi.success(intl.formatMessage({ id: 'pages.message.emailConfig.testSuccess' }));
    },
    onError: (err) => {
      messageApi.error(err.message || intl.formatMessage({ id: 'pages.message.emailConfig.testFailed' }));
    },
  });

  const handleSetDefault = useCallback(
    (configId: number) => {
      setDefaultRun(configId);
    },
    [setDefaultRun],
  );

  const handleTest = useCallback(
    (configId: number) => {
      testRun(configId);
    },
    [testRun],
  );

  const columns: ProColumns<API.MsgEmailConfig>[] = [
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.configId' }),
      dataIndex: 'configId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.configName' }),
      dataIndex: 'configName',
      ellipsis: true,
      width: 150,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.host' }),
      dataIndex: 'host',
      hideInSearch: true,
      ellipsis: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.port' }),
      dataIndex: 'port',
      hideInSearch: true,
      width: 80,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.username' }),
      dataIndex: 'username',
      hideInSearch: true,
      ellipsis: true,
      width: 180,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.fromAddress' }),
      dataIndex: 'fromAddress',
      hideInSearch: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable' }),
      dataIndex: 'sslEnable',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        <Tag color={record.sslEnable === '1' ? 'success' : 'default'}>
          {record.sslEnable === '1'
            ? intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable.yes' })
            : intl.formatMessage({ id: 'pages.message.emailConfig.sslEnable.no' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.isDefault' }),
      dataIndex: 'isDefault',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        <Tag color={record.isDefault === '1' ? 'processing' : 'default'}>
          {record.isDefault === '1'
            ? intl.formatMessage({ id: 'pages.message.emailConfig.isDefault.yes' })
            : intl.formatMessage({ id: 'pages.message.emailConfig.isDefault.no' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.status' }),
      dataIndex: 'status',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        <Tag color={record.status === '0' ? 'success' : 'default'}>
          {record.status === '0'
            ? intl.formatMessage({ id: 'pages.message.emailConfig.status.normal' })
            : intl.formatMessage({ id: 'pages.message.emailConfig.status.disabled' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.createTime' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.emailConfig.option' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 220,
      render: (_, record) =>
        [
          access.hasPermission('message:email:test') && (
            <a key="test" onClick={() => handleTest(record.configId!)}>
              {intl.formatMessage({ id: 'pages.message.emailConfig.testConnection' })}
            </a>
          ),
          access.hasPermission('message:email:edit') && record.isDefault !== '1' && (
            <Popconfirm
              key="setDefault"
              title={intl.formatMessage({ id: 'pages.message.emailConfig.confirmSetDefault' })}
              onConfirm={() => handleSetDefault(record.configId!)}
            >
              <a>{intl.formatMessage({ id: 'pages.message.emailConfig.setDefault' })}</a>
            </Popconfirm>
          ),
          access.hasPermission('message:email:edit') && (
            <EmailConfigForm
              key="edit"
              trigger={<a>{intl.formatMessage({ id: 'pages.message.emailConfig.edit' })}</a>}
              values={record}
              onOk={() => actionRef.current?.reload?.()}
            />
          ),
          access.hasPermission('message:email:remove') && (
            <Popconfirm
              key="delete"
              title={intl.formatMessage({ id: 'pages.message.emailConfig.confirmDelete' })}
              onConfirm={() => delRun(record.configId!)}
            >
              <a style={{ color: '#ff4d4f' }}>
                {intl.formatMessage({ id: 'pages.message.emailConfig.delete' })}
              </a>
            </Popconfirm>
          ),
        ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.MsgEmailConfig>
        headerTitle={intl.formatMessage({ id: 'pages.message.emailConfig.title' })}
        actionRef={actionRef}
        rowKey="configId"
        search={false}
        scroll={{ x: 1200 }}
        toolBarRender={() =>
          [
            access.hasPermission('message:email:add') && (
              <EmailConfigForm key="create" onOk={() => actionRef.current?.reload?.()} />
            ),
          ].filter(Boolean)
        }
        request={async () => {
          const res = await getEmailConfigList();
          return {
            data: res.data || [],
            total: res.data?.length || 0,
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={false}
      />
    </PageContainer>
  );
};

export default EmailConfigManagement;
