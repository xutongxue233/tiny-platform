import type { Settings as LayoutSettings, MenuDataItem } from '@ant-design/pro-components';
import { SettingDrawer } from '@ant-design/pro-components';
import type { RequestConfig, RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import React from 'react';
import {
  AvatarDropdown,
  AvatarName,
  Footer,
  NotificationIcon,
  SelectLang,
} from '@/components';
import { getCurrentUser, getRouters, TokenUtil } from '@/services/ant-design-pro/api';
import defaultSettings from '../config/defaultSettings';
import { errorConfig } from './requestErrorConfig';
import '@ant-design/v5-patch-for-react-19';
import * as allIcons from '@ant-design/icons';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';

const fixMenuItemIcon = (menus: MenuDataItem[]): MenuDataItem[] => {
  return menus.map((item) => {
    const { icon, children, ...rest } = item;
    let iconElement = icon;
    if (typeof icon === 'string' && icon) {
      const iconName = icon.replace(/(Outlined|Filled|TwoTone)$/, '') + 'Outlined';
      const IconComponent = (allIcons as Record<string, any>)[iconName] || (allIcons as Record<string, any>)[icon];
      if (IconComponent) {
        iconElement = React.createElement(IconComponent);
      }
    }
    return {
      ...rest,
      icon: iconElement,
      children: children ? fixMenuItemIcon(children) : undefined,
    };
  });
};

const convertRoutersToMenuData = (routers: API.RouterItem[]): MenuDataItem[] => {
  return routers.map((router) => {
    const menuItem: MenuDataItem = {
      key: router.menuId?.toString(),
      name: router.localeKey || router.name,
      path: router.path,
      icon: router.icon,
      hideInMenu: router.hideInMenu === '1',
      target: router.isFrame === '1' ? router.target || '_blank' : undefined,
    };

    if (router.isFrame === '1' && router.link) {
      menuItem.path = router.link;
    }

    if (router.children && router.children.length > 0) {
      menuItem.children = convertRoutersToMenuData(router.children);
    }

    return menuItem;
  });
};

/**
 * @see https://umijs.org/docs/api/runtime-config#getinitialstate
 */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    try {
      const token = TokenUtil.getToken();
      if (!token) {
        return undefined;
      }
      const response = await getCurrentUser({
        skipErrorHandler: true,
      });
      if (response.code === 200 && response.data) {
        const userInfo = response.data;
        return {
          userId: userInfo.userId,
          username: userInfo.username,
          realName: userInfo.realName,
          avatar: userInfo.avatar,
          roles: userInfo.roles,
          permissions: userInfo.permissions,
          access: userInfo.roles?.includes('admin') ? 'admin' : 'user',
        } as API.CurrentUser;
      }
      return undefined;
    } catch (_error) {
      TokenUtil.removeToken();
      history.push(loginPath);
      return undefined;
    }
  };

  const { location } = history;
  if (
    ![loginPath, '/user/register', '/user/register-result'].includes(
      location.pathname,
    )
  ) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      settings: defaultSettings as Partial<LayoutSettings>,
    };
  }
  return {
    fetchUserInfo,
    settings: defaultSettings as Partial<LayoutSettings>,
  };
}

export const layout: RunTimeLayoutConfig = ({
  initialState,
  setInitialState,
}) => {
  return {
    actionsRender: () => [
      <NotificationIcon key="notification" />,
      <SelectLang key="SelectLang" />,
    ],
    avatarProps: {
      src: initialState?.currentUser?.avatar,
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.realName || initialState?.currentUser?.username,
    },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      if (!initialState?.currentUser && location.pathname !== loginPath) {
        history.push(loginPath);
      }
    },
    bgLayoutImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: [],
    menuHeaderRender: undefined,
    menu: {
      locale: true,
      params: {
        userId: initialState?.currentUser?.userId,
      },
      request: async () => {
        if (!initialState?.currentUser) {
          return [];
        }
        try {
          const response = await getRouters({
            skipErrorHandler: true,
          });
          if (response.code === 200 && response.data) {
            const menuData = convertRoutersToMenuData(response.data);
            return fixMenuItemIcon(menuData);
          }
          return [];
        } catch (_error) {
          return [];
        }
      },
    },
    childrenRender: (children) => {
      return (
        <>
          {children}
          {isDev && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )}
        </>
      );
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request: RequestConfig = {
  ...errorConfig,
};