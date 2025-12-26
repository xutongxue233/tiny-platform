import { request } from '@umijs/max';

/**
 * 分页查询通知公告
 */
export async function getNoticePage(
  params: API.SysNoticeQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.SysNotice>>>(
    '/api/info/notice/page',
    {
      method: 'POST',
      data: {
        current: params.current || 1,
        size: params.size || 10,
        noticeTitle: params.noticeTitle,
        noticeType: params.noticeType,
        status: params.status,
        createBy: params.createBy,
      },
      ...(options || {}),
    },
  );
}

/**
 * 查询通知公告详情
 */
export async function getNoticeDetail(
  noticeId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.SysNotice>>(
    `/api/info/notice/${noticeId}`,
    {
      method: 'GET',
      ...(options || {}),
    },
  );
}

/**
 * 新增通知公告
 */
export async function addNotice(
  data: API.SysNoticeDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

/**
 * 修改通知公告
 */
export async function updateNotice(
  data: API.SysNoticeDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/**
 * 删除通知公告
 */
export async function deleteNotice(
  noticeId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/info/notice/${noticeId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

/**
 * 批量删除通知公告
 */
export async function deleteNoticeBatch(
  data: { ids: number[] },
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/**
 * 修改通知公告状态
 */
export async function changeNoticeStatus(
  data: { id: number; status: string },
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/**
 * 修改通知公告置顶状态
 */
export async function changeNoticeTop(
  data: { id: number; isTop: string },
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice/top', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/**
 * 标记公告已读
 */
export async function markNoticeRead(
  noticeId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/info/notice/read/${noticeId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

/**
 * 获取未读公告数量
 */
export async function getUnreadCount(
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<number>>('/api/info/notice/unread-count', {
    method: 'GET',
    ...(options || {}),
  });
}

/**
 * 全部标记已读
 */
export async function markAllNoticeRead(
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/info/notice/read-all', {
    method: 'POST',
    ...(options || {}),
  });
}
