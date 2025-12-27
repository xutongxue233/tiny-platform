import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, Drawer, message, Popconfirm, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  deleteMessage,
  deleteMessageBatch,
  getMessageById,
  getMessagePage,
  revokeMessage,
} from '@/services/ant-design-pro/message';
import MessageForm from './components/MessageForm';

const MessageManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.MsgMessage>();
  const [selectedRowsState, setSelectedRows] = useState<API.MsgMessage[]>([]);

  const access = useAccess();
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const messageTypeValueEnum = {
    system: {
      text: intl.formatMessage({ id: 'pages.message.list.messageType.system' }),
    },
    user: {
      text: intl.formatMessage({ id: 'pages.message.list.messageType.user' }),
    },
  };

  const channelValueEnum = {
    site: {
      text: intl.formatMessage({ id: 'pages.message.list.channel.site' }),
    },
    email: {
      text: intl.formatMessage({ id: 'pages.message.list.channel.email' }),
    },
  };

  const priorityValueEnum = {
    0: { text: intl.formatMessage({ id: 'pages.message.list.priority.normal' }), color: 'default' },
    1: { text: intl.formatMessage({ id: 'pages.message.list.priority.important' }), color: 'warning' },
    2: { text: intl.formatMessage({ id: 'pages.message.list.priority.urgent' }), color: 'error' },
  };

  const statusValueEnum = {
    '0': {
      text: intl.formatMessage({ id: 'pages.message.list.status.normal' }),
      status: 'Success',
    },
    '1': {
      text: intl.formatMessage({ id: 'pages.message.list.status.revoked' }),
      status: 'Default',
    },
  };

  const { run: delRun, loading: delLoading } = useRequest(deleteMessage, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.list.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.list.deleteFailed' }));
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteMessageBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.list.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.list.deleteFailed' }));
    },
  });

  const { run: revokeRun } = useRequest(revokeMessage, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.list.revokeSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.list.revokeFailed' }));
    },
  });

  const { run: getDetailRun } = useRequest(getMessageById, {
    manual: true,
    onSuccess: (res) => {
      setCurrentRow(res.data);
      setShowDetail(true);
    },
  });

  const handleViewDetail = useCallback(
    (record: API.MsgMessage) => {
      getDetailRun(record.messageId!);
    },
    [getDetailRun],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.MsgMessage[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(intl.formatMessage({ id: 'pages.message.list.selectRequired' }));
        return;
      }
      const ids = selectedRows.map((row) => row.messageId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const columns: ProColumns<API.MsgMessage>[] = [
    {
      title: intl.formatMessage({ id: 'pages.message.list.messageId' }),
      dataIndex: 'messageId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.title.label' }),
      dataIndex: 'title',
      ellipsis: true,
      width: 200,
      render: (dom, entity) => <a onClick={() => handleViewDetail(entity)}>{dom}</a>,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.messageType' }),
      dataIndex: 'messageType',
      valueType: 'select',
      valueEnum: messageTypeValueEnum,
      width: 100,
      render: (_, record) => (
        <Tag color={record.messageType === 'system' ? 'blue' : 'green'}>
          {record.messageType === 'system'
            ? intl.formatMessage({ id: 'pages.message.list.messageType.system' })
            : intl.formatMessage({ id: 'pages.message.list.messageType.user' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.channel' }),
      dataIndex: 'channel',
      valueType: 'select',
      valueEnum: channelValueEnum,
      width: 100,
      render: (_, record) => (
        <Tag color={record.channel === 'site' ? 'processing' : 'warning'}>
          {record.channel === 'site'
            ? intl.formatMessage({ id: 'pages.message.list.channel.site' })
            : intl.formatMessage({ id: 'pages.message.list.channel.email' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.priority' }),
      dataIndex: 'priority',
      valueType: 'select',
      valueEnum: priorityValueEnum,
      width: 80,
      render: (_, record) => {
        const priority = record.priority || 0;
        const config = priorityValueEnum[priority as keyof typeof priorityValueEnum];
        return <Tag color={config?.color}>{config?.text}</Tag>;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.recipientCount' }),
      dataIndex: 'recipientCount',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.readCount' }),
      dataIndex: 'readCount',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.status' }),
      dataIndex: 'status',
      valueEnum: statusValueEnum,
      width: 80,
      render: (_, record) => (
        <Tag color={record.status === '0' ? 'success' : 'default'}>
          {record.status === '0'
            ? intl.formatMessage({ id: 'pages.message.list.status.normal' })
            : intl.formatMessage({ id: 'pages.message.list.status.revoked' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.createBy' }),
      dataIndex: 'createBy',
      hideInSearch: true,
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.createTime' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.list.option' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) =>
        [
          <a key="view" onClick={() => handleViewDetail(record)}>
            {intl.formatMessage({ id: 'pages.message.list.view' })}
          </a>,
          access.hasPermission('message:list:revoke') && record.status === '0' && record.channel === 'site' && (
            <Popconfirm
              key="revoke"
              title={intl.formatMessage({ id: 'pages.message.list.confirmRevoke' })}
              onConfirm={() => revokeRun(record.messageId!)}
            >
              <a style={{ color: '#faad14' }}>
                {intl.formatMessage({ id: 'pages.message.list.revoke' })}
              </a>
            </Popconfirm>
          ),
          access.hasPermission('message:list:remove') && (
            <Popconfirm
              key="delete"
              title={intl.formatMessage({ id: 'pages.message.list.confirmDelete' })}
              onConfirm={() => delRun(record.messageId!)}
            >
              <a style={{ color: '#ff4d4f' }}>
                {intl.formatMessage({ id: 'pages.message.list.delete' })}
              </a>
            </Popconfirm>
          ),
        ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.MsgMessage, API.MsgMessageQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.message.list.title' })}
        actionRef={actionRef}
        rowKey="messageId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() =>
          [
            access.hasPermission('message:list:send') && (
              <MessageForm key="send" onOk={() => actionRef.current?.reload?.()} />
            ),
          ].filter(Boolean)
        }
        request={async (params) => {
          const res = await getMessagePage({
            current: params.current,
            size: params.pageSize,
            messageType: params.messageType,
            channel: params.channel,
            title: params.title,
            priority: params.priority,
            status: params.status,
          });
          return {
            data: res.data?.records || [],
            total: res.data?.total || 0,
            success: res.code === 200,
          };
        }}
        columns={columns}
        rowSelection={{
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
        }}
        scroll={{ x: 1200 }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar>
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.message.list.batchDelete' })}
          </Button>
        </FooterToolbar>
      )}

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={true}
        title={intl.formatMessage({ id: 'pages.message.list.view' })}
      >
        {currentRow && (
          <ProDescriptions<API.MsgMessage>
            column={2}
            dataSource={currentRow}
            columns={[
              {
                title: intl.formatMessage({ id: 'pages.message.list.title.label' }),
                dataIndex: 'title',
                span: 2,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.messageType' }),
                dataIndex: 'messageType',
                valueEnum: messageTypeValueEnum,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.channel' }),
                dataIndex: 'channel',
                valueEnum: channelValueEnum,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.priority' }),
                dataIndex: 'priority',
                render: (_, record) => {
                  const priority = record.priority || 0;
                  const config = priorityValueEnum[priority as keyof typeof priorityValueEnum];
                  return <Tag color={config?.color}>{config?.text}</Tag>;
                },
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.status' }),
                dataIndex: 'status',
                valueEnum: statusValueEnum,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.recipientCount' }),
                dataIndex: 'recipientCount',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.readCount' }),
                dataIndex: 'readCount',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.createBy' }),
                dataIndex: 'createBy',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.createTime' }),
                dataIndex: 'createTime',
                valueType: 'dateTime',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.list.content' }),
                dataIndex: 'content',
                span: 2,
                valueType: 'text',
                render: (_, record) => (
                  <div
                    style={{
                      whiteSpace: 'pre-wrap',
                      wordBreak: 'break-word',
                      padding: '12px',
                      background: '#f5f5f5',
                      borderRadius: '4px',
                    }}
                  >
                    {record.content}
                  </div>
                ),
              },
            ]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default MessageManagement;
