import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { history, useAccess } from '@umijs/max';
import { Button, message, Popconfirm, Space, Tooltip } from 'antd';
import React, { useRef, useState } from 'react';
import {
  deleteGenTable,
  downloadCode,
  batchDownloadCode,
  getGenTableList,
  syncTable,
  regenerateConfig,
  batchRegenerateConfig,
  batchSyncTable,
  type GenTable,
} from '@/services/ant-design-pro/generator';
import ImportTableModal from './components/ImportTableModal';
import PreviewCodeModal from './components/PreviewCodeModal';

const GenTableList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<GenTable[]>([]);
  const [messageApi, contextHolder] = message.useMessage();
  const access = useAccess();

  const handleDelete = async (tableIds: number[]) => {
    try {
      await deleteGenTable(tableIds);
      messageApi.success('删除成功');
      actionRef.current?.reloadAndRest?.();
      setSelectedRows([]);
    } catch (error) {
      messageApi.error('删除失败');
    }
  };

  const handleSync = async (tableId: number) => {
    try {
      await syncTable(tableId);
      messageApi.success('同步成功');
      actionRef.current?.reload?.();
    } catch (error) {
      messageApi.error('同步失败');
    }
  };

  const handleRegenerate = async (tableId: number) => {
    try {
      await regenerateConfig(tableId);
      messageApi.success('重新生成配置成功');
      actionRef.current?.reload?.();
    } catch (error) {
      messageApi.error('重新生成配置失败');
    }
  };

  const handleBatchRegenerate = async () => {
    if (selectedRowsState.length === 0) {
      messageApi.warning('请选择要操作的表');
      return;
    }
    try {
      const tableIds = selectedRowsState.map((row) => row.tableId!);
      await batchRegenerateConfig(tableIds);
      messageApi.success('批量重新生成配置成功');
      actionRef.current?.reload?.();
      setSelectedRows([]);
    } catch (error) {
      messageApi.error('批量重新生成配置失败');
    }
  };

  const handleBatchSync = async () => {
    if (selectedRowsState.length === 0) {
      messageApi.warning('请选择要操作的表');
      return;
    }
    try {
      const tableIds = selectedRowsState.map((row) => row.tableId!);
      await batchSyncTable(tableIds);
      messageApi.success('批量同步成功');
      actionRef.current?.reload?.();
      setSelectedRows([]);
    } catch (error) {
      messageApi.error('批量同步失败');
    }
  };

  const handleDownload = (tableId: number) => {
    downloadCode(tableId);
  };

  const handleBatchDownload = () => {
    if (selectedRowsState.length === 0) {
      messageApi.warning('请选择要下载的表');
      return;
    }
    const tableIds = selectedRowsState.map((row) => row.tableId!);
    batchDownloadCode(tableIds);
  };

  const columns: ProColumns<GenTable>[] = [
    {
      title: '表名称',
      dataIndex: 'tableName',
      ellipsis: true,
    },
    {
      title: '表描述',
      dataIndex: 'tableComment',
      ellipsis: true,
      hideInSearch: true,
    },
    {
      title: '实体类名',
      dataIndex: 'className',
      hideInSearch: true,
    },
    {
      title: '模块名',
      dataIndex: 'moduleName',
      hideInSearch: true,
    },
    {
      title: '业务名',
      dataIndex: 'businessName',
      hideInSearch: true,
    },
    {
      title: '作者',
      dataIndex: 'author',
      hideInSearch: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      hideInSearch: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 320,
      render: (_, record) => (
        <Space size="small">
          {access.hasPermission('tool:gen:preview') && (
            <PreviewCodeModal
              tableId={record.tableId!}
              tableName={record.tableName!}
            />
          )}
          {access.hasPermission('tool:gen:edit') && (
            <a onClick={() => history.push(`/tool/gen/edit/${record.tableId}`)}>编辑</a>
          )}
          {access.hasPermission('tool:gen:code') && (
            <Tooltip title="下载代码">
              <a onClick={() => handleDownload(record.tableId!)}>生成</a>
            </Tooltip>
          )}
          {access.hasPermission('tool:gen:edit') && (
            <Popconfirm
              title="确定要根据最新配置重新生成吗？"
              onConfirm={() => handleRegenerate(record.tableId!)}
            >
              <a>刷新配置</a>
            </Popconfirm>
          )}
          {access.hasPermission('tool:gen:edit') && (
            <Popconfirm
              title="确定要同步数据库表结构吗？"
              onConfirm={() => handleSync(record.tableId!)}
            >
              <a>同步</a>
            </Popconfirm>
          )}
          {access.hasPermission('tool:gen:remove') && (
            <Popconfirm
              title="确定要删除吗？"
              onConfirm={() => handleDelete([record.tableId!])}
            >
              <a style={{ color: '#ff4d4f' }}>删除</a>
            </Popconfirm>
          )}
        </Space>
      ),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<GenTable>
        headerTitle="代码生成列表"
        actionRef={actionRef}
        rowKey="tableId"
        search={{
          labelWidth: 100,
        }}
        toolBarRender={() => [
          access.hasPermission('tool:gen:import') && (
            <ImportTableModal
              key="import"
              onSuccess={() => actionRef.current?.reload?.()}
            />
          ),
          access.hasPermission('tool:gen:code') && selectedRowsState.length > 0 && (
            <Button key="batchDownload" onClick={handleBatchDownload}>
              批量生成
            </Button>
          ),
          access.hasPermission('tool:gen:edit') && selectedRowsState.length > 0 && (
            <Popconfirm
              key="batchRegenerate"
              title="确定要根据最新配置重新生成选中的表吗？"
              onConfirm={handleBatchRegenerate}
            >
              <Button>批量刷新配置</Button>
            </Popconfirm>
          ),
          access.hasPermission('tool:gen:edit') && selectedRowsState.length > 0 && (
            <Popconfirm
              key="batchSync"
              title="确定要同步选中表的数据库结构吗？"
              onConfirm={handleBatchSync}
            >
              <Button>批量同步</Button>
            </Popconfirm>
          ),
          access.hasPermission('tool:gen:remove') && selectedRowsState.length > 0 && (
            <Popconfirm
              key="batchDelete"
              title="确定要删除选中的表吗？"
              onConfirm={() => handleDelete(selectedRowsState.map((row) => row.tableId!))}
            >
              <Button danger>批量删除</Button>
            </Popconfirm>
          ),
          access.hasPermission('tool:gen:config') && (
            <Button key="config" onClick={() => history.push('/tool/genConfig')}>
              生成配置
            </Button>
          ),
        ].filter(Boolean)}
        request={async (params) => {
          const res = await getGenTableList({
            tableName: params.tableName,
          });
          return {
            data: res.data || [],
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
    </PageContainer>
  );
};

export default GenTableList;
