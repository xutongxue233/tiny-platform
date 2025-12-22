import { PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProFormDigit,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, Form, message } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useState } from 'react';
import { addRole, getRoleDetail, updateRole } from '@/services/ant-design-pro/api';
import MenuTreeSelect from './MenuTreeSelect';

interface RoleFormProps {
  trigger?: ReactElement;
  values?: API.SysRole;
  onOk?: () => void;
}

const RoleForm: FC<RoleFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.roleId;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);
  const [initialValues, setInitialValues] = useState<API.SysRole>({});

  const { run: fetchDetail, loading: detailLoading } = useRequest(
    async (roleId: number) => {
      const res = await getRoleDetail(roleId);
      return res.data;
    },
    {
      manual: true,
      onSuccess: (data) => {
        if (data) {
          setInitialValues({
            ...data,
            menuIds: data.menuIds || [],
          });
        }
      },
    },
  );

  const { run: addRun, loading: addLoading } = useRequest(addRole, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.role.addSuccess', defaultMessage: '新增成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.role.addFailed', defaultMessage: '新增失败' }),
      );
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateRole, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.role.updateSuccess', defaultMessage: '更新成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.role.updateFailed', defaultMessage: '更新失败' }),
      );
    },
  });

  const onOpen = useCallback(async () => {
    if (isEdit && values?.roleId) {
      await fetchDetail(values.roleId);
    } else {
      setInitialValues({
        sort: 0,
        dataScope: '1',
        status: '0',
        menuIds: [],
      });
    }
    setOpen(true);
  }, [isEdit, values?.roleId, fetchDetail]);

  const handleFinish = useCallback(
    async (formValues: API.SysRoleDTO) => {
      if (isEdit) {
        await updateRun({ ...formValues, roleId: values?.roleId });
      } else {
        await addRun(formValues);
      }
      return true;
    },
    [isEdit, values?.roleId, addRun, updateRun],
  );

  const triggerDom = trigger ? (
    cloneElement(trigger, { onClick: onOpen })
  ) : (
    <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
      {intl.formatMessage({ id: 'pages.role.new', defaultMessage: '新增角色' })}
    </Button>
  );

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysRoleDTO>
        title={intl.formatMessage({
          id: isEdit ? 'pages.role.editTitle' : 'pages.role.addTitle',
          defaultMessage: isEdit ? '编辑角色' : '新增角色',
        })}
        open={open}
        onOpenChange={setOpen}
        width={600}
        loading={detailLoading}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: addLoading || updateLoading },
        }}
        initialValues={initialValues}
        onFinish={handleFinish}
      >
        <ProFormText
          name="roleName"
          label={intl.formatMessage({ id: 'pages.role.roleName', defaultMessage: '角色名称' })}
          placeholder={intl.formatMessage({
            id: 'pages.role.roleNamePlaceholder',
            defaultMessage: '请输入角色名称',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.role.roleNameRequired',
                defaultMessage: '请输入角色名称',
              }),
            },
            {
              max: 30,
              message: intl.formatMessage({
                id: 'pages.role.roleNameMaxLength',
                defaultMessage: '角色名称不能超过30个字符',
              }),
            },
          ]}
        />
        <ProFormText
          name="roleKey"
          label={intl.formatMessage({ id: 'pages.role.roleKey', defaultMessage: '角色标识' })}
          placeholder={intl.formatMessage({
            id: 'pages.role.roleKeyPlaceholder',
            defaultMessage: '请输入角色标识',
          })}
          tooltip={intl.formatMessage({
            id: 'pages.role.roleKeyTooltip',
            defaultMessage: '角色唯一标识，如：admin、user',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.role.roleKeyRequired',
                defaultMessage: '请输入角色标识',
              }),
            },
            {
              max: 100,
              message: intl.formatMessage({
                id: 'pages.role.roleKeyMaxLength',
                defaultMessage: '角色标识不能超过100个字符',
              }),
            },
            {
              pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/,
              message: intl.formatMessage({
                id: 'pages.role.roleKeyPattern',
                defaultMessage: '角色标识只能包含字母、数字和下划线，且以字母开头',
              }),
            },
          ]}
        />
        <ProFormDigit
          name="sort"
          label={intl.formatMessage({ id: 'pages.role.sort', defaultMessage: '显示顺序' })}
          min={0}
          max={9999}
          fieldProps={{ precision: 0 }}
        />
        <ProFormSelect
          name="dataScope"
          label={intl.formatMessage({ id: 'pages.role.dataScope', defaultMessage: '数据范围' })}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.role.dataScope.all', defaultMessage: '全部数据' }),
              value: '1',
            },
            {
              label: intl.formatMessage({ id: 'pages.role.dataScope.custom', defaultMessage: '自定义数据' }),
              value: '2',
            },
            {
              label: intl.formatMessage({ id: 'pages.role.dataScope.dept', defaultMessage: '本部门数据' }),
              value: '3',
            },
            {
              label: intl.formatMessage({ id: 'pages.role.dataScope.deptAndBelow', defaultMessage: '本部门及以下数据' }),
              value: '4',
            },
            {
              label: intl.formatMessage({ id: 'pages.role.dataScope.self', defaultMessage: '仅本人数据' }),
              value: '5',
            },
          ]}
        />
        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.role.status', defaultMessage: '状态' })}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.role.status.normal', defaultMessage: '正常' }),
              value: '0',
            },
            {
              label: intl.formatMessage({ id: 'pages.role.status.disabled', defaultMessage: '停用' }),
              value: '1',
            },
          ]}
        />
        <Form.Item
          name="menuIds"
          label={intl.formatMessage({ id: 'pages.role.menuPermission', defaultMessage: '菜单权限' })}
        >
          <MenuTreeSelect />
        </Form.Item>
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.role.remark', defaultMessage: '备注' })}
          placeholder={intl.formatMessage({
            id: 'pages.role.remarkPlaceholder',
            defaultMessage: '请输入备注',
          })}
        />
      </ModalForm>
    </>
  );
};

export default RoleForm;
