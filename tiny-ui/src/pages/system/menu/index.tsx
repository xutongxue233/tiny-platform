import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { useAccess, useIntl, useRequest } from '@umijs/max';
import { Button, message, Popconfirm, Space, Switch, Tag } from 'antd';
import React, { useCallback, useRef, useState } from 'react';
import { deleteMenu, getMenuTree } from '@/services/ant-design-pro/api';
import MenuForm from './components/MenuForm';
import * as icons from '@ant-design/icons';
import { Icon } from '@iconify/react';

const MenuManagement: React.FC = () => {
  const actionRef = useRef<ActionType | null>(null);
  const [expandedRowKeys, setExpandedRowKeys] = useState<React.Key[]>([]);
  const [menuData, setMenuData] = useState<API.SysMenu[]>([]);

  const intl = useIntl();
  const access = useAccess();
  const [messageApi, contextHolder] = message.useMessage();

  const { run: delRun, loading: delLoading } = useRequest(deleteMenu, {
    manual: true,
    onSuccess: () => {
      actionRef.current?.reloadAndRest?.();
      messageApi.success(
        intl.formatMessage({ id: 'pages.menu.deleteSuccess', defaultMessage: '删除成功' }),
      );
    },
    onError: () => {
      messageApi.error(
        intl.formatMessage({ id: 'pages.menu.deleteFailed', defaultMessage: '删除失败' }),
      );
    },
  });

  const handleExpandAll = useCallback((data: API.SysMenu[]) => {
    const keys: React.Key[] = [];
    const collectKeys = (menus: API.SysMenu[]) => {
      menus.forEach((menu) => {
        if (menu.menuId) {
          keys.push(menu.menuId);
        }
        if (menu.children && menu.children.length > 0) {
          collectKeys(menu.children);
        }
      });
    };
    collectKeys(data);
    setExpandedRowKeys(keys);
  }, []);

  const handleCollapseAll = useCallback(() => {
    setExpandedRowKeys([]);
  }, []);

  const renderIcon = (iconName?: string) => {
    if (!iconName) return null;
    // 支持 Iconify 格式 (prefix:icon-name)
    if (iconName.includes(':')) {
      return <Icon icon={iconName} style={{ marginRight: 4, fontSize: 16 }} />;
    }
    // 兼容旧的 Ant Design 图标格式
    const IconComponent = (icons as any)[iconName];
    if (IconComponent) {
      return <IconComponent style={{ marginRight: 4 }} />;
    }
    return null;
  };

  const columns: ProColumns<API.SysMenu>[] = [
    {
      title: intl.formatMessage({ id: 'pages.menu.menuName', defaultMessage: '菜单名称' }),
      dataIndex: 'menuName',
      width: 200,
      render: (_, record) => (
        <Space>
          {renderIcon(record.icon)}
          <span>{record.menuName}</span>
        </Space>
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.icon', defaultMessage: '图标' }),
      dataIndex: 'icon',
      width: 80,
      hideInSearch: true,
      render: (_, record) => renderIcon(record.icon) || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.sort', defaultMessage: '排序' }),
      dataIndex: 'sort',
      width: 80,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.perms', defaultMessage: '权限标识' }),
      dataIndex: 'perms',
      width: 180,
      hideInSearch: true,
      render: (_, record) => record.perms || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.component', defaultMessage: '组件路径' }),
      dataIndex: 'component',
      width: 200,
      hideInSearch: true,
      ellipsis: true,
      render: (_, record) => record.component || '-',
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.menuType', defaultMessage: '类型' }),
      dataIndex: 'menuType',
      width: 80,
      hideInSearch: true,
      render: (_, record) => {
        const typeMap: Record<string, { text: string; color: string }> = {
          M: {
            text: intl.formatMessage({ id: 'pages.menu.menuType.directory', defaultMessage: '目录' }),
            color: 'blue',
          },
          C: {
            text: intl.formatMessage({ id: 'pages.menu.menuType.menu', defaultMessage: '菜单' }),
            color: 'green',
          },
          F: {
            text: intl.formatMessage({ id: 'pages.menu.menuType.button', defaultMessage: '按钮' }),
            color: 'orange',
          },
        };
        const type = typeMap[record.menuType || ''];
        return type ? <Tag color={type.color}>{type.text}</Tag> : '-';
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.visible', defaultMessage: '可见' }),
      dataIndex: 'visible',
      width: 80,
      hideInSearch: true,
      render: (_, record) => (
        <Switch
          checked={record.visible === '0'}
          checkedChildren={intl.formatMessage({ id: 'pages.menu.visible.show', defaultMessage: '显示' })}
          unCheckedChildren={intl.formatMessage({ id: 'pages.menu.visible.hide', defaultMessage: '隐藏' })}
          disabled
          size="small"
        />
      ),
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.status', defaultMessage: '状态' }),
      dataIndex: 'status',
      width: 80,
      valueEnum: {
        '0': {
          text: intl.formatMessage({ id: 'pages.menu.status.normal', defaultMessage: '正常' }),
          status: 'Success',
        },
        '1': {
          text: intl.formatMessage({ id: 'pages.menu.status.disabled', defaultMessage: '停用' }),
          status: 'Error',
        },
      },
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.createTime', defaultMessage: '创建时间' }),
      dataIndex: 'createTime',
      valueType: 'dateTime',
      width: 180,
      hideInSearch: true,
    },
    {
      title: intl.formatMessage({ id: 'pages.menu.option', defaultMessage: '操作' }),
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      render: (_, record) => [
        access.hasPermission('system:menu:edit') && (
          <MenuForm
            key="edit"
            trigger={<a>{intl.formatMessage({ id: 'pages.menu.edit', defaultMessage: '编辑' })}</a>}
            values={record}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:menu:add') && record.menuType !== 'F' && (
          <MenuForm
            key="addChild"
            trigger={<a>{intl.formatMessage({ id: 'pages.menu.addChild', defaultMessage: '新增' })}</a>}
            parentId={record.menuId}
            parentName={record.menuName}
            onOk={() => actionRef.current?.reload?.()}
          />
        ),
        access.hasPermission('system:menu:remove') && (
          <Popconfirm
            key="delete"
            title={intl.formatMessage({ id: 'pages.menu.confirmDelete', defaultMessage: '确定删除该菜单吗?' })}
            onConfirm={() => delRun(record.menuId!)}
          >
            <a style={{ color: '#ff4d4f' }}>
              {intl.formatMessage({ id: 'pages.menu.delete', defaultMessage: '删除' })}
            </a>
          </Popconfirm>
        ),
      ].filter(Boolean),
    },
  ];

  return (
    <PageContainer>
      {contextHolder}
      <ProTable<API.SysMenu, API.SysMenuQueryParams>
        headerTitle={intl.formatMessage({ id: 'pages.menu.title', defaultMessage: '菜单管理' })}
        actionRef={actionRef}
        rowKey="menuId"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button key="expand" onClick={() => handleExpandAll(menuData)}>
            {intl.formatMessage({ id: 'pages.menu.expandAll', defaultMessage: '展开全部' })}
          </Button>,
          <Button key="collapse" onClick={handleCollapseAll}>
            {intl.formatMessage({ id: 'pages.menu.collapseAll', defaultMessage: '折叠全部' })}
          </Button>,
          access.hasPermission('system:menu:add') && (
            <MenuForm key="create" onOk={() => actionRef.current?.reload?.()} />
          ),
        ].filter(Boolean)}
        request={async (params) => {
          const res = await getMenuTree({
            menuName: params.menuName,
            status: params.status,
          });
          const processData = (menus: API.SysMenu[]): API.SysMenu[] => {
            return menus.map((menu) => ({
              ...menu,
              children: menu.children && menu.children.length > 0
                ? processData(menu.children)
                : undefined,
            }));
          };
          const data = processData(res.data || []);
          setMenuData(data);
          return {
            data,
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={false}
        expandable={{
          expandedRowKeys,
          onExpandedRowsChange: (keys) => setExpandedRowKeys(keys as React.Key[]),
        }}
      />
    </PageContainer>
  );
};

export default MenuManagement;
