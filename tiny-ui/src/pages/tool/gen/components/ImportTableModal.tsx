import { ModalForm, ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { Button, message } from 'antd';
import React, { useRef, useState } from 'react';
import { getDbTableList, importTable, type GenTable } from '@/services/ant-design-pro/generator';
import { PlusOutlined } from '@ant-design/icons';

interface ImportTableModalProps {
  onSuccess?: () => void;
}

const ImportTableModal: React.FC<ImportTableModalProps> = ({ onSuccess }) => {
  const actionRef = useRef<ActionType | null>(null);
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [messageApi, contextHolder] = message.useMessage();

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
  ];

  const handleSubmit = async () => {
    if (selectedRowKeys.length === 0) {
      messageApi.warning('请选择要导入的表');
      return false;
    }
    try {
      await importTable(selectedRowKeys as string[]);
      messageApi.success('导入成功');
      setSelectedRowKeys([]);
      onSuccess?.();
      return true;
    } catch (error) {
      messageApi.error('导入失败');
      return false;
    }
  };

  return (
    <>
      {contextHolder}
      <ModalForm
        title="导入表结构"
        trigger={
          <Button type="primary" icon={<PlusOutlined />}>
            导入表
          </Button>
        }
        width={800}
        modalProps={{
          destroyOnClose: true,
          onCancel: () => setSelectedRowKeys([]),
        }}
        submitter={{
          searchConfig: {
            submitText: '导入',
          },
        }}
        onFinish={handleSubmit}
      >
        <ProTable<GenTable>
          actionRef={actionRef}
          rowKey="tableName"
          search={{
            labelWidth: 80,
          }}
          options={false}
          request={async (params) => {
            const res = await getDbTableList(params.tableName);
            return {
              data: res.data || [],
              success: res.code === 200,
            };
          }}
          columns={columns}
          rowSelection={{
            selectedRowKeys,
            onChange: (keys) => setSelectedRowKeys(keys),
          }}
          pagination={{
            defaultPageSize: 10,
            showSizeChanger: true,
          }}
        />
      </ModalForm>
    </>
  );
};

export default ImportTableModal;
