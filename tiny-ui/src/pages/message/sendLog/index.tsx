import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { FooterToolbar, PageContainer, ProDescriptions, ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, Drawer, message, Popconfirm, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import { deleteSendLog, deleteSendLogBatch, getSendLogById, getSendLogPage, retrySend } from '@/services/ant-design-pro/message';

const SendLogManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.MsgSendLog>();
  const [selectedRowsState, setSelectedRows] = useState<API.MsgSendLog[]>([]);

  const access = useAccess();
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const channelValueEnum = {
    site: {
      text: intl.formatMessage({ id: 'pages.message.sendLog.channel.site' }),
    },
    email: {
      text: intl.formatMessage({ id: 'pages.message.sendLog.channel.email' }),
    },
  };

  const sendStatusValueEnum = {
    '0': {
      text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.pending' }),
      status: 'Default',
    },
    '1': {
      text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.sending' }),
      status: 'Processing',
    },
    '2': {
      text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.success' }),
      status: 'Success',
    },
    '3': {
      text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.failed' }),
      status: 'Error',
    },
  };

  const { run: retryRun } = useRequest(retrySend, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.sendLog.retrySuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.sendLog.retryFailed' }));
    },
  });

  const { run: delRun } = useRequest(deleteSendLog, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.sendLog.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.sendLog.deleteFailed' }));
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteSendLogBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.sendLog.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.sendLog.deleteFailed' }));
    },
  });

  const { run: getDetailRun } = useRequest(getSendLogById, {
    manual: true,
    onSuccess: (res) => {
      setCurrentRow(res.data);
      setShowDetail(true);
    },
  });

  const handleViewDetail = useCallback(
    (record: API.MsgSendLog) => {
      getDetailRun(record.logId!);
    },
    [getDetailRun],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.MsgSendLog[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(intl.formatMessage({ id: 'pages.message.sendLog.selectRequired' }));
        return;
      }
      const ids = selectedRows.map((row) => row.logId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const columns: ProColumns<API.MsgSendLog>[] = [
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.logId' }),
      dataIndex: 'logId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.messageId' }),
      dataIndex: 'messageId',
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.messageTitle' }),
      dataIndex: 'messageTitle',
      hideInSearch: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.channel' }),
      dataIndex: 'channel',
      valueType: 'select',
      valueEnum: channelValueEnum,
      width: 100,
      render: (_, record) => (
        <Tag color={record.channel === 'site' ? 'processing' : 'warning'}>
          {record.channel === 'site'
            ? intl.formatMessage({ id: 'pages.message.sendLog.channel.site' })
            : intl.formatMessage({ id: 'pages.message.sendLog.channel.email' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.recipientAddress' }),
      dataIndex: 'recipientAddress',
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus' }),
      dataIndex: 'sendStatus',
      valueType: 'select',
      valueEnum: sendStatusValueEnum,
      width: 100,
      render: (_, record) => {
        const statusConfig: Record<string, { color: string; text: string }> = {
          '0': {
            color: 'default',
            text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.pending' }),
          },
          '1': {
            color: 'processing',
            text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.sending' }),
          },
          '2': {
            color: 'success',
            text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.success' }),
          },
          '3': {
            color: 'error',
            text: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus.failed' }),
          },
        };
        const config = statusConfig[record.sendStatus || '0'];
        return <Tag color={config.color}>{config.text}</Tag>;
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.retryCount' }),
      dataIndex: 'retryCount',
      hideInSearch: true,
      width: 80,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.sendTime' }),
      dataIndex: 'sendTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.successTime' }),
      dataIndex: 'successTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.errorMsg' }),
      dataIndex: 'errorMsg',
      hideInSearch: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.sendLog.option' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) => [
        <a key="view" onClick={() => handleViewDetail(record)}>
          {intl.formatMessage({ id: 'pages.message.sendLog.view' })}
        </a>,
        access.hasPermission('message:log:retry') && record.sendStatus === '3' && (
          <Popconfirm
            key="retry"
            title={intl.formatMessage({ id: 'pages.message.sendLog.confirmRetry' })}
            onConfirm={() => retryRun(record.logId!)}
          >
            <a style={{ color: '#1890ff' }}>
              {intl.formatMessage({ id: 'pages.message.sendLog.retry' })}
            </a>
          </Popconfirm>
        ),
        access.hasPermission('message:log:remove') && (
          <Popconfirm
            key="delete"
            title={intl.formatMessage({ id: 'pages.message.sendLog.confirmDelete' })}
            onConfirm={() => delRun(record.logId!)}
          >
            <a style={{ color: '#ff4d4f' }}>
              {intl.formatMessage({ id: 'pages.message.sendLog.delete' })}
            </a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.MsgSendLog, API.MsgSendLogQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.message.sendLog.title' })}
        actionRef={actionRef}
        rowKey="logId"
        search={{
          labelWidth: 120,
        }}
        request={async (params) => {
          const res = await getSendLogPage({
            current: params.current,
            size: params.pageSize,
            messageId: params.messageId,
            channel: params.channel,
            recipientAddress: params.recipientAddress,
            sendStatus: params.sendStatus,
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
        scroll={{ x: 1400 }}
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar>
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.message.sendLog.batchDelete' })}
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
        title={intl.formatMessage({ id: 'pages.message.sendLog.detail' })}
      >
        {currentRow && (
          <ProDescriptions<API.MsgSendLog>
            column={2}
            dataSource={currentRow}
            columns={[
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.logId' }),
                dataIndex: 'logId',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.messageId' }),
                dataIndex: 'messageId',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.messageTitle' }),
                dataIndex: 'messageTitle',
                span: 2,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.channel' }),
                dataIndex: 'channel',
                valueEnum: channelValueEnum,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.sendStatus' }),
                dataIndex: 'sendStatus',
                valueEnum: sendStatusValueEnum,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.recipientAddress' }),
                dataIndex: 'recipientAddress',
                span: 2,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.sendTime' }),
                dataIndex: 'sendTime',
                valueType: 'dateTime',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.successTime' }),
                dataIndex: 'successTime',
                valueType: 'dateTime',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.retryCount' }),
                dataIndex: 'retryCount',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.errorCode' }),
                dataIndex: 'errorCode',
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.errorMsg' }),
                dataIndex: 'errorMsg',
                span: 2,
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.requestData' }),
                dataIndex: 'requestData',
                span: 2,
                render: (_, record) =>
                  record.requestData ? (
                    <pre
                      style={{
                        whiteSpace: 'pre-wrap',
                        wordBreak: 'break-word',
                        padding: '12px',
                        background: '#f5f5f5',
                        borderRadius: '4px',
                        maxHeight: '200px',
                        overflow: 'auto',
                      }}
                    >
                      {record.requestData}
                    </pre>
                  ) : (
                    '-'
                  ),
              },
              {
                title: intl.formatMessage({ id: 'pages.message.sendLog.responseData' }),
                dataIndex: 'responseData',
                span: 2,
                render: (_, record) =>
                  record.responseData ? (
                    <pre
                      style={{
                        whiteSpace: 'pre-wrap',
                        wordBreak: 'break-word',
                        padding: '12px',
                        background: '#f5f5f5',
                        borderRadius: '4px',
                        maxHeight: '200px',
                        overflow: 'auto',
                      }}
                    >
                      {record.responseData}
                    </pre>
                  ) : (
                    '-'
                  ),
              },
            ]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default SendLogManagement;
