import { request } from '@umijs/max';

/** 获取当前用户路由菜单 GET /api/system/menu/routers */
export async function getRouters(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.RouterItem[]>>('/api/system/menu/routers', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取菜单列表 POST /api/system/menu/list */
export async function getMenuList(
  params: API.SysMenuQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu[]>>('/api/system/menu/list', {
    method: 'POST',
    data: params,
    ...(options || {}),
  });
}

/** 获取菜单树 POST /api/system/menu/tree */
export async function getMenuTree(
  params: API.SysMenuQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu[]>>('/api/system/menu/tree', {
    method: 'POST',
    data: params,
    ...(options || {}),
  });
}

/** 获取菜单详情 GET /api/system/menu/detail/:menuId */
export async function getMenuDetail(
  menuId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysMenu>>(`/api/system/menu/detail/${menuId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

/** 新增菜单 POST /api/system/menu */
export async function addMenu(
  data: API.SysMenuDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/menu', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/** 更新菜单 PUT /api/system/menu */
export async function updateMenu(
  data: API.SysMenuDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/system/menu', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/** 删除菜单 DELETE /api/system/menu/:menuId */
export async function deleteMenu(
  menuId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/system/menu/${menuId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}
