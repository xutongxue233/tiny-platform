import {
  ModalForm,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import React, { useState } from 'react';
import { addDictType, updateDictType } from '@/services/ant-design-pro/dict';
import { PlusOutlined } from '@ant-design/icons';

interface DictTypeFormProps {
  values?: API.SysDictType;
  trigger?: React.ReactElement;
  onOk?: () => void;
}

const DictTypeForm: React.FC<DictTypeFormProps> = ({ values, trigger, onOk }) => {
  const [visible, setVisible] = useState(false);
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const isEdit = !!values?.dictId;

  const { run: addRun, loading: addLoading } = useRequest(addDictType, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.dict.addSuccess', defaultMessage: '新增成功' }),
      );
      setVisible(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.dict.addFailed', defaultMessage: '新增失败' }),
      );
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateDictType, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.dict.updateSuccess', defaultMessage: '修改成功' }),
      );
      setVisible(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.dict.updateFailed', defaultMessage: '修改失败' }),
      );
    },
  });

  const handleSubmit = async (formValues: API.SysDictTypeDTO) => {
    if (isEdit) {
      await updateRun({ ...formValues, dictId: values?.dictId });
    } else {
      await addRun(formValues);
    }
  };

  return (
    <>
      {contextHolder}
      <ModalForm<API.SysDictTypeDTO>
        title={isEdit
          ? intl.formatMessage({ id: 'pages.dict.editType', defaultMessage: '编辑字典类型' })
          : intl.formatMessage({ id: 'pages.dict.addType', defaultMessage: '新增字典类型' })
        }
        width={500}
        trigger={
          trigger || (
            <Button type="primary" icon={<PlusOutlined />}>
              {intl.formatMessage({ id: 'pages.dict.add', defaultMessage: '新增' })}
            </Button>
          )
        }
        open={visible}
        onOpenChange={setVisible}
        initialValues={values}
        modalProps={{
          destroyOnClose: true,
        }}
        submitTimeout={2000}
        onFinish={handleSubmit}
        loading={addLoading || updateLoading}
      >
        <ProFormText
          name="dictName"
          label={intl.formatMessage({ id: 'pages.dict.typeName', defaultMessage: '字典名称' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.typeNamePlaceholder', defaultMessage: '请输入字典名称' })}
          rules={[
            { required: true, message: intl.formatMessage({ id: 'pages.dict.typeNameRequired', defaultMessage: '请输入字典名称' }) },
            { max: 100, message: intl.formatMessage({ id: 'pages.dict.typeNameMax', defaultMessage: '字典名称长度不能超过100个字符' }) },
          ]}
        />
        <ProFormText
          name="dictCode"
          label={intl.formatMessage({ id: 'pages.dict.typeCode', defaultMessage: '字典编码' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.typeCodePlaceholder', defaultMessage: '请输入字典编码' })}
          rules={[
            { required: true, message: intl.formatMessage({ id: 'pages.dict.typeCodeRequired', defaultMessage: '请输入字典编码' }) },
            { max: 100, message: intl.formatMessage({ id: 'pages.dict.typeCodeMax', defaultMessage: '字典编码长度不能超过100个字符' }) },
            { pattern: /^[a-z_]+$/, message: intl.formatMessage({ id: 'pages.dict.typeCodePattern', defaultMessage: '字典编码只能包含小写字母和下划线' }) },
          ]}
          disabled={isEdit}
        />
        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.dict.status', defaultMessage: '状态' })}
          initialValue="0"
          options={[
            { label: intl.formatMessage({ id: 'pages.dict.status.normal', defaultMessage: '正常' }), value: '0' },
            { label: intl.formatMessage({ id: 'pages.dict.status.disabled', defaultMessage: '停用' }), value: '1' },
          ]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.dict.remark', defaultMessage: '备注' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.remarkPlaceholder', defaultMessage: '请输入备注' })}
          fieldProps={{ rows: 3 }}
        />
      </ModalForm>
    </>
  );
};

export default DictTypeForm;
