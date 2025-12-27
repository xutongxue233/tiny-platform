import { request } from '@umijs/max';

/**
 * 消息模板API
 */

// 分页查询消息模板
export async function getTemplatePage(
  params: API.MsgTemplateQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.MsgTemplate>>>('/api/message/template/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      templateCode: params.templateCode,
      templateName: params.templateName,
      templateType: params.templateType,
      status: params.status,
    },
    ...(options || {}),
  });
}

// 获取模板下拉列表
export async function getTemplateList(options?: { [key: string]: any }) {
  return request<API.ResponseResult<API.MsgTemplate[]>>('/api/message/template/list', {
    method: 'GET',
    ...(options || {}),
  });
}

// 查询模板详情
export async function getTemplateById(
  templateId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.MsgTemplate>>(`/api/message/template/${templateId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

// 新增模板
export async function addTemplate(
  data: API.MsgTemplateDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/template', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

// 修改模板
export async function updateTemplate(
  data: API.MsgTemplateDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/template', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

// 删除模板
export async function deleteTemplate(
  templateId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/template/${templateId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

// 批量删除模板
export async function deleteTemplateBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/template/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

// 修改模板状态
export async function changeTemplateStatus(
  data: { id: number; status: string },
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/template/status', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

/**
 * 消息API
 */

// 分页查询消息
export async function getMessagePage(
  params: API.MsgMessageQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.MsgMessage>>>('/api/message/list/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      messageType: params.messageType,
      channel: params.channel,
      title: params.title,
      createBy: params.createBy,
      priority: params.priority,
      status: params.status,
    },
    ...(options || {}),
  });
}

// 查询消息详情
export async function getMessageById(
  messageId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.MsgMessage>>(`/api/message/list/${messageId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

// 发送消息
export async function sendMessage(
  data: API.SendMessageDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/list/send', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

// 撤回消息
export async function revokeMessage(
  messageId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/list/revoke/${messageId}`, {
    method: 'PUT',
    ...(options || {}),
  });
}

// 删除消息
export async function deleteMessage(
  messageId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/list/${messageId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

// 批量删除消息
export async function deleteMessageBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/list/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/**
 * 发送记录API
 */

// 分页查询发送记录
export async function getSendLogPage(
  params: API.MsgSendLogQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.MsgSendLog>>>('/api/message/log/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      messageId: params.messageId,
      channel: params.channel,
      recipientAddress: params.recipientAddress,
      sendStatus: params.sendStatus,
    },
    ...(options || {}),
  });
}

// 查询发送记录详情
export async function getSendLogById(
  logId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.MsgSendLog>>(`/api/message/log/${logId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

// 重试发送
export async function retrySend(
  logId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/log/retry/${logId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

// 删除发送记录
export async function deleteSendLog(
  logId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/log/${logId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

// 批量删除发送记录
export async function deleteSendLogBatch(
  data: API.DeleteDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/log/batch', {
    method: 'DELETE',
    data,
    ...(options || {}),
  });
}

/**
 * 邮件配置API
 */

// 查询所有邮件配置
export async function getEmailConfigList(
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.MsgEmailConfig[]>>('/api/message/email/list', {
    method: 'GET',
    ...(options || {}),
  });
}

// 查询邮件配置详情
export async function getEmailConfigById(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.MsgEmailConfig>>(`/api/message/email/${configId}`, {
    method: 'GET',
    ...(options || {}),
  });
}

// 新增邮件配置
export async function addEmailConfig(
  data: API.MsgEmailConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/email', {
    method: 'POST',
    data,
    ...(options || {}),
  });
}

// 修改邮件配置
export async function updateEmailConfig(
  data: API.MsgEmailConfigDTO,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/email', {
    method: 'PUT',
    data,
    ...(options || {}),
  });
}

// 删除邮件配置
export async function deleteEmailConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/email/${configId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}

// 设置默认配置
export async function setDefaultEmailConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/email/default/${configId}`, {
    method: 'PUT',
    ...(options || {}),
  });
}

// 测试连接
export async function testEmailConfig(
  configId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<boolean>>(`/api/message/email/test/${configId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

/**
 * 用户消息中心API
 */

// 分页查询用户消息
export async function getUserMessagePage(
  params: API.UserMessageQueryParams,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<API.PageResult<API.UserMessage>>>('/api/message/center/page', {
    method: 'POST',
    data: {
      current: params.current || 1,
      size: params.size || 10,
      messageType: params.messageType,
      isRead: params.isRead,
      title: params.title,
    },
    ...(options || {}),
  });
}

// 获取未读消息数量
export async function getUnreadMessageCount(
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<number>>('/api/message/center/unread-count', {
    method: 'GET',
    ...(options || {}),
  });
}

// 标记消息已读
export async function markMessageRead(
  recipientId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/center/read/${recipientId}`, {
    method: 'POST',
    ...(options || {}),
  });
}

// 标记所有消息已读
export async function markAllMessageRead(
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>('/api/message/center/read-all', {
    method: 'POST',
    ...(options || {}),
  });
}

// 删除用户消息
export async function deleteUserMessage(
  recipientId: number,
  options?: { [key: string]: any },
) {
  return request<API.ResponseResult<void>>(`/api/message/center/${recipientId}`, {
    method: 'DELETE',
    ...(options || {}),
  });
}
