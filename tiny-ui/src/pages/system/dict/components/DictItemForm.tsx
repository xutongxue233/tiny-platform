import {
  ModalForm,
  ProFormDigit,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import React, { useState } from 'react';
import { addDictItem, updateDictItem } from '@/services/ant-design-pro/dict';
import { PlusOutlined } from '@ant-design/icons';

interface DictItemFormProps {
  dictCode: string;
  values?: API.SysDictItem;
  trigger?: React.ReactElement;
  onOk?: () => void;
}

const DictItemForm: React.FC<DictItemFormProps> = ({ dictCode, values, trigger, onOk }) => {
  const [visible, setVisible] = useState(false);
  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();

  const isEdit = !!values?.itemId;

  const { run: addRun, loading: addLoading } = useRequest(addDictItem, {
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

  const { run: updateRun, loading: updateLoading } = useRequest(updateDictItem, {
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

  const handleSubmit = async (formValues: API.SysDictItemDTO) => {
    if (isEdit) {
      await updateRun({ ...formValues, itemId: values?.itemId, dictCode });
    } else {
      await addRun({ ...formValues, dictCode });
    }
  };

  return (
    <>
      {contextHolder}
      <ModalForm<API.SysDictItemDTO>
        title={isEdit
          ? intl.formatMessage({ id: 'pages.dict.editItem', defaultMessage: '编辑字典项' })
          : intl.formatMessage({ id: 'pages.dict.addItem', defaultMessage: '新增字典项' })
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
          name="itemLabel"
          label={intl.formatMessage({ id: 'pages.dict.itemLabel', defaultMessage: '字典标签' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.itemLabelPlaceholder', defaultMessage: '请输入字典标签' })}
          rules={[
            { required: true, message: intl.formatMessage({ id: 'pages.dict.itemLabelRequired', defaultMessage: '请输入字典标签' }) },
            { max: 100, message: intl.formatMessage({ id: 'pages.dict.itemLabelMax', defaultMessage: '字典标签长度不能超过100个字符' }) },
          ]}
        />
        <ProFormText
          name="itemValue"
          label={intl.formatMessage({ id: 'pages.dict.itemValue', defaultMessage: '字典值' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.itemValuePlaceholder', defaultMessage: '请输入字典值' })}
          rules={[
            { required: true, message: intl.formatMessage({ id: 'pages.dict.itemValueRequired', defaultMessage: '请输入字典值' }) },
            { max: 100, message: intl.formatMessage({ id: 'pages.dict.itemValueMax', defaultMessage: '字典值长度不能超过100个字符' }) },
          ]}
        />
        <ProFormDigit
          name="itemSort"
          label={intl.formatMessage({ id: 'pages.dict.itemSort', defaultMessage: '排序' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.itemSortPlaceholder', defaultMessage: '请输入排序' })}
          initialValue={0}
          min={0}
          max={9999}
          fieldProps={{ precision: 0 }}
        />
        <ProFormText
          name="cssClass"
          label={intl.formatMessage({ id: 'pages.dict.cssClass', defaultMessage: '样式属性' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.cssClassPlaceholder', defaultMessage: '请输入样式属性' })}
        />
        <ProFormText
          name="listClass"
          label={intl.formatMessage({ id: 'pages.dict.listClass', defaultMessage: '表格回显样式' })}
          placeholder={intl.formatMessage({ id: 'pages.dict.listClassPlaceholder', defaultMessage: '请输入表格回显样式' })}
        />
        <ProFormRadio.Group
          name="isDefault"
          label={intl.formatMessage({ id: 'pages.dict.isDefault', defaultMessage: '是否默认' })}
          initialValue="N"
          options={[
            { label: intl.formatMessage({ id: 'pages.dict.yes', defaultMessage: '是' }), value: 'Y' },
            { label: intl.formatMessage({ id: 'pages.dict.no', defaultMessage: '否' }), value: 'N' },
          ]}
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

export default DictItemForm;
