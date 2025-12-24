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
  cleanOperationLog,
  deleteOperationLogBatch,
  getOperationLogPage,
} from '@/services/ant-design-pro/api';
import OperationLogDetail from './components/OperationLogDetail';
import dayjs from 'dayjs';

const { RangePicker } = DatePicker;

const OperationLogList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<API.SysOperationLog[]>([]);
  const [detailOpen, setDetailOpen] = useState<boolean>(false);
  const [currentRecord, setCurrentRecord] = useState<API.SysOperationLog>();

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteOperationLogBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.operationLog.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.operationLog.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: cleanRun, loading: cleanLoading } = useRequest(cleanOperationLog, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.operationLog.cleanSuccess', defaultMessage: '清空成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.operationLog.cleanFailed', defaultMessage: '清空失败' }),
      );
    },
  });

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysOperationLog[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(
          intl.formatMessage({ id: 'pages.operationLog.selectRequired', defaultMessage: '请选择要删除的记录' }),
        );
        return;
      }
      const ids = selectedRows.map((row) => row.operationLogId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const handleClean = useCallback(() => {
    Modal.confirm({
      title: intl.formatMessage({ id: 'pages.operationLog.confirmClean', defaultMessage: '确认清空' }),
      content: intl.formatMessage({ id: 'pages.operationLog.cleanContent', defaultMessage: '确定要清空所有操作日志吗？此操作不可恢复！' }),
      okType: 'danger',
      onOk: () => cleanRun(),
    });
  }, [cleanRun, intl]);

  const handleViewDetail = (record: API.SysOperationLog) => {
    setCurrentRecord(record);
    setDetailOpen(true);
  };

  const methodColorMap: Record<string, string> = {
    GET: 'green',
    POST: 'blue',
    PUT: 'orange',
    DELETE: 'red',
    PATCH: 'cyan',
  };

  const columns: ProColumns<API.SysOperationLog>[] = [
    {
      title: intl.formatMessage({ id: 'pages.operationLog.operationLogId', defaultMessage: '日志ID' }),
      dataIndex: 'operationLogId',
      hideInSearch: true,
      width: 80,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.moduleName', defaultMessage: '模块名称' }),
      dataIndex: 'moduleName',
      width: 120,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.operationType', defaultMessage: '操作类型' }),
      dataIndex: 'operationType',
      width: 100,
      valueEnum: {
        OTHER: { text: '其他' },
        INSERT: { text: '新增' },
        UPDATE: { text: '修改' },
        DELETE: { text: '删除' },
        SELECT: { text: '查询' },
        IMPORT: { text: '导入' },
        EXPORT: { text: '导出' },
        LOGIN: { text: '登录' },
        LOGOUT: { text: '登出' },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.operationDesc', defaultMessage: '操作描述' }),
      dataIndex: 'operationDesc',
      hideInSearch: true,
      width: 150,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.username', defaultMessage: '操作人员' }),
      dataIndex: 'username',
      width: 100,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.requestMethod', defaultMessage: '请求方式' }),
      dataIndex: 'requestMethod',
      hideInSearch: true,
      width: 90,
      render: (_, record) => (
        <Tag color={methodColorMap[record.requestMethod || ''] || 'default'}>
          {record.requestMethod}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.ipAddr', defaultMessage: 'IP地址' }),
      dataIndex: 'ipAddr',
      hideInSearch: true,
      width: 130,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.operationLocation', defaultMessage: '操作地点' }),
      dataIndex: 'operationLocation',
      hideInSearch: true,
      width: 120,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      width: 80,
      valueEnum: {
        '0': { text: intl.formatMessage({ id: 'pages.operationLog.status.success', defaultMessage: '成功' }), status: 'Success' },
        '1': { text: intl.formatMessage({ id: 'pages.operationLog.status.fail', defaultMessage: '失败' }), status: 'Error' },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.executionTime', defaultMessage: '执行时长' }),
      dataIndex: 'executionTime',
      hideInSearch: true,
      width: 100,
      render: (_, record) => `${record.executionTime || 0} ms`,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.operationTime', defaultMessage: '操作时间' }),
      dataIndex: 'operationTime',
      valueType: 'dateTime',
      hideInSearch: true,
      width: 170,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.timeRange', defaultMessage: '时间范围' }),
      dataIndex: 'timeRange',
      valueType: 'dateTimeRange',
      hideInTable: true,
      renderFormItem: () => <RangePicker showTime />,
    },
    {
      title: intl.formatMessage({ id: 'pages.operationLog.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 80,
      fixed: 'right',
      render: (_, record) => [
        <a key="detail" onClick={() => handleViewDetail(record)}>
          {intl.formatMessage({ id: 'pages.operationLog.viewDetail', defaultMessage: '详情' })}
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysOperationLog, API.SysOperationLogQueryParams & { timeRange?: [string, string] }>
        headerTitle={intl.formatMessage({ id: 'pages.operationLog.title', defaultMessage: '操作日志' })}
        actionRef={actionRef}
        rowKey="operationLogId"
        scroll={{ x: 1400 }}
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
            {intl.formatMessage({ id: 'pages.operationLog.clean', defaultMessage: '清空日志' })}
          </Button>,
        ]}
        request={async (params) => {
          const { timeRange, ...restParams } = params;
          const res = await getOperationLogPage({
            current: restParams.current,
            size: restParams.pageSize,
            username: restParams.username,
            moduleName: restParams.moduleName,
            operationType: restParams.operationType,
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
        <FooterToolbar>
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.operationLog.batchDelete', defaultMessage: '批量删除' })}
          </Button>
        </FooterToolbar>
      )}

      <OperationLogDetail
        open={detailOpen}
        onClose={() => {
          setDetailOpen(false);
          setCurrentRecord(undefined);
        }}
        record={currentRecord}
      />
    </PageContainer>
  );
};

export default OperationLogList;
