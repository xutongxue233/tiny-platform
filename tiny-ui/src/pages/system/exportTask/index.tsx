import React, { useRef, useState } from 'react';
import { PageContainer } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Tag, Space, Popconfirm, Tooltip } from 'antd';
import {
  DeleteOutlined,
  DownloadOutlined,
  EyeOutlined,
  ClearOutlined,
  ReloadOutlined,
} from '@ant-design/icons';
import {
  getExportTaskPage,
  deleteExportTask,
  deleteExportTaskBatch,
  cleanExpiredTasks,
  getExportDownloadUrl,
} from '@/services/ant-design-pro/export';
import TaskDetail from './components/TaskDetail';

const ExportTaskList: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [detailVisible, setDetailVisible] = useState(false);
  const [currentTask, setCurrentTask] = useState<API.ExportTask | null>(null);

  // 状态标签颜色映射
  const statusMap: Record<string, { color: string; text: string }> = {
    '0': { color: 'default', text: '待处理' },
    '1': { color: 'processing', text: '处理中' },
    '2': { color: 'success', text: '成功' },
    '3': { color: 'error', text: '失败' },
  };

  // 任务类型标签
  const taskTypeMap: Record<string, { color: string; text: string }> = {
    export: { color: 'blue', text: '导出' },
    import: { color: 'green', text: '导入' },
  };

  // 查看详情
  const handleViewDetail = (record: API.ExportTask) => {
    setCurrentTask(record);
    setDetailVisible(true);
  };

  // 下载文件
  const handleDownload = (record: API.ExportTask) => {
    if (!record.taskId || record.status !== '2' || !record.fileUrl) {
      message.warning('该任务暂无可下载的文件');
      return;
    }
    window.open(getExportDownloadUrl(record.taskId), '_blank');
  };

  // 删除单个任务
  const handleDelete = async (taskId: number) => {
    try {
      await deleteExportTask(taskId);
      message.success('删除成功');
      actionRef.current?.reload();
    } catch (error: any) {
      message.error(error.message || '删除失败');
    }
  };

  // 批量删除
  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要删除的任务');
      return;
    }
    try {
      await deleteExportTaskBatch(selectedRowKeys as number[]);
      message.success('批量删除成功');
      setSelectedRowKeys([]);
      actionRef.current?.reload();
    } catch (error: any) {
      message.error(error.message || '批量删除失败');
    }
  };

  // 清理过期任务
  const handleCleanExpired = async () => {
    Modal.confirm({
      title: '确认清理',
      content: '确定要清理所有过期任务吗?清理后将无法恢复。',
      onOk: async () => {
        try {
          const res = await cleanExpiredTasks();
          message.success(`清理完成，共清理 ${res.data || 0} 条过期任务`);
          actionRef.current?.reload();
        } catch (error: any) {
          message.error(error.message || '清理失败');
        }
      },
    });
  };

  // 列定义
  const columns: ProColumns<API.ExportTask>[] = [
    {
      title: '任务ID',
      dataIndex: 'taskId',
      width: 80,
      hideInSearch: true,
    },
    {
      title: '任务名称',
      dataIndex: 'taskName',
      ellipsis: true,
      width: 200,
    },
    {
      title: '任务类型',
      dataIndex: 'taskType',
      width: 100,
      valueType: 'select',
      valueEnum: {
        export: { text: '导出' },
        import: { text: '导入' },
      },
      render: (_, record) => {
        const type = taskTypeMap[record.taskType || ''];
        return type ? <Tag color={type.color}>{type.text}</Tag> : '-';
      },
    },
    {
      title: '业务类型',
      dataIndex: 'businessType',
      width: 120,
      hideInSearch: true,
      render: (_, record) => record.businessTypeName || record.businessType || '-',
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      valueType: 'select',
      valueEnum: {
        '0': { text: '待处理', status: 'Default' },
        '1': { text: '处理中', status: 'Processing' },
        '2': { text: '成功', status: 'Success' },
        '3': { text: '失败', status: 'Error' },
      },
      render: (_, record) => {
        const status = statusMap[record.status || ''];
        return status ? <Tag color={status.color}>{status.text}</Tag> : '-';
      },
    },
    {
      title: '文件名',
      dataIndex: 'fileName',
      ellipsis: true,
      width: 180,
      hideInSearch: true,
    },
    {
      title: '记录数',
      dataIndex: 'totalCount',
      width: 100,
      hideInSearch: true,
      render: (_, record) => {
        if (record.totalCount === undefined) return '-';
        const success = record.successCount ?? 0;
        const fail = record.failCount ?? 0;
        return (
          <Tooltip title={`成功: ${success}, 失败: ${fail}`}>
            <span>
              {record.totalCount}
              {fail > 0 && <span style={{ color: 'red' }}> ({fail})</span>}
            </span>
          </Tooltip>
        );
      },
    },
    {
      title: '文件大小',
      dataIndex: 'fileSizeDesc',
      width: 100,
      hideInSearch: true,
    },
    {
      title: '耗时(ms)',
      dataIndex: 'duration',
      width: 100,
      hideInSearch: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      width: 170,
      hideInSearch: true,
    },
    {
      title: '创建时间',
      dataIndex: 'startTimeRange',
      valueType: 'dateRange',
      hideInTable: true,
      search: {
        transform: (value) => ({
          startTimeBegin: value[0],
          startTimeEnd: value[1],
        }),
      },
    },
    {
      title: '过期时间',
      dataIndex: 'expireTime',
      valueType: 'dateTime',
      width: 170,
      hideInSearch: true,
    },
    {
      title: '操作',
      valueType: 'option',
      width: 180,
      fixed: 'right',
      render: (_, record) => (
        <Space size="small">
          <Button
            type="link"
            size="small"
            icon={<EyeOutlined />}
            onClick={() => handleViewDetail(record)}
          >
            详情
          </Button>
          {record.status === '2' && record.fileUrl && (
            <Button
              type="link"
              size="small"
              icon={<DownloadOutlined />}
              onClick={() => handleDownload(record)}
            >
              下载
            </Button>
          )}
          <Popconfirm
            title="确定删除该任务吗?"
            onConfirm={() => handleDelete(record.taskId!)}
          >
            <Button type="link" size="small" danger icon={<DeleteOutlined />}>
              删除
            </Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.ExportTask>
        headerTitle="导出任务列表"
        actionRef={actionRef}
        rowKey="taskId"
        columns={columns}
        scroll={{ x: 1500 }}
        request={async (params) => {
          const { current, pageSize, ...rest } = params;
          const res = await getExportTaskPage({
            current,
            size: pageSize,
            ...rest,
          });
          return {
            data: res.data?.records || [],
            total: res.data?.total || 0,
            success: res.code === 200,
          };
        }}
        rowSelection={{
          selectedRowKeys,
          onChange: setSelectedRowKeys,
        }}
        toolBarRender={() => [
          <Button
            key="refresh"
            icon={<ReloadOutlined />}
            onClick={() => actionRef.current?.reload()}
          >
            刷新
          </Button>,
          <Button
            key="clean"
            icon={<ClearOutlined />}
            onClick={handleCleanExpired}
          >
            清理过期
          </Button>,
          <Popconfirm
            key="batchDelete"
            title="确定删除选中的任务吗?"
            onConfirm={handleBatchDelete}
            disabled={selectedRowKeys.length === 0}
          >
            <Button
              danger
              icon={<DeleteOutlined />}
              disabled={selectedRowKeys.length === 0}
            >
              批量删除
            </Button>
          </Popconfirm>,
        ]}
        pagination={{
          defaultPageSize: 10,
          showSizeChanger: true,
          showQuickJumper: true,
        }}
      />

      {/* 任务详情抽屉 */}
      <TaskDetail
        visible={detailVisible}
        task={currentTask}
        onClose={() => {
          setDetailVisible(false);
          setCurrentTask(null);
        }}
        onDownload={handleDownload}
      />
    </PageContainer>
  );
};

export default ExportTaskList;
