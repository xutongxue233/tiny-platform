import React, { useRef, useState } from 'react';
import { PageContainer } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable } from '@ant-design/pro-components';
import { Button, message, Modal, Space } from 'antd';
import { PlusOutlined, DeleteOutlined } from '@ant-design/icons';
import { list${className}, remove${className} } from '@/services/${moduleName}/${businessName}';
import type { ${className}VO, ${className}Query } from '@/services/${moduleName}/${businessName}.d';
import ${className}Form from './components/${className}Form';

const ${className}List: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [formVisible, setFormVisible] = useState(false);
  const [currentRow, setCurrentRow] = useState<${className}VO>();
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);

  const columns: ProColumns<${className}VO>[] = [
<#list listColumns as column>
    {
      title: '${column.columnComment!column.javaField}',
      dataIndex: '${column.javaField}',
<#if column.pk>
      width: 80,
      hideInSearch: true,
<#elseif column.htmlType == 'datetime'>
      width: 180,
      valueType: 'dateTime',
<#if !column.query>
      hideInSearch: true,
</#if>
<#elseif column.htmlType == 'date'>
      width: 120,
      valueType: 'date',
<#if !column.query>
      hideInSearch: true,
</#if>
<#elseif column.htmlType == 'select' || column.htmlType == 'radio'>
      width: 100,
      valueType: 'select',
<#if column.dictType?? && column.dictType != ''>
      // TODO: 配置字典 ${column.dictType}
</#if>
<#if !column.query>
      hideInSearch: true,
</#if>
<#else>
      ellipsis: true,
<#if !column.query>
      hideInSearch: true,
</#if>
</#if>
    },
</#list>
    {
      title: '操作',
      valueType: 'option',
      width: 150,
      fixed: 'right',
      render: (_, record) => (
        <Space>
          <a onClick={() => { setCurrentRow(record); setFormVisible(true); }}>编辑</a>
          <a onClick={() => handleDelete([record.${pkColumn.javaField}!])}>删除</a>
        </Space>
      ),
    },
  ];

  const handleDelete = async (ids: React.Key[]) => {
    Modal.confirm({
      title: '确认删除',
      content: '是否确认删除选中的数据?',
      onOk: async () => {
        await remove${className}(ids as ${pkColumn.javaType}[]);
        message.success('删除成功');
        actionRef.current?.reload();
        setSelectedRowKeys([]);
      },
    });
  };

  return (
    <PageContainer>
      <ProTable<${className}VO, ${className}Query>
        actionRef={actionRef}
        rowKey="${pkColumn.javaField}"
        columns={columns}
        request={async (params) => {
          const res = await list${className}(params);
          return { data: res.data, success: true };
        }}
        rowSelection={{
          selectedRowKeys,
          onChange: setSelectedRowKeys,
        }}
        toolBarRender={() => [
          <Button
            key="add"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => { setCurrentRow(undefined); setFormVisible(true); }}
          >
            新增
          </Button>,
          <Button
            key="delete"
            danger
            icon={<DeleteOutlined />}
            disabled={selectedRowKeys.length === 0}
            onClick={() => handleDelete(selectedRowKeys)}
          >
            删除
          </Button>,
        ]}
        scroll={{ x: 'max-content' }}
      />
      <${className}Form
        visible={formVisible}
        current={currentRow}
        onCancel={() => setFormVisible(false)}
        onSuccess={() => {
          setFormVisible(false);
          actionRef.current?.reload();
        }}
      />
    </PageContainer>
  );
};

export default ${className}List;
