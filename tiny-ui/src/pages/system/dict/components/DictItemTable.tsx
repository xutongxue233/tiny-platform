import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, message, Modal, Popconfirm, Switch, Tag } from 'antd';
import React, { forwardRef, useCallback, useImperativeHandle, useRef } from 'react';
import {
  changeDictItemStatus,
  deleteDictItem,
  deleteDictItemBatch,
  getDictItemPage,
} from '@/services/ant-design-pro/dict';
import DictItemForm from './DictItemForm';

interface DictItemTableProps {
  dictCode?: string;
  onChange: () => void;
}

const DictItemTable = forwardRef<ActionType, DictItemTableProps>(
  ({ dictCode, onChange }, ref) => {
    const actionRef = useRef<ActionType | null>(null);
    const intl = useIntl();
    const access = useAccess();
    const [messageApi, contextHolder] = message.useMessage();

    useImperativeHandle(ref, () => actionRef.current!);

    const { run: deleteRun } = useRequest(deleteDictItem, {
      manual: true,
      onSuccess: () => {
        actionRef.current?.reload();
        messageApi.success(
          intl.formatMessage({ id: 'pages.dict.deleteSuccess', defaultMessage: '删除成功' }),
        );
        onChange();
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.dict.deleteFailed', defaultMessage: '删除失败' }),
        );
      },
    });

    const { run: deleteBatchRun } = useRequest(deleteDictItemBatch, {
      manual: true,
      onSuccess: () => {
        actionRef.current?.clearSelected?.();
        actionRef.current?.reload();
        messageApi.success(
          intl.formatMessage({ id: 'pages.dict.deleteSuccess', defaultMessage: '删除成功' }),
        );
        onChange();
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.dict.deleteFailed', defaultMessage: '删除失败' }),
        );
      },
    });

    const { run: changeStatusRun } = useRequest(changeDictItemStatus, {
      manual: true,
      onSuccess: () => {
        actionRef.current?.reload();
        messageApi.success(
          intl.formatMessage({ id: 'pages.dict.statusChangeSuccess', defaultMessage: '状态修改成功' }),
        );
        onChange();
      },
      onError: () => {
        messageApi.error(
          intl.formatMessage({ id: 'pages.dict.statusChangeFailed', defaultMessage: '状态修改失败' }),
        );
      },
    });

    const handleStatusChange = useCallback(
      (checked: boolean, record: API.SysDictItem) => {
        Modal.confirm({
          title: intl.formatMessage({ id: 'pages.dict.confirmStatusChange', defaultMessage: '确认修改状态' }),
          content: intl.formatMessage(
            { id: 'pages.dict.itemStatusChangeContent', defaultMessage: '确定要{action}字典项"{name}"吗？' },
            { action: checked ? '启用' : '禁用', name: record.itemLabel },
          ),
          onOk: () => {
            changeStatusRun({ id: record.itemId!, status: checked ? '0' : '1' });
          },
        });
      },
      [changeStatusRun, intl],
    );

    const columns: ProColumns<API.SysDictItem>[] = [
      {
        title: intl.formatMessage({ id: 'pages.dict.itemLabel', defaultMessage: '字典标签' }),
        dataIndex: 'itemLabel',
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.itemValue', defaultMessage: '字典值' }),
        dataIndex: 'itemValue',
        hideInSearch: true,
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.itemSort', defaultMessage: '排序' }),
        dataIndex: 'itemSort',
        hideInSearch: true,
        width: 80,
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.isDefault', defaultMessage: '默认' }),
        dataIndex: 'isDefault',
        hideInSearch: true,
        width: 80,
        render: (_, record) => (
          <Tag color={record.isDefault === 'Y' ? 'green' : 'default'}>
            {record.isDefault === 'Y' ? '是' : '否'}
          </Tag>
        ),
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.status', defaultMessage: '状态' }),
        dataIndex: 'status',
        width: 100,
        valueEnum: {
          '0': { text: intl.formatMessage({ id: 'pages.dict.status.normal', defaultMessage: '正常' }), status: 'Success' },
          '1': { text: intl.formatMessage({ id: 'pages.dict.status.disabled', defaultMessage: '停用' }), status: 'Error' },
        },
        render: (_, record) => (
          <Switch
            checked={record.status === '0'}
            onChange={(checked) => handleStatusChange(checked, record)}
            checkedChildren={intl.formatMessage({ id: 'pages.dict.status.normal', defaultMessage: '正常' })}
            unCheckedChildren={intl.formatMessage({ id: 'pages.dict.status.disabled', defaultMessage: '停用' })}
            disabled={!access.hasPermission('system:dict:edit')}
          />
        ),
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.remark', defaultMessage: '备注' }),
        dataIndex: 'remark',
        hideInSearch: true,
        ellipsis: true,
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.createTime', defaultMessage: '创建时间' }),
        dataIndex: 'createTime',
        valueType: 'dateTime',
        hideInSearch: true,
        width: 160,
      },
      {
        title: intl.formatMessage({ id: 'pages.dict.option', defaultMessage: '操作' }),
        dataIndex: 'option',
        valueType: 'option',
        width: 120,
        render: (_, record) => [
          access.hasPermission('system:dict:edit') && (
            <DictItemForm
              key="edit"
              dictCode={dictCode!}
              values={record}
              trigger={<a>{intl.formatMessage({ id: 'pages.dict.edit', defaultMessage: '编辑' })}</a>}
              onOk={() => {
                actionRef.current?.reload();
                onChange();
              }}
            />
          ),
          access.hasPermission('system:dict:remove') && (
            <Popconfirm
              key="delete"
              title={intl.formatMessage({ id: 'pages.dict.confirmDeleteItem', defaultMessage: '确定删除该字典项吗？' })}
              onConfirm={() => deleteRun(record.itemId!)}
            >
              <a style={{ color: '#ff4d4f' }}>
                {intl.formatMessage({ id: 'pages.dict.delete', defaultMessage: '删除' })}
              </a>
            </Popconfirm>
          ),
        ].filter(Boolean),
      },
    ];

    if (!dictCode) {
      return (
        <div style={{ textAlign: 'center', padding: '50px 0', color: '#999' }}>
          {intl.formatMessage({ id: 'pages.dict.selectDictType', defaultMessage: '请选择左侧字典类型' })}
        </div>
      );
    }

    return (
      <>
        {contextHolder}
        <ProTable<API.SysDictItem, API.SysDictItemQueryParams>
          actionRef={actionRef}
          rowKey="itemId"
          search={{
            labelWidth: 80,
          }}
          toolBarRender={() => [
            access.hasPermission('system:dict:add') && (
              <DictItemForm
                key="create"
                dictCode={dictCode}
                onOk={() => {
                  actionRef.current?.reload();
                  onChange();
                }}
              />
            ),
          ].filter(Boolean)}
          request={async (params) => {
            const res = await getDictItemPage({
              current: params.current,
              size: params.pageSize,
              dictCode: dictCode,
              itemLabel: params.itemLabel,
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
            selections: [true],
          }}
          tableAlertOptionRender={({ selectedRowKeys, onCleanSelected }) => (
            <Button
              danger
              onClick={() => {
                deleteBatchRun({ ids: selectedRowKeys as number[] });
              }}
            >
              {intl.formatMessage({ id: 'pages.dict.batchDelete', defaultMessage: '批量删除' })}
            </Button>
          )}
          pagination={{
            defaultPageSize: 10,
            showSizeChanger: true,
            showQuickJumper: true,
          }}
        />
      </>
    );
  },
);

export default DictItemTable;
