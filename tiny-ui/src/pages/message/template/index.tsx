import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { FooterToolbar, PageContainer, ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, message, Modal, Popconfirm, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  changeTemplateStatus,
  deleteTemplate,
  deleteTemplateBatch,
  getTemplatePage,
} from '@/services/ant-design-pro/message';
import TemplateForm from './components/TemplateForm';

const TemplateManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowsState, setSelectedRows] = useState<API.MsgTemplate[]>([]);

  const access = useAccess();
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const templateTypeValueEnum = {
    site: {
      text: intl.formatMessage({ id: 'pages.message.template.templateType.site' }),
      status: 'Processing',
    },
    email: {
      text: intl.formatMessage({ id: 'pages.message.template.templateType.email' }),
      status: 'Warning',
    },
  };

  const statusValueEnum = {
    '0': {
      text: intl.formatMessage({ id: 'pages.message.template.status.normal' }),
      status: 'Success',
    },
    '1': {
      text: intl.formatMessage({ id: 'pages.message.template.status.disabled' }),
      status: 'Default',
    },
  };

  const { run: delRun, loading: delLoading } = useRequest(deleteTemplate, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.template.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.template.deleteFailed' }));
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteTemplateBatch, {
    manual: true,
    onSuccess: () => {
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.template.deleteSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.template.deleteFailed' }));
    },
  });

  const { run: changeStatusRun } = useRequest(changeTemplateStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success(intl.formatMessage({ id: 'pages.message.template.statusChangeSuccess' }));
    },
    onError: () => {
      messageApi.error(intl.formatMessage({ id: 'pages.message.template.statusChangeFailed' }));
    },
  });

  const handleStatusChange = useCallback(
    (checked: boolean, record: API.MsgTemplate) => {
      const action = checked
        ? intl.formatMessage({ id: 'pages.message.common.enable' })
        : intl.formatMessage({ id: 'pages.message.common.disable' });
      Modal.confirm({
        title: intl.formatMessage({ id: 'pages.message.template.confirmStatusChange' }),
        content: intl.formatMessage(
          { id: 'pages.message.template.statusChangeContent' },
          { action, name: record.templateName },
        ),
        onOk: () => {
          changeStatusRun({ id: record.templateId!, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun, intl],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.MsgTemplate[]) => {
      if (!selectedRows?.length) {
        messageApi.warning(intl.formatMessage({ id: 'pages.message.template.selectRequired' }));
        return;
      }
      const ids = selectedRows.map((row) => row.templateId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, intl, messageApi],
  );

  const columns: ProColumns<API.MsgTemplate>[] = [
    {
      title: intl.formatMessage({ id: 'pages.message.template.templateId' }),
      dataIndex: 'templateId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.templateCode' }),
      dataIndex: 'templateCode',
      ellipsis: true,
      width: 150,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.templateName' }),
      dataIndex: 'templateName',
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.templateType' }),
      dataIndex: 'templateType',
      valueType: 'select',
      valueEnum: templateTypeValueEnum,
      width: 120,
      render: (_, record) => (
        <Tag color={record.templateType === 'site' ? 'processing' : 'warning'}>
          {record.templateType === 'site'
            ? intl.formatMessage({ id: 'pages.message.template.templateType.site' })
            : intl.formatMessage({ id: 'pages.message.template.templateType.email' })}
        </Tag>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.templateSubject' }),
      dataIndex: 'templateSubject',
      hideInSearch: true,
      ellipsis: true,
      width: 200,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.variables' }),
      dataIndex: 'variables',
      hideInSearch: true,
      ellipsis: true,
      width: 150,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.status' }),
      dataIndex: 'status',
      valueEnum: statusValueEnum,
      width: 100,
      render: (_, record) => (
        <Switch
          checked={record.status === '0'}
          onChange={(checked) => handleStatusChange(checked, record)}
          checkedChildren={intl.formatMessage({ id: 'pages.message.template.status.normal' })}
          unCheckedChildren={intl.formatMessage({ id: 'pages.message.template.status.disabled' })}
          disabled={!access.hasPermission('message:template:edit')}
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.createTime' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      sorter: true,
      width: 170,
    },
    {
      title: intl.formatMessage({ id: 'pages.message.template.option' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) =>
        [
          access.hasPermission('message:template:edit') && (
            <TemplateForm
              key="edit"
              trigger={<a>{intl.formatMessage({ id: 'pages.message.template.edit' })}</a>}
              values={record}
              onOk={() => actionRef.current?.reload?.()}
            />
          ),
          access.hasPermission('message:template:remove') && (
            <Popconfirm
              key="delete"
              title={intl.formatMessage({ id: 'pages.message.template.confirmDelete' })}
              onConfirm={() => delRun(record.templateId!)}
            >
              <a style={{ color: '#ff4d4f' }}>
                {intl.formatMessage({ id: 'pages.message.template.delete' })}
              </a>
            </Popconfirm>
          ),
        ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.MsgTemplate, API.MsgTemplateQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.message.template.title' })}
        actionRef={actionRef}
        rowKey="templateId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() =>
          [
            access.hasPermission('message:template:add') && (
              <TemplateForm key="create" onOk={() => actionRef.current?.reload?.()} />
            ),
          ].filter(Boolean)
        }
        request={async (params) => {
          const res = await getTemplatePage({
            current: params.current,
            size: params.pageSize,
            templateCode: params.templateCode,
            templateName: params.templateName,
            templateType: params.templateType,
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
            {intl.formatMessage({ id: 'pages.message.template.batchDelete' })}
          </Button>
        </FooterToolbar>
      )}
    </PageContainer>
  );
};

export default TemplateManagement;
