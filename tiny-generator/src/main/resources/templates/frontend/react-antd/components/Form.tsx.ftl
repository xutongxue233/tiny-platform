import React, { useEffect } from 'react';
import { Modal, Form, message } from 'antd';
<#assign formItems = []>
<#list insertColumns as col>
<#if col.htmlType == 'input'>
<#assign formItems = formItems + ['Input']>
<#elseif col.htmlType == 'textarea'>
<#assign formItems = formItems + ['Input']>
<#elseif col.htmlType == 'number'>
<#assign formItems = formItems + ['InputNumber']>
<#elseif col.htmlType == 'select'>
<#assign formItems = formItems + ['Select']>
<#elseif col.htmlType == 'radio'>
<#assign formItems = formItems + ['Radio']>
<#elseif col.htmlType == 'checkbox'>
<#assign formItems = formItems + ['Checkbox']>
<#elseif col.htmlType == 'datetime' || col.htmlType == 'date'>
<#assign formItems = formItems + ['DatePicker']>
</#if>
</#list>
import { ${formItems?join(', ')} } from 'antd';
import { add${className}, update${className} } from '@/services/${moduleName}/${businessName}';
import type { ${className}VO, ${className}DTO } from '@/services/${moduleName}/${businessName}.d';

interface ${className}FormProps {
  visible: boolean;
  current?: ${className}VO;
  onCancel: () => void;
  onSuccess: () => void;
}

const ${className}Form: React.FC<${className}FormProps> = ({
  visible,
  current,
  onCancel,
  onSuccess,
}) => {
  const [form] = Form.useForm();
  const isEdit = !!current?.${pkColumn.javaField};

  useEffect(() => {
    if (visible) {
      if (current) {
        form.setFieldsValue(current);
      } else {
        form.resetFields();
      }
    }
  }, [visible, current, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    const data: ${className}DTO = {
      ...values,
<#if pkColumn??>
      ${pkColumn.javaField}: current?.${pkColumn.javaField},
</#if>
    };

    if (isEdit) {
      await update${className}(data);
      message.success('修改成功');
    } else {
      await add${className}(data);
      message.success('新增成功');
    }
    onSuccess();
  };

  return (
    <Modal
      title={isEdit ? '编辑${functionName!tableComment}' : '新增${functionName!tableComment}'}
      open={visible}
      onOk={handleSubmit}
      onCancel={onCancel}
      destroyOnClose
    >
      <Form
        form={form}
        layout="vertical"
      >
<#list insertColumns as column>
<#if !column.pk>
        <Form.Item
          name="${column.javaField}"
          label="${column.columnComment!column.javaField}"
<#if column.required>
          rules={[{ required: true, message: '请输入${column.columnComment!column.javaField}' }]}
</#if>
        >
<#if column.htmlType == 'input'>
          <Input placeholder="请输入${column.columnComment!column.javaField}" />
<#elseif column.htmlType == 'textarea'>
          <Input.TextArea placeholder="请输入${column.columnComment!column.javaField}" rows={4} />
<#elseif column.htmlType == 'number'>
          <InputNumber placeholder="请输入${column.columnComment!column.javaField}" style={{ width: '100%' }} />
<#elseif column.htmlType == 'select'>
          <Select placeholder="请选择${column.columnComment!column.javaField}">
            {/* TODO: 配置选项 */}
          </Select>
<#elseif column.htmlType == 'radio'>
          <Radio.Group>
            {/* TODO: 配置选项 */}
          </Radio.Group>
<#elseif column.htmlType == 'checkbox'>
          <Checkbox.Group>
            {/* TODO: 配置选项 */}
          </Checkbox.Group>
<#elseif column.htmlType == 'datetime'>
          <DatePicker showTime style={{ width: '100%' }} />
<#elseif column.htmlType == 'date'>
          <DatePicker style={{ width: '100%' }} />
<#else>
          <Input placeholder="请输入${column.columnComment!column.javaField}" />
</#if>
        </Form.Item>
</#if>
</#list>
      </Form>
    </Modal>
  );
};

export default ${className}Form;
