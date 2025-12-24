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
import { addMenu, getMenuTree, updateMenu } from '@/services/ant-design-pro/api';
import IconSelect from './IconSelect';
import { useDicts } from '@/hooks/useDict';

interface MenuFormProps {
  trigger?: ReactElement;
  values?: API.SysMenu;
  parentId?: number;
  parentName?: string;
  onOk?: () => void;
}

const MenuForm: FC<MenuFormProps> = (props) => {
  const { trigger, values, parentId, parentName, onOk } = props;
  const isEdit = !!values?.menuId;

  const intl = useIntl();
  const [messageApi, contextHolder] = message.useMessage();
  const [open, setOpen] = useState(false);
  const [menuType, setMenuType] = useState<string>(values?.menuType || 'M');

  const { getOptions } = useDicts([
    'sys_menu_type',
    'sys_yes_no_01',
    'sys_cache_status',
    'sys_visible_status',
    'sys_common_status',
  ]);

  const menuTypeOptions = getOptions('sys_menu_type');
  const yesNoOptions = getOptions('sys_yes_no_01');
  const cacheStatusOptions = getOptions('sys_cache_status');
  const visibleStatusOptions = getOptions('sys_visible_status');
  const statusOptions = getOptions('sys_common_status');

  const { run: addRun, loading: addLoading } = useRequest(addMenu, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.menu.addSuccess', defaultMessage: '新增成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.menu.addFailed', defaultMessage: '新增失败' }),
      );
    },
  });

  const { run: updateRun, loading: updateLoading } = useRequest(updateMenu, {
    manual: true,
    onSuccess: () => {
      messageApi.success(
        intl.formatMessage({ id: 'pages.menu.updateSuccess', defaultMessage: '更新成功' }),
      );
      setOpen(false);
      onOk?.();
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.menu.updateFailed', defaultMessage: '更新失败' }),
      );
    },
  });

  const onOpen = useCallback(() => {
    setMenuType(values?.menuType || 'M');
    setOpen(true);
  }, [values?.menuType]);

  const handleFinish = useCallback(
    async (formValues: API.SysMenuDTO) => {
      if (isEdit) {
        await updateRun({ ...formValues, menuId: values?.menuId });
      } else {
        await addRun(formValues);
      }
      return true;
    },
    [isEdit, values?.menuId, addRun, updateRun],
  );

  const triggerDom = trigger ? (
    cloneElement(trigger, { onClick: onOpen })
  ) : (
    <Button type="primary" icon={<PlusOutlined />} onClick={onOpen}>
      {intl.formatMessage({ id: 'pages.menu.new', defaultMessage: '新增菜单' })}
    </Button>
  );

  const convertToTreeData = (menus: API.SysMenu[]): any[] => {
    return menus
      .filter((menu) => menu.menuType !== 'F')
      .map((menu) => ({
        title: menu.menuName,
        value: menu.menuId,
        children: menu.children ? convertToTreeData(menu.children) : [],
      }));
  };

  return (
    <>
      {contextHolder}
      {triggerDom}
      <ModalForm<API.SysMenuDTO>
        title={intl.formatMessage({
          id: isEdit ? 'pages.menu.editTitle' : 'pages.menu.addTitle',
          defaultMessage: isEdit ? '编辑菜单' : '新增菜单',
        })}
        open={open}
        onOpenChange={setOpen}
        width={680}
        modalProps={{
          destroyOnHidden: true,
          okButtonProps: { loading: addLoading || updateLoading },
        }}
        initialValues={{
          menuType: 'M',
          visible: '0',
          status: '0',
          isFrame: '0',
          isCache: '0',
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
          label={intl.formatMessage({ id: 'pages.menu.parentMenu', defaultMessage: '上级菜单' })}
          colProps={{ span: 24 }}
          fieldProps={{
            placeholder: intl.formatMessage({ id: 'pages.menu.parentMenuPlaceholder', defaultMessage: '选择上级菜单' }),
            allowClear: true,
            treeDefaultExpandAll: true,
            showSearch: true,
            treeNodeFilterProp: 'title',
          }}
          request={async () => {
            const res = await getMenuTree({});
            const rootNode = {
              title: intl.formatMessage({ id: 'pages.menu.rootMenu', defaultMessage: '主类目' }),
              value: 0,
              children: convertToTreeData(res.data || []),
            };
            return [rootNode];
          }}
        />

        <ProFormRadio.Group
          name="menuType"
          label={intl.formatMessage({ id: 'pages.menu.menuType', defaultMessage: '菜单类型' })}
          colProps={{ span: 24 }}
          options={menuTypeOptions}
          fieldProps={{
            onChange: (e) => setMenuType(e.target.value),
          }}
        />

        {menuType !== 'F' && (
          <IconSelect
            name="icon"
            label={intl.formatMessage({ id: 'pages.menu.icon', defaultMessage: '菜单图标' })}
            colProps={{ span: 24 }}
          />
        )}

        <ProFormText
          name="menuName"
          label={intl.formatMessage({ id: 'pages.menu.menuName', defaultMessage: '菜单名称' })}
          colProps={{ span: 12 }}
          placeholder={intl.formatMessage({
            id: 'pages.menu.menuNamePlaceholder',
            defaultMessage: '请输入菜单名称',
          })}
          rules={[
            {
              required: true,
              message: intl.formatMessage({
                id: 'pages.menu.menuNameRequired',
                defaultMessage: '请输入菜单名称',
              }),
            },
            {
              max: 50,
              message: intl.formatMessage({
                id: 'pages.menu.menuNameMaxLength',
                defaultMessage: '菜单名称不能超过50个字符',
              }),
            },
          ]}
        />

        <ProFormDigit
          name="sort"
          label={intl.formatMessage({ id: 'pages.menu.sort', defaultMessage: '显示排序' })}
          colProps={{ span: 12 }}
          min={0}
          fieldProps={{ precision: 0 }}
        />

        {menuType !== 'F' && (
          <>
            <ProFormText
              name="path"
              label={intl.formatMessage({ id: 'pages.menu.path', defaultMessage: '路由地址' })}
              colProps={{ span: 12 }}
              placeholder={intl.formatMessage({
                id: 'pages.menu.pathPlaceholder',
                defaultMessage: '请输入路由地址',
              })}
              tooltip={intl.formatMessage({
                id: 'pages.menu.pathTooltip',
                defaultMessage: '访问的路由地址，如：user',
              })}
              rules={[
                {
                  max: 200,
                  message: intl.formatMessage({
                    id: 'pages.menu.pathMaxLength',
                    defaultMessage: '路由地址不能超过200个字符',
                  }),
                },
              ]}
            />

            {menuType === 'C' && (
              <ProFormText
                name="component"
                label={intl.formatMessage({ id: 'pages.menu.component', defaultMessage: '组件路径' })}
                colProps={{ span: 12 }}
                placeholder={intl.formatMessage({
                  id: 'pages.menu.componentPlaceholder',
                  defaultMessage: '请输入组件路径',
                })}
                tooltip={intl.formatMessage({
                  id: 'pages.menu.componentTooltip',
                  defaultMessage: '访问的组件路径，如：system/user/index',
                })}
                rules={[
                  {
                    max: 255,
                    message: intl.formatMessage({
                      id: 'pages.menu.componentMaxLength',
                      defaultMessage: '组件路径不能超过255个字符',
                    }),
                  },
                ]}
              />
            )}
          </>
        )}

        {menuType === 'F' && (
          <ProFormText
            name="perms"
            label={intl.formatMessage({ id: 'pages.menu.perms', defaultMessage: '权限标识' })}
            colProps={{ span: 12 }}
            placeholder={intl.formatMessage({
              id: 'pages.menu.permsPlaceholder',
              defaultMessage: '请输入权限标识',
            })}
            tooltip={intl.formatMessage({
              id: 'pages.menu.permsTooltip',
              defaultMessage: '控制器中定义的权限字符，如：system:user:list',
            })}
            rules={[
              {
                max: 100,
                message: intl.formatMessage({
                  id: 'pages.menu.permsMaxLength',
                  defaultMessage: '权限标识不能超过100个字符',
                }),
              },
            ]}
          />
        )}

        {menuType !== 'F' && (
          <>
            <ProFormRadio.Group
              name="isFrame"
              label={intl.formatMessage({ id: 'pages.menu.isFrame', defaultMessage: '是否外链' })}
              colProps={{ span: 12 }}
              options={yesNoOptions}
            />

            <ProFormRadio.Group
              name="isCache"
              label={intl.formatMessage({ id: 'pages.menu.isCache', defaultMessage: '是否缓存' })}
              colProps={{ span: 12 }}
              tooltip={intl.formatMessage({
                id: 'pages.menu.isCacheTooltip',
                defaultMessage: '选择缓存则会被keep-alive缓存',
              })}
              options={cacheStatusOptions}
            />
          </>
        )}

        <ProFormRadio.Group
          name="visible"
          label={intl.formatMessage({ id: 'pages.menu.visible', defaultMessage: '显示状态' })}
          colProps={{ span: 12 }}
          tooltip={intl.formatMessage({
            id: 'pages.menu.visibleTooltip',
            defaultMessage: '选择隐藏则路由将不会出现在侧边栏',
          })}
          options={visibleStatusOptions}
        />

        <ProFormRadio.Group
          name="status"
          label={intl.formatMessage({ id: 'pages.menu.status', defaultMessage: '菜单状态' })}
          colProps={{ span: 12 }}
          options={statusOptions}
        />

        <ProFormTextArea
          name="remark"
          label={intl.formatMessage({ id: 'pages.menu.remark', defaultMessage: '备注' })}
          colProps={{ span: 24 }}
          placeholder={intl.formatMessage({
            id: 'pages.menu.remarkPlaceholder',
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

export default MenuForm;
