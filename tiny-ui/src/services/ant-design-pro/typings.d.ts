declare namespace API {
  /** 统一响应结构 */
  type ResponseResult<T = any> = {
    code: number;
    message: string;
    data: T;
    timestamp: number;
  };

  /** 用户信息 */
  type UserInfo = {
    userId: number;
    username: string;
    realName?: string;
    avatar?: string;
    roles: string[];
    permissions: string[];
  };

  /** 登录响应 */
  type LoginResult = {
    token: string;
    userInfo: UserInfo;
  };

  /** 登录参数 */
  type LoginParams = {
    username: string;
    password: string;
    captcha?: string;
    captchaKey?: string;
  };

  /** 当前用户信息（兼容老结构） */
  type CurrentUser = {
    userId?: number;
    username?: string;
    realName?: string;
    avatar?: string;
    roles?: string[];
    permissions?: string[];
    access?: string;
  };

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  type RuleListItem = {
    key?: number;
    disabled?: boolean;
    href?: string;
    avatar?: string;
    name?: string;
    owner?: string;
    desc?: string;
    callNo?: number;
    status?: number;
    updatedAt?: string;
    createdAt?: string;
    progress?: number;
  };

  type RuleList = {
    data?: RuleListItem[];
    total?: number;
    success?: boolean;
  };

  type FakeCaptcha = {
    code?: number;
    status?: string;
  };

  type ErrorResponse = {
    errorCode: string;
    errorMessage?: string;
    success?: boolean;
  };

  type NoticeIconList = {
    data?: NoticeIconItem[];
    total?: number;
    success?: boolean;
  };

  type NoticeIconItemType = 'notification' | 'message' | 'event';

  type NoticeIconItem = {
    id?: string;
    extra?: string;
    key?: string;
    read?: boolean;
    avatar?: string;
    title?: string;
    status?: string;
    datetime?: string;
    description?: string;
    type?: NoticeIconItemType;
  };

  type SysUser = {
    userId?: number;
    username?: string;
    password?: string;
    realName?: string;
    email?: string;
    phone?: string;
    gender?: string;
    avatar?: string;
    status?: string;
    deptId?: number;
    superAdmin?: number;
    remark?: string;
    createTime?: string;
    updateTime?: string;
    roleIds?: number[];
    roleNames?: string[];
  };

  type PageResult<T> = {
    total: number;
    records: T[];
    current: number;
    size: number;
  };

  type SysUserQueryParams = {
    current?: number;
    size?: number;
    username?: string;
    realName?: string;
    phone?: string;
    status?: string;
    deptId?: number;
  };

  type SysUserDTO = {
    userId?: number;
    username?: string;
    password?: string;
    realName?: string;
    phone?: string;
    email?: string;
    gender?: string;
    avatar?: string;
    deptId?: number;
    status?: string;
    remark?: string;
    roleIds?: number[];
  };

  type ResetPasswordDTO = {
    userId: number;
    newPassword: string;
  };

  type UpdateStatusDTO = {
    id: number;
    status: string;
  };
}
