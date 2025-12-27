import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Badge, Button, Drawer, message, Popconfirm, Segmented, Space, Tag, Typography } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  deleteUserMessage,
  getUserMessagePage,
  markAllMessageRead,
  markMessageRead,
} from '@/services/ant-design-pro/message';

const { Paragraph, Title } = Typography;

const MessageCenter: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.UserMessage>();
  const [readFilter, setReadFilter] = useState<string>('all');

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const messageTypeValueEnum = {
    system: {
      text: intl.formatMessage({ id: 'pages.message.center.messageType.system' }),
    },
    user: {
      text: intl.formatMessage({ id: 'pages.message.center.messageType.user' }),
    },
    notice: {
      text: intl.formatMessage({ id: 'pages.message.center.messageType.notice' }),
    },
  };

  const priorityConfig: Record<number, { color: string; text: string }> = {
    0: {
      color: 'default',
      text: intl.formatMessage({ id: 'pages.message.list.priority.normal' }),
    },
    1: {
      color: 'warning',
      text: intl.formatMessage({ id: 'pages.message.list.priority.important' }),
    },
    2: {
      color: 'error',
      text: intl.formatMessage({ id: 'pages.message.list.priority.urgent' }),
    },
  };

  const { run: markReadRun } = useRequest(markMessageRead, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.center.markReadSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.center.markReadFailed' }));
    },
  });

  const { run: markAllReadRun, loading: markAllReadLoading } = useRequest(markAllMessageRead, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.center.markAllReadSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.center.markAllReadFailed' }));
    },
  });

  const { run: deleteRun } = useRequest(deleteUserMessage, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.center.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.center.deleteFailed' }));
    },
  });

  const handleViewDetail = useCallback(
    (record: API.UserMessage) => {
      setCurrentRow(record);
      setShowDetail(true);
      if (record.isRead === '0' && record.recipientId) {
        markReadRun(record.recipientId);
      }
    },
    [markReadRun],
  );

  const columns: ProColumns<API.UserMessage>[] = [
    {
      title: intl.formatMessage({ id: 'pages.message.center.title.label' }),
      dataIndex: 'title',
      ellipsis: true,
      width: 300,
      render: (dom, entity) => (
        <Space>
          {entity.isRead === '0' && <Badge status="error" />}
          <a onClick={() => handleViewDetail(entity)}>{dom}</a>
          {entity.priority !== undefined && entity.priority > 0 && (
            <Tag color={priorityConfig[entity.priority]?.color}>
              {priorityConfig[entity.priority]?.text}
            </Tag>
          )}
        </Space>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.center.messageType' }),
      dataIndex: 'messageType',
      valueType: 'select',
      valueEnum: messageTypeValueEnum,
      width: 120,
      render: (_, record) => {
        const typeConfig: Record<string, { color: string; text: string }> = {
          system: {
            color: 'blue',
            text: intl.formatMessage({ id: 'pages.message.center.messageType.system' }),
          },
          user: {
            color: 'green',
            text: intl.formatMessage({ id: 'pages.message.center.messageType.user' }),
          },
          notice: {
            color: 'orange',
            text: intl.formatMessage({ id: 'pages.message.center.messageType.notice' }),
          },
        };
        const config = typeConfig[record.messageType || 'system'];
        return <Tag color={config?.color}>{config?.text}</Tag>;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.message.center.sender' }),
      dataIndex: 'senderName',
      hideInSearch: true,
      width: 120,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.center.time' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.center.status' }),
      dataIndex: 'isRead',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        <Tag color={record.isRead === '1' ? 'default' : 'processing'}>
          {record.isRead === '1'
            ? intl.formatMessage({ id: 'pages.message.center.status.read' })
            : intl.formatMessage({ id: 'pages.message.center.status.unread' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.center.option' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) => [
        <a key="view" onClick={() => handleViewDetail(record)}>
          {intl.formatMessage({ id: 'pages.message.center.viewDetail' })}
        </a>,
        record.isRead === '0' && (
          <a key="markRead" onClick={() => markReadRun(record.recipientId!)}>
            {intl.formatMessage({ id: 'pages.message.center.markRead' })}
          </a>
        ),
        <Popconfirm
          key="delete"
          title={intl.formatMessage({ id: 'pages.message.center.confirmDelete' })}
          onConfirm={() => deleteRun(record.recipientId!)}
        >
          <a style={{ color: '#ff4d4f' }}>
            {intl.formatMessage({ id: 'pages.message.center.delete' })}
          </a>
        </Popconfirm>,
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.UserMessage, API.UserMessageQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.message.center.title' })}
        actionRef={actionRef}
        rowKey="recipientId"
        search={{
          labelWidth: 120,
        }}
        toolbar={{
          menu: {
            type: 'tab',
            activeKey: readFilter,
            items: [
              {
                key: 'all',
                label: intl.formatMessage({ id: 'pages.message.center.all' }),
              },
              {
                key: 'unread',
                label: intl.formatMessage({ id: 'pages.message.center.unread' }),
              },
              {
                key: 'read',
                label: intl.formatMessage({ id: 'pages.message.center.read' }),
              },
            ],
            onChange: (key) => {
              setReadFilter(key as string);
              actionRef.current?.reloadAndRest?.();
            },
          },
        }}
        toolBarRender={() => [
          <Button
            key="markAllRead"
            loading={markAllReadLoading}
            onClick={() => markAllReadRun()}
          >
            {intl.formatMessage({ id: 'pages.message.center.markAllRead' })}
          </Button>,
        ]}
        request={async (params) => {
          let isRead: string | undefined;
          if (readFilter === 'unread') {
            isRead = '0';
          } else if (readFilter === 'read') {
            isRead = '1';
          }

          const res = await getUserMessagePage({
            current: params.current,
            size: params.pageSize,
            messageType: params.messageType,
            isRead,
            title: params.title,
          });
          return {
            data: res.data?.records || [],
            total: res.data?.total || 0,
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
        }}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={true}
        title={currentRow?.title}
      >
        {currentRow && (
          <div>
            <div style={{ marginBottom: 16 }}>
              <Space wrap>
                <Tag
                  color={
                    currentRow.messageType === 'system'
                      ? 'blue'
                      : currentRow.messageType === 'user'
                        ? 'green'
                        : 'orange'
                  }
                >
                  {messageTypeValueEnum[
                    currentRow.messageType as keyof typeof messageTypeValueEnum
                  ]?.text || currentRow.messageType}
                </Tag>
                {currentRow.priority !== undefined && currentRow.priority > 0 && (
                  <Tag color={priorityConfig[currentRow.priority]?.color}>
                    {priorityConfig[currentRow.priority]?.text}
                  </Tag>
                )}
                <Tag color={currentRow.isRead === '1' ? 'default' : 'processing'}>
                  {currentRow.isRead === '1'
                    ? intl.formatMessage({ id: 'pages.message.center.status.read' })
                    : intl.formatMessage({ id: 'pages.message.center.status.unread' })}
                </Tag>
              </Space>
            </div>
            <div style={{ marginBottom: 16, color: '#999' }}>
              <Space split="|">
                <span>
                  {intl.formatMessage({ id: 'pages.message.center.sender' })}: {currentRow.senderName || '-'}
                </span>
                <span>
                  {intl.formatMessage({ id: 'pages.message.center.time' })}: {currentRow.createTime}
                </span>
              </Space>
            </div>
            <div
              style={{
                whiteSpace: 'pre-wrap',
                wordBreak: 'break-word',
                padding: '16px',
                background: '#f5f5f5',
                borderRadius: '4px',
                minHeight: '200px',
              }}
            >
              {currentRow.content}
            </div>
          </div>
        )}
      </Drawer>
    </PageContainer>
  );
};

export default MessageCenter;
