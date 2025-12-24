import { PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProFormDigit,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
  ProFormTreeSelect,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useState } from 'react';
import { addDept, getDeptTreeSelect, getDeptTreeExclude, updateDept } from '@/services/ant-design-pro/api';
import { useDict } from '@/hooks/useDict';

interface DeptFormProps {
  trigger?: ReactElement;
  values?: API.SysDept;
  parentId?: number;
  parentName?: string;
  onOk?: () => void;
}

const DeptForm: FC<DeptFormProps> = (props) => {
  const { trigger, values, parentId, parentName, onOk } = props;
  const isEdit = !!values?.deptId;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);

  const { options: statusOptions } = useDict('sys_common_status');

  const { run: addRun, loading: addLoading } = useRequest(addDept, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.dept.addSuccess', defaultMessage: '新增成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.dept.addFailed', defaultMessage: '新增失败' }),
      );
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateDept, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.dept.updateSuccess', defaultMessage: '更新成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.dept.updateFailed', defaultMessage: '更新失败' }),
      );
    },
  });

  const onOpen = useCallback(() => {
    setOpen(true);
  }, []);

  const handleFinish = useCallback(
    async (formValues: API.SysDeptDTO) => {
      if (isEdit) {
        await updateRun({ ...formValues, deptId: values?.deptId });
      } else {
        await addRun(formValues);
      }
      return true;
    },
    [isEdit, values?.deptId, addRun, updateRun],
  );

  const triggerDom = trigger ? (
    cloneElement(trigger, { onClick: onOpen })
  ) : (
    <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
      {intl.formatMessage({ id: 'pages.dept.new', defaultMessage: '新增部门' })}
    </Button>
  );

  const convertToTreeData = (depts: API.SysDept[]): any[] => {
    return depts.map((dept) => ({
      title: dept.deptName,
      value: dept.deptId,
      children: dept.children ? convertToTreeData(dept.children) : [],
    }));
  };

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysDeptDTO>
        title={intl.formatMessage({
          id: isEdit ? 'pages.dept.editTitle' : 'pages.dept.addTitle',
          defaultMessage: isEdit ? '编辑部门' : '新增部门',
        })}
        open={open}
        onOpenChange={setOpen}
        width={600}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: addLoading || updateLoading },
        }}
        initialValues={{
          status: '0',
          sort: 0,
          parentId: parentId || 0,
          ...values,
        }}
        onFinish={handleFinish}
        grid
        rowProps={{ gutter: [16, 0] }}
      >
        <ProFormTreeSelect
          name="parentId"
          label={intl.formatMessage({ id: 'pages.dept.parentDept', defaultMessage: '上级部门' })}
          colProps={{ span: 24 }}
          fieldProps={{
            placeholder: intl.formatMessage({ id: 'pages.dept.parentDeptPlaceholder', defaultMessage: '选择上级部门' }),
            allowClear: true,
            treeDefaultExpandAll: true,
            showSearch: true,
            treeNodeFilterProp: 'title',
          }}
          request={async () => {
            let res;
            if (isEdit && values?.deptId) {
              res = await getDeptTreeExclude(values.deptId);
            } else {
              res = await getDeptTreeSelect();
            }
            const rootNode = {
              title: intl.formatMessage({ id: 'pages.dept.rootDept', defaultMessage: '主部门' }),
              value: 0,
              children: convertToTreeData(res.data || []),
            };
            return [rootNode];
          }}
        />

        <ProFormText
          name="deptName"
          label={intl.formatMessage({ id: 'pages.dept.deptName', defaultMessage: '部门名称' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({
            id: 'pages.dept.deptNamePlaceholder',
            defaultMessage: '请输入部门名称',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.dept.deptNameRequired',
                defaultMessage: '请输入部门名称',
              }),
            },
            {
              max: 50,
              message: intl.formatMessage({
                id: 'pages.dept.deptNameMaxLength',
                defaultMessage: '部门名称不能超过50个字符',
              }),
            },
          ]}
        />

        <ProFormDigit
          name="sort"
          label={intl.formatMessage({ id: 'pages.dept.sort', defaultMessage: '显示排序' })}
          colProps={{ span: 12 }}
          min={0}
          fieldProps={{ precision: 0 }}
        />

        <ProFormText
          name="leader"
          label={intl.formatMessage({ id: 'pages.dept.leader', defaultMessage: '负责人' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({
            id: 'pages.dept.leaderPlaceholder',
            defaultMessage: '请输入负责人',
          })}
          rules={[
            {
              max: 50,
              message: intl.formatMessage({
                id: 'pages.dept.leaderMaxLength',
                defaultMessage: '负责人不能超过50个字符',
              }),
            },
          ]}
        />

        <ProFormText
          name="phone"
          label={intl.formatMessage({ id: 'pages.dept.phone', defaultMessage: '联系电话' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({
            id: 'pages.dept.phonePlaceholder',
            defaultMessage: '请输入联系电话',
          })}
          rules={[
            {
              max: 20,
              message: intl.formatMessage({
                id: 'pages.dept.phoneMaxLength',
                defaultMessage: '联系电话不能超过20个字符',
              }),
            },
          ]}
        />

        <ProFormText
          name="email"
          label={intl.formatMessage({ id: 'pages.dept.email', defaultMessage: '邮箱' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({
            id: 'pages.dept.emailPlaceholder',
            defaultMessage: '请输入邮箱',
          })}
          rules={[
            {
              type: 'email',
              message: intl.formatMessage({
                id: 'pages.dept.emailInvalid',
                defaultMessage: '邮箱格式不正确',
              }),
            },
            {
              max: 100,
              message: intl.formatMessage({
                id: 'pages.dept.emailMaxLength',
                defaultMessage: '邮箱不能超过100个字符',
              }),
            },
          ]}
        />

        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.dept.status', defaultMessage: '部门状态' })}
          colProps={{ span: 12 }}
          options={statusOptions}
        />

        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.dept.remark', defaultMessage: '备注' })}
          colProps={{ span: 24 }}
          placeholder={intl.formatMessage({
            id: 'pages.dept.remarkPlaceholder',
            defaultMessage: '请输入备注',
          })}
          fieldProps={{
            rows: 3,
          }}
        />
      </ModalForm>
    </>
  );
};

export default DeptForm;
