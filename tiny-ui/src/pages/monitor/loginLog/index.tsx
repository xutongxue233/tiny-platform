import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message, Modal, Tag, DatePicker } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  cleanLoginLog,
  deleteLoginLogBatch,
  getLoginLogPage,
} from '@/services/ant-design-pro/api';
import dayjs from 'dayjs';

const { RangePicker } = DatePicker;

const LoginLogList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<API.SysLoginLog[]>([]);

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteLoginLogBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.loginLog.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.loginLog.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: cleanRun, loading: cleanLoading } = useRequest(cleanLoginLog, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.loginLog.cleanSuccess', defaultMessage: '清空成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.loginLog.cleanFailed', defaultMessage: '清空失败' }),
      );
    },
  });

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysLoginLog[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(
          intl.formatMessage({ id: 'pages.loginLog.selectRequired', defaultMessage: '请选择要删除的记录' }),
        );
        return;
      }
      const ids = selectedRows.map((row) => row.loginLogId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const handleClean = useCallback(() => {
    Modal.confirm({
      title: intl.formatMessage({ id: 'pages.loginLog.confirmClean', defaultMessage: '确认清空' }),
      content: intl.formatMessage({ id: 'pages.loginLog.cleanContent', defaultMessage: '确定要清空所有登录日志吗？此操作不可恢复！' }),
      okType: 'danger',
      onOk: () => cleanRun(),
    });
  }, [cleanRun, intl]);

  const columns: ProColumns<API.SysLoginLog>[] = [
    {
      title: intl.formatMessage({ id: 'pages.loginLog.loginLogId', defaultMessage: '日志ID' }),
      dataIndex: 'loginLogId',
      hideInSearch: true,
      width: 80,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.username', defaultMessage: '用户名' }),
      dataIndex: 'username',
      width: 120,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.loginType', defaultMessage: '登录类型' }),
      dataIndex: 'loginType',
      width: 100,
      valueEnum: {
        login: { text: intl.formatMessage({ id: 'pages.loginLog.loginType.login', defaultMessage: '登录' }) },
        logout: { text: intl.formatMessage({ id: 'pages.loginLog.loginType.logout', defaultMessage: '登出' }) },
      },
      render: (_, record) => {
        const colorMap: Record<string, string> = {
          login: 'green',
          logout: 'blue',
        };
        return (
          <Tag color={colorMap[record.loginType || ''] || 'default'}>
            {record.loginType === 'login' ? '登录' : '登出'}
          </Tag>
        );
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.ipAddr', defaultMessage: 'IP地址' }),
      dataIndex: 'ipAddr',
      width: 140,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.loginLocation', defaultMessage: '登录地点' }),
      dataIndex: 'loginLocation',
      hideInSearch: true,
      width: 150,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.browser', defaultMessage: '浏览器' }),
      dataIndex: 'browser',
      hideInSearch: true,
      width: 120,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.os', defaultMessage: '操作系统' }),
      dataIndex: 'os',
      hideInSearch: true,
      width: 120,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      width: 80,
      valueEnum: {
        '0': { text: intl.formatMessage({ id: 'pages.loginLog.status.success', defaultMessage: '成功' }), status: 'Success' },
        '1': { text: intl.formatMessage({ id: 'pages.loginLog.status.fail', defaultMessage: '失败' }), status: 'Error' },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.errorMsg', defaultMessage: '错误信息' }),
      dataIndex: 'errorMsg',
      hideInSearch: true,
      width: 200,
      ellipsis: true,
      render: (_, record) => record.errorMsg || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.loginTime', defaultMessage: '登录时间' }),
      dataIndex: 'loginTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.loginLog.timeRange', defaultMessage: '时间范围' }),
      dataIndex: 'timeRange',
      valueType: 'dateTimeRange',
      hideInTable: true,
      renderFormItem: () => <RangePicker showTime />,
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysLoginLog, API.SysLoginLogQueryParams & { timeRange?: [string, string] }>
        headerTitle={intl.formatMessage({ id: 'pages.loginLog.title', defaultMessage: '登录日志' })}
        actionRef={actionRef}
        rowKey="loginLogId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            key="clean"
            danger
            loading={cleanLoading}
            onClick={handleClean}
          >
            {intl.formatMessage({ id: 'pages.loginLog.clean', defaultMessage: '清空日志' })}
          </Button>,
        ]}
        request={async (params) => {
          const { timeRange, ...restParams } = params;
          const res = await getLoginLogPage({
            current: restParams.current,
            size: restParams.pageSize,
            username: restParams.username,
            ipAddr: restParams.ipAddr,
            loginType: restParams.loginType,
            status: restParams.status,
            beginTime: timeRange?.[0] ? dayjs(timeRange[0]).format('YYYY-MM-DD HH:mm:ss') : undefined,
            endTime: timeRange?.[1] ? dayjs(timeRange[1]).format('YYYY-MM-DD HH:mm:ss') : undefined,
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
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              {intl.formatMessage({ id: 'pages.loginLog.selected', defaultMessage: '已选择' })}{' '}
              <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a>{' '}
              {intl.formatMessage({ id: 'pages.loginLog.item', defaultMessage: '项' })}
            </div>
          }
        >
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.loginLog.batchDelete', defaultMessage: '批量删除' })}
          </Button>
        </FooterToolbar>
      )}
    </PageContainer>
  );
};

export default LoginLogList;