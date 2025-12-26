import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import { useAccess, useRequest } from '@umijs/max';
import { Badge, Button, Drawer, message, Modal, Popconfirm, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import {
  changeNoticeStatus,
  changeNoticeTop,
  deleteNotice,
  deleteNoticeBatch,
  getNoticePage,
  markNoticeRead,
  markAllNoticeRead,
} from '@/services/ant-design-pro/notice';
import NoticeForm from './components/NoticeForm';
import { useDicts } from '@/hooks/useDict';
import { Viewer } from '@bytemd/react';
import gfm from '@bytemd/plugin-gfm';
import highlight from '@bytemd/plugin-highlight';
import zhHans from 'bytemd/locales/zh_Hans.json';
import 'bytemd/dist/index.css';
import 'highlight.js/styles/github.css';

const plugins = [gfm(), highlight()];

const NoticeManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysNotice>();
  const [selectedRowsState, setSelectedRows] = useState<API.SysNotice[]>([]);

  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { getOptions, getValueEnum } = useDicts(['sys_notice_type', 'sys_common_status']);

  const noticeTypeOptions = getOptions('sys_notice_type');
  const noticeTypeValueEnum = getValueEnum('sys_notice_type');
  const statusValueEnum = getValueEnum('sys_common_status');

  const { run: delRun, loading: delLoading } = useRequest(deleteNotice, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success('删除成功');
    },
    onError: () => {
      messageApi.error('删除失败');
    },
  });

  const { run: delBatchRun, loading: delBatchLoading } = useRequest(deleteNoticeBatch, {
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

  const { run: changeStatusRun } = useRequest(changeNoticeStatus, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success('状态修改成功');
    },
    onError: () => {
      messageApi.error('状态修改失败');
    },
  });

  const { run: changeTopRun } = useRequest(changeNoticeTop, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success('置顶状态修改成功');
    },
    onError: () => {
      messageApi.error('置顶状态修改失败');
    },
  });

  const { run: markReadRun } = useRequest(markNoticeRead, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
    },
  });

  const { run: markAllReadRun, loading: markAllReadLoading } = useRequest(markAllNoticeRead, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reload?.();
      messageApi.success('已全部标记为已读');
    },
    onError: () => {
      messageApi.error('操作失败');
    },
  });

  const handleViewDetail = useCallback(
    (record: API.SysNotice) => {
      setCurrentRow(record);
      setShowDetail(true);
      // 如果是未读状态，则标记为已读
      if (record.isRead === '0' && record.noticeId) {
        markReadRun(record.noticeId);
      }
    },
    [markReadRun],
  );

  const handleStatusChange = useCallback(
    (checked: boolean, record: API.SysNotice) => {
      Modal.confirm({
        title: '确认修改状态',
        content: `确定要${checked ? '启用' : '关闭'}公告"${record.noticeTitle}"吗?`,
        onOk: () => {
          changeStatusRun({ id: record.noticeId!, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun],
  );

  const handleTopChange = useCallback(
    (checked: boolean, record: API.SysNotice) => {
      Modal.confirm({
        title: '确认修改置顶状态',
        content: `确定要${checked ? '置顶' : '取消置顶'}公告"${record.noticeTitle}"吗?`,
        onOk: () => {
          changeTopRun({ id: record.noticeId!, isTop: checked ? '1' : '0' });
        },
      });
    },
    [changeTopRun],
  );

  const handleBatchRemove = useCallback(
    async (selectedRows: API.SysNotice[]) => {
      if (!selectedRows?.length) {
        messageApi.warning('请选择要删除的公告');
        return;
      }
      const ids = selectedRows.map((row) => row.noticeId!);
      await delBatchRun({ ids });
    },
    [delBatchRun, messageApi],
  );

  const columns: ProColumns<API.SysNotice>[] = [
    {
      title: '公告ID',
      dataIndex: 'noticeId',
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: '公告标题',
      dataIndex: 'noticeTitle',
      ellipsis: true,
      render: (dom, entity) => (
        <span>
          {entity.isRead === '0' && <Badge status="error" style={{ marginRight: 4 }} />}
          <a onClick={() => handleViewDetail(entity)}>{dom}</a>
        </span>
      ),
    },
    {
      title: '公告类型',
      dataIndex: 'noticeType',
      valueType: 'select',
      valueEnum: noticeTypeValueEnum,
      fieldProps: {
        options: noticeTypeOptions,
      },
      width: 100,
    },
    {
      title: '置顶',
      dataIndex: 'isTop',
      hideInSearch: true,
      width: 80,
      render: (_, record) => (
        <Switch
          checked={record.isTop === '1'}
          onChange={(checked) => handleTopChange(checked, record)}
          checkedChildren="是"
          unCheckedChildren="否"
          disabled={!access.hasPermission('info:notice:edit')}
        />
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
          unCheckedChildren="关闭"
          disabled={!access.hasPermission('info:notice:edit')}
        />
      ),
    },
    {
      title: '创建者',
      dataIndex: 'createBy',
      hideInSearch: true,
      width: 100,
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
        access.hasPermission('info:notice:edit') && (
          <NoticeForm
            key="edit"
            trigger={<a>编辑</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('info:notice:remove') && (
          <Popconfirm
            key="delete"
            title="确定删除该公告吗?"
            onConfirm={() => delRun(record.noticeId!)}
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
      <ProTable<API.SysNotice, API.SysNoticeQueryParams>
        headerTitle="通知公告"
        actionRef={actionRef}
        rowKey="noticeId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            key="markAllRead"
            loading={markAllReadLoading}
            onClick={() => markAllReadRun()}
          >
            全部标记已读
          </Button>,
          access.hasPermission('info:notice:add') && (
            <NoticeForm key="create" onOk={() => actionRef.current?.reload?.()} />
          ),
        ].filter(Boolean)}
        request={async (params) => {
          const res = await getNoticePage({
            current: params.current,
            size: params.pageSize,
            noticeTitle: params.noticeTitle,
            noticeType: params.noticeType,
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
        width={800}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={true}
        title={currentRow?.noticeTitle}
      >
        {currentRow?.noticeTitle && (
          <div>
            <div style={{ marginBottom: 16 }}>
              <Tag color={currentRow?.noticeType === '1' ? 'processing' : 'warning'}>
                {currentRow?.noticeType === '1' ? '通知' : '公告'}
              </Tag>
              {currentRow?.isTop === '1' && <Tag color="red">置顶</Tag>}
              <Tag color={currentRow?.status === '0' ? 'success' : 'default'}>
                {currentRow?.status === '0' ? '正常' : '关闭'}
              </Tag>
              <span style={{ marginLeft: 16, color: '#999' }}>
                创建者: {currentRow?.createBy} | 创建时间: {currentRow?.createTime}
              </span>
            </div>
            <div className="markdown-body">
              <Viewer value={currentRow?.noticeContent || ''} plugins={plugins} />
            </div>
          </div>
        )}
      </Drawer>
    </PageContainer>
  );
};

export default NoticeManagement;
