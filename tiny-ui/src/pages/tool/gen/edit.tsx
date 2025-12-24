import {
  PageContainer,
  ProCard,
  ProForm,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { history, useParams } from '@umijs/max';
import { Button, message, Space, Spin, Table, Input, Select, Checkbox } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import React, { useEffect, useState } from 'react';
import {
  getGenTableDetail,
  updateGenTable,
  type GenTable,
  type GenTableColumn,
} from '@/services/ant-design-pro/generator';

const HTML_TYPE_OPTIONS = [
  { label: '文本框', value: 'input' },
  { label: '文本域', value: 'textarea' },
  { label: '下拉框', value: 'select' },
  { label: '单选框', value: 'radio' },
  { label: '复选框', value: 'checkbox' },
  { label: '日期控件', value: 'datetime' },
  { label: '图片上传', value: 'imageUpload' },
  { label: '文件上传', value: 'fileUpload' },
  { label: '富文本控件', value: 'editor' },
];

const QUERY_TYPE_OPTIONS = [
  { label: '=', value: 'EQ' },
  { label: '!=', value: 'NE' },
  { label: '>', value: 'GT' },
  { label: '>=', value: 'GE' },
  { label: '<', value: 'LT' },
  { label: '<=', value: 'LE' },
  { label: 'LIKE', value: 'LIKE' },
  { label: 'BETWEEN', value: 'BETWEEN' },
];

const JAVA_TYPE_OPTIONS = [
  { label: 'Long', value: 'Long' },
  { label: 'String', value: 'String' },
  { label: 'Integer', value: 'Integer' },
  { label: 'Double', value: 'Double' },
  { label: 'BigDecimal', value: 'BigDecimal' },
  { label: 'LocalDateTime', value: 'LocalDateTime' },
  { label: 'LocalDate', value: 'LocalDate' },
  { label: 'Boolean', value: 'Boolean' },
];

const GenTableEdit: React.FC = () => {
  const { tableId } = useParams<{ tableId: string }>();
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [tableData, setTableData] = useState<GenTable | null>(null);
  const [columns, setColumns] = useState<GenTableColumn[]>([]);
  const [messageApi, contextHolder] = message.useMessage();

  useEffect(() => {
    if (tableId) {
      loadTableData(parseInt(tableId, 10));
    }
  }, [tableId]);

  const loadTableData = async (id: number) => {
    setLoading(true);
    try {
      const res = await getGenTableDetail(id);
      if (res.code === 200 && res.data) {
        setTableData(res.data);
        setColumns(res.data.columns || []);
      } else {
        messageApi.error('获取表信息失败');
      }
    } catch (error) {
      messageApi.error('获取表信息失败');
    } finally {
      setLoading(false);
    }
  };

  const handleColumnChange = (
    index: number,
    field: keyof GenTableColumn,
    value: any,
  ) => {
    const newColumns = [...columns];
    newColumns[index] = { ...newColumns[index], [field]: value };
    setColumns(newColumns);
  };

  const handleSubmit = async (values: GenTable) => {
    setSaving(true);
    try {
      const data: GenTable = {
        ...tableData,
        ...values,
        columns,
      };
      await updateGenTable(data);
      messageApi.success('保存成功');
      history.push('/tool/gen');
    } catch (error) {
      messageApi.error('保存失败');
    } finally {
      setSaving(false);
    }
  };

  const tableColumns: ColumnsType<GenTableColumn> = [
    {
      title: '序号',
      dataIndex: 'sortOrder',
      width: 60,
      render: (_, __, index) => index + 1,
    },
    {
      title: '字段名称',
      dataIndex: 'columnName',
      width: 120,
    },
    {
      title: '字段描述',
      dataIndex: 'columnComment',
      width: 140,
      render: (text, record, index) => (
        <Input
          value={text}
          onChange={(e) => handleColumnChange(index, 'columnComment', e.target.value)}
          size="small"
        />
      ),
    },
    {
      title: '物理类型',
      dataIndex: 'columnType',
      width: 100,
    },
    {
      title: 'Java类型',
      dataIndex: 'javaType',
      width: 120,
      render: (text, record, index) => (
        <Select
          value={text}
          onChange={(value) => handleColumnChange(index, 'javaType', value)}
          options={JAVA_TYPE_OPTIONS}
          size="small"
          style={{ width: '100%' }}
        />
      ),
    },
    {
      title: 'Java属性',
      dataIndex: 'javaField',
      width: 120,
      render: (text, record, index) => (
        <Input
          value={text}
          onChange={(e) => handleColumnChange(index, 'javaField', e.target.value)}
          size="small"
        />
      ),
    },
    {
      title: '插入',
      dataIndex: 'isInsert',
      width: 50,
      align: 'center',
      render: (text, record, index) => (
        <Checkbox
          checked={text === '1'}
          onChange={(e) =>
            handleColumnChange(index, 'isInsert', e.target.checked ? '1' : '0')
          }
        />
      ),
    },
    {
      title: '编辑',
      dataIndex: 'isEdit',
      width: 50,
      align: 'center',
      render: (text, record, index) => (
        <Checkbox
          checked={text === '1'}
          onChange={(e) =>
            handleColumnChange(index, 'isEdit', e.target.checked ? '1' : '0')
          }
        />
      ),
    },
    {
      title: '列表',
      dataIndex: 'isList',
      width: 50,
      align: 'center',
      render: (text, record, index) => (
        <Checkbox
          checked={text === '1'}
          onChange={(e) =>
            handleColumnChange(index, 'isList', e.target.checked ? '1' : '0')
          }
        />
      ),
    },
    {
      title: '查询',
      dataIndex: 'isQuery',
      width: 50,
      align: 'center',
      render: (text, record, index) => (
        <Checkbox
          checked={text === '1'}
          onChange={(e) =>
            handleColumnChange(index, 'isQuery', e.target.checked ? '1' : '0')
          }
        />
      ),
    },
    {
      title: '详情',
      dataIndex: 'isDetail',
      width: 50,
      align: 'center',
      render: (text, record, index) => (
        <Checkbox
          checked={text === '1'}
          onChange={(e) =>
            handleColumnChange(index, 'isDetail', e.target.checked ? '1' : '0')
          }
        />
      ),
    },
    {
      title: '查询方式',
      dataIndex: 'queryType',
      width: 100,
      render: (text, record, index) => (
        <Select
          value={text}
          onChange={(value) => handleColumnChange(index, 'queryType', value)}
          options={QUERY_TYPE_OPTIONS}
          size="small"
          style={{ width: '100%' }}
          allowClear
        />
      ),
    },
    {
      title: '显示类型',
      dataIndex: 'htmlType',
      width: 110,
      render: (text, record, index) => (
        <Select
          value={text}
          onChange={(value) => handleColumnChange(index, 'htmlType', value)}
          options={HTML_TYPE_OPTIONS}
          size="small"
          style={{ width: '100%' }}
        />
      ),
    },
    {
      title: '字典类型',
      dataIndex: 'dictType',
      width: 120,
      render: (text, record, index) => (
        <Input
          value={text}
          onChange={(e) => handleColumnChange(index, 'dictType', e.target.value)}
          size="small"
          placeholder="字典类型"
        />
      ),
    },
  ];

  return (
    <PageContainer
      title="编辑表配置"
      extra={
        <Space>
          <Button onClick={() => history.push('/tool/gen')}>返回</Button>
        </Space>
      }
    >
      {contextHolder}
      <Spin spinning={loading}>
        {tableData && (
          <ProForm<GenTable>
            initialValues={tableData}
            onFinish={handleSubmit}
            submitter={{
              render: (_, dom) => (
                <div style={{ textAlign: 'center', marginTop: 16 }}>
                  <Space>{dom}</Space>
                </div>
              ),
              submitButtonProps: {
                loading: saving,
              },
              searchConfig: {
                submitText: '保存',
              },
            }}
          >
            <ProCard title="基本信息" collapsible bordered style={{ marginBottom: 16 }}>
              <ProForm.Group>
                <ProFormText
                  name="tableName"
                  label="表名称"
                  width="md"
                  disabled
                />
                <ProFormText
                  name="tableComment"
                  label="表描述"
                  width="md"
                  rules={[{ required: true, message: '请输入表描述' }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  name="className"
                  label="实体类名"
                  width="md"
                  rules={[{ required: true, message: '请输入实体类名' }]}
                />
                <ProFormText
                  name="functionName"
                  label="功能名称"
                  width="md"
                  rules={[{ required: true, message: '请输入功能名称' }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  name="author"
                  label="作者"
                  width="md"
                  rules={[{ required: true, message: '请输入作者' }]}
                />
                <ProFormText
                  name="packageName"
                  label="包路径"
                  width="md"
                  rules={[{ required: true, message: '请输入包路径' }]}
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormText
                  name="moduleName"
                  label="模块名"
                  width="md"
                  rules={[{ required: true, message: '请输入模块名' }]}
                />
                <ProFormText
                  name="businessName"
                  label="业务名"
                  width="md"
                  rules={[{ required: true, message: '请输入业务名' }]}
                />
              </ProForm.Group>
            </ProCard>

            <ProCard title="生成信息" collapsible bordered style={{ marginBottom: 16 }}>
              <ProForm.Group>
                <ProFormSelect
                  name="genType"
                  label="生成方式"
                  width="md"
                  options={[
                    { label: 'zip压缩包', value: '0' },
                    { label: '自定义路径', value: '1' },
                  ]}
                />
                <ProFormText
                  name="genPath"
                  label="生成路径"
                  width="md"
                  tooltip="生成方式为自定义路径时有效"
                />
              </ProForm.Group>
              <ProForm.Group>
                <ProFormSelect
                  name="feGenerateType"
                  label="前端生成"
                  width="md"
                  options={[
                    { label: '生成前端代码', value: '1' },
                    { label: '不生成前端代码', value: '0' },
                  ]}
                />
                <ProFormText
                  name="feModulePath"
                  label="前端模块路径"
                  width="md"
                  tooltip="前端代码生成的模块路径"
                />
              </ProForm.Group>
              <ProFormTextArea
                name="remark"
                label="备注"
                fieldProps={{ rows: 3 }}
              />
            </ProCard>

            <ProCard title="字段信息" collapsible bordered>
              <Table
                columns={tableColumns}
                dataSource={columns}
                rowKey="columnId"
                size="small"
                scroll={{ x: 1400 }}
                pagination={false}
              />
            </ProCard>
          </ProForm>
        )}
      </Spin>
    </PageContainer>
  );
};

export default GenTableEdit;
