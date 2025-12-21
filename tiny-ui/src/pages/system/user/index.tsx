import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, Drawer, message, Modal, Popconfirm, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  changeUserStatus,
  deleteUser,
  deleteUserBatch,
  getUserPage,
} from '@/services/ant-design-pro/api';
import UserForm from './components/UserForm';
import ResetPasswordForm from './components/ResetPasswordForm';

const UserList: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysUser>();
  const [selectedRowsState, setSelectedRows] = useState<API.SysUser[]>([]);

  const intl = useIntl();
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
      await delBatchRun(ids);
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
      title: intl.formatMessage({ id: 'pages.user.realName', defaultMessage: '姓名' }),
      dataIndex: 'realName',
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
          disabled={record.superAdmin === 1}
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
        <UserForm
          key="edit"
          trigger={<a>{intl.formatMessage({ id: 'pages.user.edit', defaultMessage: '编辑' })}</a>}
          values={record}
          onOk={() => actionRef.current?.reload?.()}
        />,
        <ResetPasswordForm
          key="resetPwd"
          trigger={<a>{intl.formatMessage({ id: 'pages.user.resetPassword', defaultMessage: '重置密码' })}</a>}
          userId={record.userId!}
        />,
        record.superAdmin !== 1 && (
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
      ],
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysUser, API.SysUserQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.user.title', defaultMessage: '用户管理' })}
        actionRef={actionRef}
        rowKey="userId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <UserForm key="create" onOk={() => actionRef.current?.reload?.()} />,
        ]}
        request={async (params) => {
          const res = await getUserPage({
            current: params.current,
            size: params.pageSize,
            username: params.username,
            realName: params.realName,
            phone: params.phone,
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
          getCheckboxProps: (record) => ({
            disabled: record.superAdmin === 1,
          }),
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
              {intl.formatMessage({ id: 'pages.user.selected', defaultMessage: '已选择' })}{' '}
              <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a>{' '}
              {intl.formatMessage({ id: 'pages.user.item', defaultMessage: '项' })}
            </div>
          }
        >
          <Button
            danger
            loading={delBatchLoading}
            onClick={() => handleBatchRemove(selectedRowsState)}
          >
            {intl.formatMessage({ id: 'pages.user.batchDelete', defaultMessage: '批量删除' })}
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
        {currentRow?.username && (
          <ProDescriptions<API.SysUser>
            column={2}
            title={currentRow?.username}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.userId,
            }}
            columns={columns as ProDescriptionsItemProps<API.SysUser>[]}
          />
        )}
      </Drawer>
    </PageContainer>
  );
};

export default UserList;
