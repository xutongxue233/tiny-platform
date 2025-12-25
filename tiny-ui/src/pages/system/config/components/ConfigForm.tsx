import { PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Button, Col, message, Row } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useRef, useState } from 'react';
import { addConfig, updateConfig } from '@/services/ant-design-pro/config';
import type { ProFormInstance } from '@ant-design/pro-components';
import { useDicts } from '@/hooks/useDict';

interface ConfigFormProps {
  trigger?: ReactElement;
  values?: API.SysConfig;
  onOk?: () => void;
}

const ConfigForm: FC<ConfigFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.configId;

  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);
  const [submitLoading, setSubmitLoading] = useState(false);
  const formRef = useRef<ProFormInstance>();

  const { getOptions } = useDicts(['sys_config_group', 'sys_config_type', 'sys_common_status']);

  const configGroupOptions = getOptions('sys_config_group');
  const configTypeOptions = getOptions('sys_config_type');
  const statusOptions = getOptions('sys_common_status');

  const onOpen = useCallback(async () => {
    setOpen(true);
    setTimeout(() => {
      if (isEdit && values) {
        formRef.current?.setFieldsValue({
          ...values,
        });
      } else {
        formRef.current?.setFieldsValue({
          configType: 'STRING',
          configGroup: 'SYSTEM',
          isBuiltin: 'N',
          status: '0',
        });
      }
    }, 100);
  }, [isEdit, values]);

  const handleFinish = useCallback(
    async (formValues: API.SysConfigDTO) => {
      try {
        setSubmitLoading(true);
        if (isEdit) {
          const res = await updateConfig({ ...formValues, configId: values?.configId });
          if (res.code === 200) {
            messageApi.success('更新成功');
            setOpen(false);
            onOk?.();
          }
        } else {
          const res = await addConfig(formValues);
          if (res.code === 200) {
            messageApi.success('新增成功');
            setOpen(false);
            onOk?.();
          }
        }
      } catch (err) {
        messageApi.error(isEdit ? '更新失败' : '新增失败');
      } finally {
        setSubmitLoading(false);
      }
      return true;
    },
    [isEdit, values?.configId, messageApi, onOk],
  );

  const triggerDom = trigger ? (
    cloneElement(trigger, { onClick: onOpen })
  ) : (
    <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
      新增参数
    </Button>
  );

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysConfigDTO>
        formRef={formRef}
        title={isEdit ? '编辑参数配置' : '新增参数配置'}
        open={open}
        onOpenChange={setOpen}
        width={600}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: submitLoading },
        }}
        onFinish={handleFinish}
      >
        <Row gutter={16}>
          <Col span={12}>
            <ProFormText
              name="configName"
              label="参数名称"
              placeholder="请输入参数名称"
              rules={[
                { required: true, message: '请输入参数名称' },
                { max: 100, message: '参数名称不能超过100个字符' },
              ]}
            />
          </Col>
          <Col span={12}>
            <ProFormText
              name="configKey"
              label="参数键名"
              placeholder="请输入参数键名"
              tooltip="参数的唯一标识，如：sys.app.name"
              disabled={isEdit}
              rules={[
                { required: true, message: '请输入参数键名' },
                { max: 100, message: '参数键名不能超过100个字符' },
                {
                  pattern: /^[a-zA-Z][a-zA-Z0-9._-]*$/,
                  message: '参数键名只能包含字母、数字、点、下划线和横杠，且以字母开头',
                },
              ]}
            />
          </Col>
          <Col span={24}>
            <ProFormTextArea
              name="configValue"
              label="参数值"
              placeholder="请输入参数值"
              fieldProps={{
                rows: 3,
              }}
            />
          </Col>
          <Col span={12}>
            <ProFormSelect
              name="configType"
              label="参数类型"
              options={configTypeOptions}
              placeholder="请选择参数类型"
            />
          </Col>
          <Col span={12}>
            <ProFormSelect
              name="configGroup"
              label="参数分组"
              options={configGroupOptions}
              placeholder="请选择参数分组"
            />
          </Col>
          <Col span={12}>
            <ProFormRadio.Group
              name="status"
              label="状态"
              options={statusOptions}
            />
          </Col>
          <Col span={12}>
            <ProFormRadio.Group
              name="isBuiltin"
              label="内置参数"
              disabled={isEdit && values?.isBuiltin === 'Y'}
              options={[
                { label: '是', value: 'Y' },
                { label: '否', value: 'N' },
              ]}
              tooltip="内置参数不允许删除"
            />
          </Col>
          <Col span={24}>
            <ProFormTextArea
              name="remark"
              label="备注"
              placeholder="请输入备注"
              fieldProps={{
                rows: 2,
              }}
            />
          </Col>
        </Row>
      </ModalForm>
    </>
  );
};

export default ConfigForm;
