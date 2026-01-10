/**
 * 通用 CRUD 表格 Hook
 * 封装表格常用的增删改查操作，减少重复代码
 */
import type { ActionType } from '@ant-design/pro-components';
import { useRequest } from '@umijs/max';
import { message, Modal } from 'antd';
import { useCallback, useRef, useState } from 'react';

export interface UseCrudTableOptions<T, DeleteParams = number> {
  /** 删除单条记录的 API */
  deleteApi?: (id: DeleteParams) => Promise<any>;
  /** 批量删除的 API */
  deleteBatchApi?: (data: { ids: DeleteParams[] }) => Promise<any>;
  /** 修改状态的 API */
  changeStatusApi?: (data: { id: DeleteParams; status: string }) => Promise<any>;
  /** 获取记录 ID 的字段名，默认 'id' */
  rowKey?: keyof T | string;
  /** 获取记录名称的字段名，用于提示信息 */
  nameKey?: keyof T | string;
  /** 消息配置 */
  messages?: {
    deleteSuccess?: string;
    deleteFailed?: string;
    deleteConfirm?: string;
    batchDeleteConfirm?: string;
    selectRequired?: string;
    statusChangeSuccess?: string;
    statusChangeFailed?: string;
    statusChangeConfirm?: string;
  };
}

const defaultMessages = {
  deleteSuccess: '删除成功',
  deleteFailed: '删除失败',
  deleteConfirm: '确定删除该记录吗？',
  batchDeleteConfirm: '确定删除选中的记录吗？',
  selectRequired: '请选择要删除的记录',
  statusChangeSuccess: '状态修改成功',
  statusChangeFailed: '状态修改失败',
  statusChangeConfirm: '确定要修改状态吗？',
};

export function useCrudTable<T extends Record<string, any>, DeleteParams = number>(
  options: UseCrudTableOptions<T, DeleteParams> = {},
) {
  const {
    deleteApi,
    deleteBatchApi,
    changeStatusApi,
    rowKey = 'id',
    nameKey,
    messages: customMessages = {},
  } = options;

  const messages = { ...defaultMessages, ...customMessages };
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRows, setSelectedRows] = useState<T[]>([]);
  const [messageApi, contextHolder] = message.useMessage();

  // 获取记录 ID
  const getRowId = useCallback(
    (record: T): DeleteParams => {
      return record[rowKey as keyof T] as DeleteParams;
    },
    [rowKey],
  );

  // 获取记录名称
  const getRowName = useCallback(
    (record: T): string => {
      if (nameKey) {
        return String(record[nameKey as keyof T] || '');
      }
      return '';
    },
    [nameKey],
  );

  // 删除单条记录
  const { run: deleteRun, loading: deleteLoading } = useRequest(
    async (id: DeleteParams) => {
      if (!deleteApi) throw new Error('deleteApi is not defined');
      return deleteApi(id);
    },
    {
      manual: true,
      onSuccess: () => {
        actionRef.current?.reloadAndRest?.();
        messageApi.success(messages.deleteSuccess);
      },
      onError: () => {
        messageApi.error(messages.deleteFailed);
      },
    },
  );

  // 批量删除
  const { run: deleteBatchRun, loading: deleteBatchLoading } = useRequest(
    async (ids: DeleteParams[]) => {
      if (!deleteBatchApi) throw new Error('deleteBatchApi is not defined');
      return deleteBatchApi({ ids });
    },
    {
      manual: true,
      onSuccess: () => {
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
        messageApi.success(messages.deleteSuccess);
      },
      onError: () => {
        messageApi.error(messages.deleteFailed);
      },
    },
  );

  // 修改状态
  const { run: changeStatusRun, loading: changeStatusLoading } = useRequest(
    async (params: { id: DeleteParams; status: string }) => {
      if (!changeStatusApi) throw new Error('changeStatusApi is not defined');
      return changeStatusApi(params);
    },
    {
      manual: true,
      onSuccess: () => {
        actionRef.current?.reload?.();
        messageApi.success(messages.statusChangeSuccess);
      },
      onError: () => {
        messageApi.error(messages.statusChangeFailed);
      },
    },
  );

  // 删除单条记录（带确认）
  const handleDelete = useCallback(
    (record: T) => {
      const id = getRowId(record);
      deleteRun(id);
    },
    [deleteRun, getRowId],
  );

  // 批量删除（带确认）
  const handleBatchDelete = useCallback(async () => {
    if (!selectedRows?.length) {
      messageApi.warning(messages.selectRequired);
      return;
    }
    const ids = selectedRows.map((row) => getRowId(row));
    await deleteBatchRun(ids);
  }, [selectedRows, deleteBatchRun, getRowId, messageApi, messages.selectRequired]);

  // 状态变更（带确认弹窗）
  const handleStatusChange = useCallback(
    (checked: boolean, record: T, options?: { enableText?: string; disableText?: string }) => {
      const { enableText = '启用', disableText = '停用' } = options || {};
      const name = getRowName(record);
      const action = checked ? enableText : disableText;

      Modal.confirm({
        title: messages.statusChangeConfirm,
        content: name ? `确定要${action}"${name}"吗？` : `确定要${action}吗？`,
        onOk: () => {
          const id = getRowId(record);
          changeStatusRun({ id, status: checked ? '0' : '1' });
        },
      });
    },
    [changeStatusRun, getRowId, getRowName, messages.statusChangeConfirm],
  );

  // 刷新表格
  const reload = useCallback(() => {
    actionRef.current?.reload?.();
  }, []);

  // 刷新并重置
  const reloadAndRest = useCallback(() => {
    actionRef.current?.reloadAndRest?.();
  }, []);

  // 清空选中
  const clearSelected = useCallback(() => {
    setSelectedRows([]);
  }, []);

  // 行选择配置
  const rowSelection = {
    onChange: (_: React.Key[], rows: T[]) => {
      setSelectedRows(rows);
    },
  };

  return {
    // Refs
    actionRef,
    // 状态
    selectedRows,
    setSelectedRows,
    // Loading 状态
    deleteLoading,
    deleteBatchLoading,
    changeStatusLoading,
    // 操作方法
    handleDelete,
    handleBatchDelete,
    handleStatusChange,
    reload,
    reloadAndRest,
    clearSelected,
    // 配置
    rowSelection,
    // 消息上下文
    contextHolder,
    messageApi,
  };
}

export default useCrudTable;
