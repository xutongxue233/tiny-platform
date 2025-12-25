import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, Drawer, message, Modal, Popconfirm, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  changeConfigStatus,
  deleteConfig,
  deleteConfigBatch,
  getConfigPage,
  refreshConfigCache,
} from '@/services/ant-design-pro/config';
import ConfigForm from './components/ConfigForm';
import { useDicts } from '@/hooks/useDict';

const ConfigManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysConfig>();
  const [selectedRowsState, setSelectedRows] = useState<API.SysConfig[]>([]);

  const intl = useIntl();
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { getOptions, getValueEnum } = useDicts(['sys_config_group', 'sys_config_type', 'sys_common_status']);

  const configGroupOptions = getOptions('sys_config_group');
  const configTypeOptions = getOptions('sys_config_type');
  const configGroupValueEnum = getValueEnum('sys_config_group');
  const configTypeValueEnum = getValueEnum('sys_config_type');
  const statusValueEnum = getValueEnum('sys_common_status');

  const { run: delRun, loading: delLoading } = useRequest(deleteConfig, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success('删除成功');
    },
    onError: () => {
      messageApi.error('删除失败');
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteConfigBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success('删除成功');
    },
    onError: () => {
      messageApi.error('删除失败');
    },
  });

  const { run: changeStatusRun } = useRequest(changeConfigStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success('状态修改成功');
    },
    onError: () => {
      messageApi.error('状态修改失败');
    },
  });

  const { run: refreshCacheRun, loading: refreshLoading } = useRequest(refreshConfigCache, {
    manual: true,
    onSuccess: () => {
      messageApi.success('缓存刷新成功');
    },
    onError: () => {
      messageApi.error('缓存刷新失败');
    },
  });

  const handleStatusChange = useCallback(
    (checked: boolean, record: API.SysConfig) => {
      Modal.confirm({
        title: '确认修改状态',
        content: `确定要${checked ? '启用' : '停用'}参数"${record.configName}"吗?`,
        onOk: () => {
          changeStatusRun({ id: record.configId!, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysConfig[]) => {
      if (!selectedRows?.length) {
        messageApi.warning('请选择要删除的参数');
        return;
      }
      const builtinItems = selectedRows.filter((row) => row.isBuiltin === 'Y');
      if (builtinItems.length > 0) {
        messageApi.warning('内置参数不允许删除');
        return;
      }
      const ids = selectedRows.map((row) => row.configId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, messageApi],
  );

  const columns: ProColumns<API.SysConfig>[] = [
    {
      title: '参数ID',
      dataIndex: 'configId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: '参数名称',
      dataIndex: 'configName',
      ellipsis: true,
      render: (dom, entity) => (
        <a
          onClick={() => {
            setCurrentRow(entity);
            setShowDetail(true);
          }}
        >
          {dom}
        </a>
      ),
    },
    {
      title: '参数键名',
      dataIndex: 'configKey',
      ellipsis: true,
      copyable: true,
      render: (_, record) => <Tag color="blue">{record.configKey}</Tag>,
    },
    {
      title: '参数值',
      dataIndex: 'configValue',
      hideInSearch: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: '参数类型',
      dataIndex: 'configType',
      valueType: 'select',
      valueEnum: configTypeValueEnum,
      fieldProps: {
        options: configTypeOptions,
      },
      width: 100,
    },
    {
      title: '参数分组',
      dataIndex: 'configGroup',
      valueType: 'select',
      valueEnum: configGroupValueEnum,
      fieldProps: {
        options: configGroupOptions,
      },
      width: 100,
    },
    {
      title: '内置',
      dataIndex: 'isBuiltin',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        record.isBuiltin === 'Y'
          ? <Tag color="red">是</Tag>
          : <Tag color="default">否</Tag>
      ),
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: statusValueEnum,
      width: 100,
      render: (_, record) => (
        <Switch
          checked={record.status === '0'}
          onChange={(checked) => handleStatusChange(checked, record)}
          checkedChildren="正常"
          unCheckedChildren="停用"
        />
      ),
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: '备注',
      dataIndex: 'remark',
      hideInSearch: true,
      hideInTable: true,
      ellipsis: true,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) => [
        access.hasPermission('system:config:edit') && (
          <ConfigForm
            key="edit"
            trigger={<a>编辑</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:config:remove') && record.isBuiltin !== 'Y' && (
          <Popconfirm
            key="delete"
            title="确定删除该参数配置吗?"
            onConfirm={() => delRun(record.configId!)}
          >
            <a style={{ color: '#ff4d4f' }}>删除</a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysConfig, API.SysConfigQueryParams>
        headerTitle="参数配置"
        actionRef={actionRef}
        rowKey="configId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          access.hasPermission('system:config:add') && (
            <ConfigForm key="create" onOk={() => actionRef.current?.reload?.()} />
          ),
          access.hasPermission('system:config:refresh') && (
            <Button
              key="refresh"
              loading={refreshLoading}
              onClick={() => refreshCacheRun()}
            >
              刷新缓存
            </Button>
          ),
        ].filter(Boolean)}
        request={async (params, sort) => {
          const res = await getConfigPage({
            current: params.current,
            size: params.pageSize,
            configName: params.configName,
            configKey: params.configKey,
            configType: params.configType,
            configGroup: params.configGroup,
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
      />
      {selectedRowsState?.length > 0 && (
        <FooterToolbar>
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            批量删除
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
        closable={false}
      >
        {currentRow?.configName && (
          <ProDescriptions<API.SysConfig>
            column={2}
            title={currentRow?.configName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.configId,
            }}
            columns={columns as ProDescriptionsItemProps<API.SysConfig>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default ConfigManagement;
