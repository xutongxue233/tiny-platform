import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProTable,
} from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, Col, message, Modal, Popconfirm, Row, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  changeUserStatus,
  deleteUser,
  deleteUserBatch,
  getUserPage,
} from '@/services/ant-design-pro/api';
import {
  exportUserData,
  exportUserDataAsync,
  getUserImportTemplateUrl,
  importUserData,
} from '@/services/ant-design-pro/export';
import { ExportButton, ImportButton } from '@/components/ExportImport';
import UserForm from './components/UserForm';
import ResetPasswordForm from './components/ResetPasswordForm';
import DeptTree from './components/DeptTree';

const UserList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<API.SysUser[]>([]);
  const [selectedDeptId, setSelectedDeptId] = useState<number | undefined>();

  const intl = useIntl();

  const handleDeptSelect = useCallback((deptId?: number) => {
    setSelectedDeptId(deptId);
    actionRef.current?.reloadAndRest?.();
  }, []);
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun, loading: delLoading } = useRequest(deleteUser, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.user.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.user.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteUserBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.user.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.user.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: changeStatusRun } = useRequest(changeUserStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.user.statusChangeSuccess', defaultMessage: '状态修改成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.user.statusChangeFailed', defaultMessage: '状态修改失败' }),
      );
    },
  });

  const handleStatusChange = useCallback(
    (checked: boolean, record: API.SysUser) => {
      Modal.confirm({
        title: intl.formatMessage({ id: 'pages.user.confirmStatusChange', defaultMessage: '确认修改状态' }),
        content: intl.formatMessage(
          { id: 'pages.user.statusChangeContent', defaultMessage: '确定要{action}用户"{name}"吗？' },
          { action: checked ? '启用' : '禁用', name: record.username },
        ),
        onOk: () => {
          changeStatusRun({ id: record.userId!, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun, intl],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysUser[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(
          intl.formatMessage({ id: 'pages.user.selectRequired', defaultMessage: '请选择要删除的用户' }),
        );
        return;
      }
      const ids = selectedRows.map((row) => row.userId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const columns: ProColumns<API.SysUser>[] = [
    {
      title: intl.formatMessage({ id: 'pages.user.userId', defaultMessage: '用户ID' }),
      dataIndex: 'userId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.user.username', defaultMessage: '用户名' }),
      dataIndex: 'username',
    },
    {
      title: intl.formatMessage({ id: 'pages.user.realName', defaultMessage: '姓名' }),
      dataIndex: 'realName',
    },
    {
      title: intl.formatMessage({ id: 'pages.user.dept', defaultMessage: '部门' }),
      dataIndex: 'deptName',
      hideInSearch: true,
      render: (_, record) => record.deptName || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.user.phone', defaultMessage: '手机号' }),
      dataIndex: 'phone',
    },
    {
      title: intl.formatMessage({ id: 'pages.user.email', defaultMessage: '邮箱' }),
      dataIndex: 'email',
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.user.gender', defaultMessage: '性别' }),
      dataIndex: 'gender',
      hideInSearch: true,
      valueEnum: {
        '0': { text: intl.formatMessage({ id: 'pages.user.gender.male', defaultMessage: '男' }) },
        '1': { text: intl.formatMessage({ id: 'pages.user.gender.female', defaultMessage: '女' }) },
        '2': { text: intl.formatMessage({ id: 'pages.user.gender.unknown', defaultMessage: '未知' }) },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.user.roles', defaultMessage: '角色' }),
      dataIndex: 'roleNames',
      hideInSearch: true,
      render: (_, record) => (
        <>
          {record.roleNames?.map((role) => (
            <Tag color="blue" key={role}>
              {role}
            </Tag>
          ))}
        </>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.user.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      valueEnum: {
        '0': { text: intl.formatMessage({ id: 'pages.user.status.normal', defaultMessage: '正常' }), status: 'Success' },
        '1': { text: intl.formatMessage({ id: 'pages.user.status.disabled', defaultMessage: '停用' }), status: 'Error' },
      },
      render: (_, record) => (
        <Switch
          checked={record.status === '0'}
          onChange={(checked) => handleStatusChange(checked, record)}
          checkedChildren={intl.formatMessage({ id: 'pages.user.status.normal', defaultMessage: '正常' })}
          unCheckedChildren={intl.formatMessage({ id: 'pages.user.status.disabled', defaultMessage: '停用' })}
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.user.createTime', defaultMessage: '创建时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.user.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 180,
      render: (_, record) => [
        access.hasPermission('system:user:edit') && (
          <UserForm
            key="edit"
            trigger={<a>{intl.formatMessage({ id: 'pages.user.edit', defaultMessage: '编辑' })}</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:user:edit') && (
          <ResetPasswordForm
            key="resetPwd"
            trigger={<a>{intl.formatMessage({ id: 'pages.user.resetPassword', defaultMessage: '重置密码' })}</a>}
            userId={record.userId!}
          />
        ),
        access.hasPermission('system:user:remove') && (
          <Popconfirm
            key="delete"
            title={intl.formatMessage({ id: 'pages.user.confirmDelete', defaultMessage: '确定删除该用户吗？' })}
            onConfirm={() => delRun(record.userId!)}
          >
            <a style={{ color: '#ff4d4f' }}>
              {intl.formatMessage({ id: 'pages.user.delete', defaultMessage: '删除' })}
            </a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <Row gutter={16}>
        <Col span={4}>
          <DeptTree onSelect={handleDeptSelect} />
        </Col>
        <Col span={20}>
          <ProTable<API.SysUser, API.SysUserQueryParams>
            headerTitle={intl.formatMessage({ id: 'pages.user.title', defaultMessage: '用户管理' })}
            actionRef={actionRef}
            rowKey="userId"
            search={{
              labelWidth: 120,
            }}
            toolBarRender={() => [
              access.hasPermission('system:user:add') && (
                <UserForm key="create" onOk={() => actionRef.current?.reload?.()} />
              ),
              access.hasPermission('system:user:export') && (
                <ExportButton
                  key="export"
                  buttonText={intl.formatMessage({ id: 'pages.user.export', defaultMessage: '导出' })}
                  fileName="用户数据"
                  showAsync={true}
                  onExportSync={async () => {
                    return await exportUserData({ deptId: selectedDeptId });
                  }}
                  onExportAsync={async () => {
                    const res = await exportUserDataAsync({ deptId: selectedDeptId });
                    return res.data!;
                  }}
                />
              ),
              access.hasPermission('system:user:import') && (
                <ImportButton
                  key="import"
                  buttonText={intl.formatMessage({ id: 'pages.user.import', defaultMessage: '导入' })}
                  templateUrl={getUserImportTemplateUrl()}
                  onImport={async (file) => {
                    const res = await importUserData(file);
                    return res.data!;
                  }}
                  onSuccess={() => actionRef.current?.reload?.()}
                />
              ),
            ].filter(Boolean)}
            request={async (params) => {
              const res = await getUserPage({
                current: params.current,
                size: params.pageSize,
                username: params.username,
                realName: params.realName,
                phone: params.phone,
                status: params.status,
                deptId: selectedDeptId,
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
        </Col>
      </Row>
      {selectedRowsState?.length > 0 && (
        <FooterToolbar>
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.user.batchDelete', defaultMessage: '批量删除' })}
          </Button>
        </FooterToolbar>
      )}
    </PageContainer>
  );
};

export default UserList;
