import { PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProFormDigit,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormDependency,
  ProFormItem,
} from '@ant-design/pro-components';
import { useIntl } from '@umijs/max';
import { Button, Col, message, Row } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useRef, useState } from 'react';
import { addRole, getRoleDetail, updateRole } from '@/services/ant-design-pro/api';
import type { ProFormInstance } from '@ant-design/pro-components';
import MenuTreeSelect from './MenuTreeSelect';
import DeptTreeSelect from './DeptTreeSelect';
import { useDicts } from '@/hooks/useDict';

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
  const [detailLoading, setDetailLoading] = useState(false);
  const [submitLoading, setSubmitLoading] = useState(false);
  const formRef = useRef<ProFormInstance>();

  const { getOptions } = useDicts(['sys_data_scope', 'sys_common_status']);

  const dataScopeOptions = getOptions('sys_data_scope');
  const statusOptions = getOptions('sys_common_status');

  const onOpen = useCallback(async () => {
    setOpen(true);
    // 延迟设置表单值，确保表单已渲染
    setTimeout(async () => {
      if (isEdit && values?.roleId) {
        try {
          setDetailLoading(true);
          const res = await getRoleDetail(values.roleId);
          if (res.code === 200 && res.data) {
            formRef.current?.setFieldsValue({
              ...res.data,
              menuIds: res.data.menuIds || [],
              deptIds: res.data.deptIds || [],
            });
          }
        } catch (err) {
          messageApi.error('获取角色详情失败');
        } finally {
          setDetailLoading(false);
        }
      } else {
        formRef.current?.setFieldsValue({
          sort: 0,
          dataScope: '1',
          status: '0',
          menuIds: [],
          deptIds: [],
        });
      }
    }, 100);
  }, [isEdit, values?.roleId, messageApi]);

  const handleFinish = useCallback(
    async (formValues: API.SysRoleDTO) => {
      try {
        setSubmitLoading(true);
        if (isEdit) {
          const res = await updateRole({ ...formValues, roleId: values?.roleId });
          if (res.code === 200) {
            messageApi.success(intl.formatMessage({ id: 'pages.role.updateSuccess', defaultMessage: '更新成功' }));
            setOpen(false);
            onOk?.();
          }
        } else {
          const res = await addRole(formValues);
          if (res.code === 200) {
            messageApi.success(intl.formatMessage({ id: 'pages.role.addSuccess', defaultMessage: '新增成功' }));
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
    [isEdit, values?.roleId, messageApi, intl, onOk],
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
        formRef={formRef}
        title={intl.formatMessage({
          id: isEdit ? 'pages.role.editTitle' : 'pages.role.addTitle',
          defaultMessage: isEdit ? '编辑角色' : '新增角色',
        })}
        open={open}
        onOpenChange={setOpen}
        width={700}
        loading={detailLoading}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: submitLoading },
        }}
        onFinish={handleFinish}
      >
        <Row gutter={16}>
          <Col span={12}>
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
          </Col>
          <Col span={12}>
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
          </Col>
          <Col span={12}>
            <ProFormDigit
              name="sort"
              label={intl.formatMessage({ id: 'pages.role.sort', defaultMessage: '显示顺序' })}
              min={0}
              max={9999}
              fieldProps={{ precision: 0 }}
            />
          </Col>
          <Col span={12}>
            <ProFormSelect
              name="dataScope"
              label={intl.formatMessage({ id: 'pages.role.dataScope', defaultMessage: '数据范围' })}
              options={dataScopeOptions}
            />
          </Col>
          <Col span={12}>
            <ProFormRadio.Group
              name="status"
              label={intl.formatMessage({ id: 'pages.role.status', defaultMessage: '状态' })}
              options={statusOptions}
            />
          </Col>
          <Col span={24}>
            <ProFormItem
              name="menuIds"
              label={intl.formatMessage({ id: 'pages.role.menuPermission', defaultMessage: '菜单权限' })}
            >
              <MenuTreeSelect />
            </ProFormItem>
          </Col>
          <ProFormDependency name={['dataScope']}>
            {({ dataScope }) => {
              if (dataScope === '2') {
                return (
                  <Col span={24}>
                    <ProFormItem
                      name="deptIds"
                      label={intl.formatMessage({ id: 'pages.role.dataPermission', defaultMessage: '数据权限' })}
                    >
                      <DeptTreeSelect />
                    </ProFormItem>
                  </Col>
                );
              }
              return null;
            }}
          </ProFormDependency>
          <Col span={24}>
            <ProFormTextArea
              name="remark"
              label={intl.formatMessage({ id: 'pages.role.remark', defaultMessage: '备注' })}
              placeholder={intl.formatMessage({
                id: 'pages.role.remarkPlaceholder',
                defaultMessage: '请输入备注',
              })}
            />
          </Col>
        </Row>
      </ModalForm>
    </>
  );
};

export default RoleForm;
