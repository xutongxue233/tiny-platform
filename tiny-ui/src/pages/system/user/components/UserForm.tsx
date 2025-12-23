import { PlusOutlined } from '@ant-design/icons';
import {
  ModalForm,
  ProFormRadio,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { useIntl, useRequest } from '@umijs/max';
import { Button, message } from 'antd';
import type { FC, ReactElement } from 'react';
import { cloneElement, useCallback, useState } from 'react';
import { addUser, updateUser } from '@/services/ant-design-pro/api';

interface UserFormProps {
  trigger?: ReactElement;
  values?: API.SysUser;
  onOk?: () => void;
}

const UserForm: FC<UserFormProps> = (props) => {
  const { trigger, values, onOk } = props;
  const isEdit = !!values?.userId;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);

  const { run: addRun, loading: addLoading } = useRequest(addUser, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.user.addSuccess', defaultMessage: '新增成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.user.addFailed', defaultMessage: '新增失败' }),
      );
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateUser, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.user.updateSuccess', defaultMessage: '更新成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.user.updateFailed', defaultMessage: '更新失败' }),
      );
    },
  });

  const onOpen = useCallback(() => {
    setOpen(true);
  }, []);

  const handleFinish = useCallback(
    async (formValues: API.SysUserDTO) => {
      if (isEdit) {
        await updateRun({ ...formValues, userId: values?.userId });
      } else {
        await addRun(formValues);
      }
      return true;
    },
    [isEdit, values?.userId, addRun, updateRun],
  );

  const triggerDom = trigger ? (
    cloneElement(trigger, { onClick: onOpen })
  ) : (
    <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
      {intl.formatMessage({ id: 'pages.user.new', defaultMessage: '新增用户' })}
    </Button>
  );

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysUserDTO>
        title={intl.formatMessage({
          id: isEdit ? 'pages.user.editTitle' : 'pages.user.addTitle',
          defaultMessage: isEdit ? '编辑用户' : '新增用户',
        })}
        open={open}
        onOpenChange={setOpen}
        width={520}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: addLoading || updateLoading },
        }}
        initialValues={{
          gender: '0',
          status: '0',
          ...values,
        }}
        onFinish={handleFinish}
      >
        <ProFormText
          name="username"
          label={intl.formatMessage({ id: 'pages.user.username', defaultMessage: '用户名' })}
          placeholder={intl.formatMessage({
            id: 'pages.user.usernamePlaceholder',
            defaultMessage: '请输入用户名',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.user.usernameRequired',
                defaultMessage: '请输入用户名',
              }),
            },
            {
              min: 2,
              max: 20,
              message: intl.formatMessage({
                id: 'pages.user.usernameLength',
                defaultMessage: '用户名长度为2-20个字符',
              }),
            },
          ]}
          disabled={isEdit}
        />
        {!isEdit && (
          <ProFormText.Password
            name="password"
            label={intl.formatMessage({ id: 'pages.user.password', defaultMessage: '密码' })}
            placeholder={intl.formatMessage({
              id: 'pages.user.passwordPlaceholder',
              defaultMessage: '请输入密码',
            })}
            rules={[
              {
                required: true,
                message: intl.formatMessage({
                  id: 'pages.user.passwordRequired',
                  defaultMessage: '请输入密码',
                }),
              },
              {
                min: 6,
                max: 20,
                message: intl.formatMessage({
                  id: 'pages.user.passwordLength',
                  defaultMessage: '密码长度为6-20个字符',
                }),
              },
            ]}
          />
        )}
        <ProFormText
          name="realName"
          label={intl.formatMessage({ id: 'pages.user.realName', defaultMessage: '姓名' })}
          placeholder={intl.formatMessage({
            id: 'pages.user.realNamePlaceholder',
            defaultMessage: '请输入姓名',
          })}
        />
        <ProFormText
          name="phone"
          label={intl.formatMessage({ id: 'pages.user.phone', defaultMessage: '手机号' })}
          placeholder={intl.formatMessage({
            id: 'pages.user.phonePlaceholder',
            defaultMessage: '请输入手机号',
          })}
          rules={[
            {
              pattern: /^1[3-9]\d{9}$/,
              message: intl.formatMessage({
                id: 'pages.user.phoneInvalid',
                defaultMessage: '请输入正确的手机号',
              }),
            },
          ]}
        />
        <ProFormText
          name="email"
          label={intl.formatMessage({ id: 'pages.user.email', defaultMessage: '邮箱' })}
          placeholder={intl.formatMessage({
            id: 'pages.user.emailPlaceholder',
            defaultMessage: '请输入邮箱',
          })}
          rules={[
            {
              type: 'email',
              message: intl.formatMessage({
                id: 'pages.user.emailInvalid',
                defaultMessage: '请输入正确的邮箱地址',
              }),
            },
          ]}
        />
        <ProFormRadio.Group
          name="gender"
          label={intl.formatMessage({ id: 'pages.user.gender', defaultMessage: '性别' })}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.user.gender.male', defaultMessage: '男' }),
              value: '0',
            },
            {
              label: intl.formatMessage({ id: 'pages.user.gender.female', defaultMessage: '女' }),
              value: '1',
            },
            {
              label: intl.formatMessage({ id: 'pages.user.gender.unknown', defaultMessage: '未知' }),
              value: '2',
            },
          ]}
        />
        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.user.status', defaultMessage: '状态' })}
          options={[
            {
              label: intl.formatMessage({ id: 'pages.user.status.normal', defaultMessage: '正常' }),
              value: '0',
            },
            {
              label: intl.formatMessage({ id: 'pages.user.status.disabled', defaultMessage: '停用' }),
              value: '1',
            },
          ]}
        />
        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.user.remark', defaultMessage: '备注' })}
          placeholder={intl.formatMessage({
            id: 'pages.user.remarkPlaceholder',
            defaultMessage: '请输入备注',
          })}
        />
      </ModalForm>
    </>
  );
};

export default UserForm;
