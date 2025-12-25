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
    deptName?: string;
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

  type RouterItem = {
    menuId: number;
    parentId: number;
    name: string;
    path: string;
    component?: string;
    icon?: string;
    isFrame?: string;
    isCache?: string;
    link?: string;
    target?: string;
    hideInMenu?: string;
    badge?: string;
    badgeColor?: string;
    sort?: number;
    perms?: string;
    children?: RouterItem[];
  };

  type SysMenu = {
    menuId?: number;
    menuName?: string;
    parentId?: number;
    sort?: number;
    path?: string;
    component?: string;
    menuType?: string;
    visible?: string;
    status?: string;
    perms?: string;
    icon?: string;
    isFrame?: string;
    isCache?: string;
    link?: string;
    target?: string;
    badge?: string;
    badgeColor?: string;
    remark?: string;
    createTime?: string;
    children?: SysMenu[];
  };

  type SysMenuDTO = {
    menuId?: number;
    menuName: string;
    parentId?: number;
    sort?: number;
    path?: string;
    component?: string;
    menuType?: string;
    visible?: string;
    status?: string;
    perms?: string;
    icon?: string;
    isFrame?: string;
    isCache?: string;
    link?: string;
    target?: string;
    badge?: string;
    badgeColor?: string;
    remark?: string;
  };

  type SysMenuQueryParams = {
    menuName?: string;
    status?: string;
  };

  /** 角色信息 */
  type SysRole = {
    roleId?: number;
    roleName?: string;
    roleKey?: string;
    sort?: number;
    dataScope?: string;
    status?: string;
    remark?: string;
    createTime?: string;
    menuIds?: number[];
    deptIds?: number[];
  };

  /** 角色DTO */
  type SysRoleDTO = {
    roleId?: number;
    roleName: string;
    roleKey: string;
    sort?: number;
    dataScope?: string;
    status?: string;
    remark?: string;
    menuIds?: number[];
    deptIds?: number[];
  };

  /** 部门信息 */
  type SysDept = {
    deptId?: number;
    deptName?: string;
    parentId?: number;
    parentName?: string;
    ancestors?: string;
    sort?: number;
    leader?: string;
    phone?: string;
    email?: string;
    status?: string;
    remark?: string;
    createTime?: string;
    children?: SysDept[];
  };

  /** 部门DTO */
  type SysDeptDTO = {
    deptId?: number;
    deptName: string;
    parentId?: number;
    sort?: number;
    leader?: string;
    phone?: string;
    email?: string;
    status?: string;
    remark?: string;
  };

  /** 部门查询参数 */
  type SysDeptQueryParams = {
    deptName?: string;
    status?: string;
  };

  /** 角色查询参数 */
  type SysRoleQueryParams = {
    current?: number;
    size?: number;
    roleName?: string;
    roleKey?: string;
    status?: string;
  };

  /** 通用删除DTO */
  type DeleteDTO = {
    ids: number[];
  };

  /** 登录日志 */
  type SysLoginLog = {
    loginLogId?: number;
    userId?: number;
    username?: string;
    loginType?: string;
    ipAddr?: string;
    loginLocation?: string;
    browser?: string;
    os?: string;
    userAgent?: string;
    status?: string;
    errorMsg?: string;
    loginTime?: string;
  };

  /** 登录日志查询参数 */
  type SysLoginLogQueryParams = {
    current?: number;
    size?: number;
    username?: string;
    ipAddr?: string;
    loginType?: string;
    status?: string;
    beginTime?: string;
    endTime?: string;
  };

  /** 操作日志 */
  type SysOperationLog = {
    operationLogId?: number;
    userId?: number;
    username?: string;
    moduleName?: string;
    operationType?: string;
    operationDesc?: string;
    requestMethod?: string;
    requestUrl?: string;
    methodName?: string;
    requestParam?: string;
    responseResult?: string;
    status?: string;
    errorMsg?: string;
    ipAddr?: string;
    operationLocation?: string;
    browser?: string;
    os?: string;
    executionTime?: number;
    operationTime?: string;
  };

  /** 操作日志查询参数 */
  type SysOperationLogQueryParams = {
    current?: number;
    size?: number;
    username?: string;
    moduleName?: string;
    operationType?: string;
    status?: string;
    beginTime?: string;
    endTime?: string;
  };

  /** 在线用户 */
  type OnlineUser = {
    tokenId?: string;
    userId?: number;
    username?: string;
    realName?: string;
    deptId?: number;
    deptName?: string;
    ipAddr?: string;
    loginLocation?: string;
    browser?: string;
    os?: string;
    loginTime?: number;
    lastAccessTime?: number;
    tokenTimeout?: number;
  };

  /** 在线用户查询参数 */
  type OnlineUserQueryParams = {
    current?: number;
    size?: number;
    username?: string;
    ipAddr?: string;
  };

  /** 存储类型枚举 */
  type StorageType = 'local' | 'aliyun_oss' | 'minio' | 'aws_s3';

  /** 存储配置VO */
  type StorageConfigVO = {
    configId?: number;
    configName?: string;
    storageType?: string;
    storageTypeDesc?: string;
    isDefault?: string;
    status?: string;
    endpoint?: string;
    bucketName?: string;
    domain?: string;
    region?: string;
    localPath?: string;
    localUrlPrefix?: string;
    useHttps?: string;
    remark?: string;
    createTime?: string;
    updateTime?: string;
  };

  /** 存储配置DTO */
  type StorageConfigDTO = {
    configId?: number;
    configName?: string;
    storageType?: string;
    isDefault?: string;
    status?: string;
    endpoint?: string;
    bucketName?: string;
    accessKeyId?: string;
    accessKeySecret?: string;
    domain?: string;
    region?: string;
    localPath?: string;
    localUrlPrefix?: string;
    useHttps?: string;
    remark?: string;
  };

  /** 存储配置查询参数 */
  type StorageConfigQueryParams = {
    current?: number;
    size?: number;
    configName?: string;
    storageType?: string;
    status?: string;
  };

  /** 存储类型选项 */
  type StorageTypeOption = {
    code: string;
    desc: string;
  };

  /** 文件信息 */
  type FileInfo = {
    fileId?: number;
    originalFilename?: string;
    storedFilename?: string;
    filePath?: string;
    fileUrl?: string;
    fileSize?: number;
    fileSizeDesc?: string;
    fileType?: string;
    fileExt?: string;
    storageType?: string;
    createTime?: string;
  };

  /** 文件查询参数 */
  type FileQueryParams = {
    current?: number;
    size?: number;
    originalFilename?: string;
    fileType?: string;
    storageType?: string;
  };

  /** 文件上传响应 */
  type FileUploadResult = {
    fileId: number;
    originalFilename: string;
    storedFilename: string;
    filePath: string;
    fileUrl: string;
    fileSize: number;
    fileSizeDesc: string;
    fileType: string;
    fileExt: string;
    storageType: string;
  };

  /** 字典类型 */
  type SysDictType = {
    dictId?: number;
    dictName?: string;
    dictCode?: string;
    status?: string;
    remark?: string;
    createTime?: string;
  };

  /** 字典类型DTO */
  type SysDictTypeDTO = {
    dictId?: number;
    dictName: string;
    dictCode: string;
    status?: string;
    remark?: string;
  };

  /** 字典类型查询参数 */
  type SysDictTypeQueryParams = {
    current?: number;
    size?: number;
    dictName?: string;
    dictCode?: string;
    status?: string;
  };

  /** 字典项 */
  type SysDictItem = {
    itemId?: number;
    dictCode?: string;
    itemLabel?: string;
    itemValue?: string;
    itemSort?: number;
    cssClass?: string;
    listClass?: string;
    isDefault?: string;
    status?: string;
    remark?: string;
    createTime?: string;
  };

  /** 字典项DTO */
  type SysDictItemDTO = {
    itemId?: number;
    dictCode: string;
    itemLabel: string;
    itemValue: string;
    itemSort?: number;
    cssClass?: string;
    listClass?: string;
    isDefault?: string;
    status?: string;
    remark?: string;
  };

  /** 字典项查询参数 */
  type SysDictItemQueryParams = {
    current?: number;
    size?: number;
    dictCode?: string;
    itemLabel?: string;
    status?: string;
  };

  /** 系统参数配置 */
  type SysConfig = {
    configId?: number;
    configName?: string;
    configKey?: string;
    configValue?: string;
    configType?: string;
    configGroup?: string;
    isBuiltin?: string;
    status?: string;
    remark?: string;
    createTime?: string;
  };

  /** 系统参数配置DTO */
  type SysConfigDTO = {
    configId?: number;
    configName: string;
    configKey: string;
    configValue?: string;
    configType?: string;
    configGroup?: string;
    isBuiltin?: string;
    status?: string;
    remark?: string;
  };

  /** 系统参数配置查询参数 */
  type SysConfigQueryParams = {
    current?: number;
    size?: number;
    configName?: string;
    configKey?: string;
    configType?: string;
    configGroup?: string;
    status?: string;
  };

  /** 注册参数 */
  type RegisterParams = {
    username: string;
    password: string;
    confirmPassword: string;
    captcha: string;
    captchaKey: string;
  };

  /** 验证码结果 */
  type CaptchaResult = {
    captchaKey: string;
    captchaImage: string;
  };

  /** 公开配置 */
  type PublicConfig = {
    appName?: string;
    appVersion?: string;
    captchaEnabled?: boolean;
    registerEnabled?: boolean;
  };
}
