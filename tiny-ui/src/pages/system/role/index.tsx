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
  changeRoleStatus,
  deleteRole,
  deleteRoleBatch,
  getRolePage,
} from '@/services/ant-design-pro/api';
import RoleForm from './components/RoleForm';

const RoleManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysRole>();
  const [selectedRowsState, setSelectedRows] = useState<API.SysRole[]>([]);

  const intl = useIntl();
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun, loading: delLoading } = useRequest(deleteRole, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.role.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.role.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteRoleBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.role.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.role.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: changeStatusRun } = useRequest(changeRoleStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.role.statusChangeSuccess', defaultMessage: '状态修改成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.role.statusChangeFailed', defaultMessage: '状态修改失败' }),
      );
    },
  });

  const handleStatusChange = useCallback(
    (checked: boolean, record: API.SysRole) => {
      Modal.confirm({
        title: intl.formatMessage({ id: 'pages.role.confirmStatusChange', defaultMessage: '确认修改状态' }),
        content: intl.formatMessage(
          { id: 'pages.role.statusChangeContent', defaultMessage: '确定要{action}角色"{name}"吗?' },
          { action: checked ? '启用' : '停用', name: record.roleName },
        ),
        onOk: () => {
          changeStatusRun({ id: record.roleId!, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun, intl],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysRole[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(
          intl.formatMessage({ id: 'pages.role.selectRequired', defaultMessage: '请选择要删除的角色' }),
        );
        return;
      }
      const ids = selectedRows.map((row) => row.roleId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const columns: ProColumns<API.SysRole>[] = [
    {
      title: intl.formatMessage({ id: 'pages.role.roleId', defaultMessage: '角色ID' }),
      dataIndex: 'roleId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.role.roleName', defaultMessage: '角色名称' }),
      dataIndex: 'roleName',
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
      title: intl.formatMessage({ id: 'pages.role.roleKey', defaultMessage: '角色标识' }),
      dataIndex: 'roleKey',
      render: (_, record) => <Tag color="blue">{record.roleKey}</Tag>,
    },
    {
      title: intl.formatMessage({ id: 'pages.role.sort', defaultMessage: '排序' }),
      dataIndex: 'sort',
      hideInSearch: true,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.role.dataScope', defaultMessage: '数据范围' }),
      dataIndex: 'dataScope',
      hideInSearch: true,
      valueEnum: {
        '1': { text: intl.formatMessage({ id: 'pages.role.dataScope.all', defaultMessage: '全部数据' }) },
        '2': { text: intl.formatMessage({ id: 'pages.role.dataScope.custom', defaultMessage: '自定义数据' }) },
        '3': { text: intl.formatMessage({ id: 'pages.role.dataScope.dept', defaultMessage: '本部门数据' }) },
        '4': { text: intl.formatMessage({ id: 'pages.role.dataScope.deptAndBelow', defaultMessage: '本部门及以下数据' }) },
        '5': { text: intl.formatMessage({ id: 'pages.role.dataScope.self', defaultMessage: '仅本人数据' }) },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.role.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      valueEnum: {
        '0': { text: intl.formatMessage({ id: 'pages.role.status.normal', defaultMessage: '正常' }), status: 'Success' },
        '1': { text: intl.formatMessage({ id: 'pages.role.status.disabled', defaultMessage: '停用' }), status: 'Error' },
      },
      render: (_, record) => (
        <Switch
          checked={record.status === '0'}
          onChange={(checked) => handleStatusChange(checked, record)}
          checkedChildren={intl.formatMessage({ id: 'pages.role.status.normal', defaultMessage: '正常' })}
          unCheckedChildren={intl.formatMessage({ id: 'pages.role.status.disabled', defaultMessage: '停用' })}
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.role.createTime', defaultMessage: '创建时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.role.remark', defaultMessage: '备注' }),
      dataIndex: 'remark',
      hideInSearch: true,
      hideInTable: true,
      ellipsis: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.role.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) => [
        access.hasPermission('system:role:edit') && (
          <RoleForm
            key="edit"
            trigger={<a>{intl.formatMessage({ id: 'pages.role.edit', defaultMessage: '编辑' })}</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:role:remove') && (
          <Popconfirm
            key="delete"
            title={intl.formatMessage({ id: 'pages.role.confirmDelete', defaultMessage: '确定删除该角色吗?' })}
            onConfirm={() => delRun(record.roleId!)}
          >
            <a style={{ color: '#ff4d4f' }}>
              {intl.formatMessage({ id: 'pages.role.delete', defaultMessage: '删除' })}
            </a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysRole, API.SysRoleQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.role.title', defaultMessage: '角色管理' })}
        actionRef={actionRef}
        rowKey="roleId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          access.hasPermission('system:role:add') && (
            <RoleForm key="create" onOk={() => actionRef.current?.reload?.()} />
          ),
        ].filter(Boolean)}
        request={async (params, sort) => {
          const res = await getRolePage({
            current: params.current,
            size: params.pageSize,
            roleName: params.roleName,
            roleKey: params.roleKey,
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
            {intl.formatMessage({ id: 'pages.role.batchDelete', defaultMessage: '批量删除' })}
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
        {currentRow?.roleName && (
          <ProDescriptions<API.SysRole>
            column={2}
            title={currentRow?.roleName}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.roleId,
            }}
            columns={columns as ProDescriptionsItemProps<API.SysRole>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default RoleManagement;
