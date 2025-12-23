import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, message, Popconfirm, Space, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import { deleteDept, getDeptTree, changeDeptStatus } from '@/services/ant-design-pro/api';
import DeptForm from './components/DeptForm';

const DeptManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [expandedRowKeys, setExpandedRowKeys] = useState<React.Key[]>([]);
  const [deptData, setDeptData] = useState<API.SysDept[]>([]);

  const intl = useIntl();
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun, loading: delLoading } = useRequest(deleteDept, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.dept.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.dept.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const { run: changeStatusRun } = useRequest(changeDeptStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.dept.statusChangeSuccess', defaultMessage: '状态修改成功' }),
      );
    },
  });

  const handleExpandAll = useCallback((data: API.SysDept[]) => {
    const keys: React.Key[] = [];
    const collectKeys = (depts: API.SysDept[]) => {
      depts.forEach((dept) => {
        if (dept.deptId) {
          keys.push(dept.deptId);
        }
        if (dept.children && dept.children.length > 0) {
          collectKeys(dept.children);
        }
      });
    };
    collectKeys(data);
    setExpandedRowKeys(keys);
  }, []);

  const handleCollapseAll = useCallback(() => {
    setExpandedRowKeys([]);
  }, []);

  const columns: ProColumns<API.SysDept>[] = [
    {
      title: intl.formatMessage({ id: 'pages.dept.deptName', defaultMessage: '部门名称' }),
      dataIndex: 'deptName',
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.sort', defaultMessage: '排序' }),
      dataIndex: 'sort',
      width: 80,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.leader', defaultMessage: '负责人' }),
      dataIndex: 'leader',
      width: 120,
      hideInSearch: true,
      render: (_, record) => record.leader || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.phone', defaultMessage: '联系电话' }),
      dataIndex: 'phone',
      width: 150,
      hideInSearch: true,
      render: (_, record) => record.phone || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.email', defaultMessage: '邮箱' }),
      dataIndex: 'email',
      width: 180,
      hideInSearch: true,
      ellipsis: true,
      render: (_, record) => record.email || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      width: 100,
      valueEnum: {
        '0': {
          text: intl.formatMessage({ id: 'pages.dept.status.normal', defaultMessage: '正常' }),
          status: 'Success',
        },
        '1': {
          text: intl.formatMessage({ id: 'pages.dept.status.disabled', defaultMessage: '停用' }),
          status: 'Error',
        },
      },
      render: (_, record) => (
        <Switch
          checked={record.status === '0'}
          checkedChildren={intl.formatMessage({ id: 'pages.dept.status.normal', defaultMessage: '正常' })}
          unCheckedChildren={intl.formatMessage({ id: 'pages.dept.status.disabled', defaultMessage: '停用' })}
          size="small"
          disabled={!access.hasPermission('system:dept:edit')}
          onChange={(checked) => {
            changeStatusRun({
              id: record.deptId!,
              status: checked ? '0' : '1',
            });
          }}
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.createTime', defaultMessage: '创建时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      width: 180,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.dept.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      render: (_, record) => [
        access.hasPermission('system:dept:edit') && (
          <DeptForm
            key="edit"
            trigger={<a>{intl.formatMessage({ id: 'pages.dept.edit', defaultMessage: '编辑' })}</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:dept:add') && (
          <DeptForm
            key="addChild"
            trigger={<a>{intl.formatMessage({ id: 'pages.dept.addChild', defaultMessage: '新增' })}</a>}
            parentId={record.deptId}
            parentName={record.deptName}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:dept:remove') && (
          <Popconfirm
            key="delete"
            title={intl.formatMessage({ id: 'pages.dept.confirmDelete', defaultMessage: '确定删除该部门吗?' })}
            onConfirm={() => delRun(record.deptId!)}
          >
            <a style={{ color: '#ff4d4f' }}>
              {intl.formatMessage({ id: 'pages.dept.delete', defaultMessage: '删除' })}
            </a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysDept, API.SysDeptQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.dept.title', defaultMessage: '部门管理' })}
        actionRef={actionRef}
        rowKey="deptId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button key="expand" onClick={() => handleExpandAll(deptData)}>
            {intl.formatMessage({ id: 'pages.dept.expandAll', defaultMessage: '展开全部' })}
          </Button>,
          <Button key="collapse" onClick={handleCollapseAll}>
            {intl.formatMessage({ id: 'pages.dept.collapseAll', defaultMessage: '折叠全部' })}
          </Button>,
          access.hasPermission('system:dept:add') && (
            <DeptForm key="create" onOk={() => actionRef.current?.reload?.()} />
          ),
        ].filter(Boolean)}
        request={async (params) => {
          const res = await getDeptTree({
            deptName: params.deptName,
            status: params.status,
          });
          const processData = (depts: API.SysDept[]): API.SysDept[] => {
            return depts.map((dept) => ({
              ...dept,
              children: dept.children && dept.children.length > 0
                ? processData(dept.children)
                : undefined,
            }));
          };
          const data = processData(res.data || []);
          setDeptData(data);
          return {
            data,
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={false}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys(keys as React.Key[]),
        }}
      />
    </PageContainer>
  );
};

export default DeptManagement;
