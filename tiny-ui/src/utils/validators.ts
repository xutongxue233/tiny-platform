/**
 * 通用表单验证规则
 * 统一管理常用的验证规则，避免重复定义
 */
import type { Rule } from 'antd/es/form';

/** 正则表达式常量 */
export const PATTERNS = {
  /** 手机号 */
  PHONE: /^1[3-9]\d{9}$/,
  /** 邮箱 */
  EMAIL: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
  /** 用户名（字母开头，允许字母数字下划线） */
  USERNAME: /^[a-zA-Z][a-zA-Z0-9_]{1,19}$/,
  /** 密码（至少包含字母和数字） */
  PASSWORD: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{6,20}$/,
  /** 纯数字 */
  NUMBER: /^\d+$/,
  /** 正整数 */
  POSITIVE_INT: /^[1-9]\d*$/,
  /** URL */
  URL: /^https?:\/\/.+/,
  /** IP地址 */
  IP: /^(\d{1,3}\.){3}\d{1,3}$/,
  /** 中文 */
  CHINESE: /^[\u4e00-\u9fa5]+$/,
  /** 字母数字 */
  ALPHANUMERIC: /^[a-zA-Z0-9]+$/,
  /** 权限标识（字母、数字、冒号） */
  PERMISSION: /^[a-zA-Z][a-zA-Z0-9:]*$/,
} as const;

/** 验证规则工厂函数 */
export const rules = {
  /** 必填 */
  required: (message = '此项为必填项'): Rule => ({
    required: true,
    message,
  }),

  /** 最小长度 */
  minLength: (min: number, message?: string): Rule => ({
    min,
    message: message || `长度不能少于${min}个字符`,
  }),

  /** 最大长度 */
  maxLength: (max: number, message?: string): Rule => ({
    max,
    message: message || `长度不能超过${max}个字符`,
  }),

  /** 长度范围 */
  lengthRange: (min: number, max: number, message?: string): Rule => ({
    min,
    max,
    message: message || `长度应在${min}-${max}个字符之间`,
  }),

  /** 手机号 */
  phone: (message = '请输入正确的手机号'): Rule => ({
    pattern: PATTERNS.PHONE,
    message,
  }),

  /** 邮箱 */
  email: (message = '请输入正确的邮箱地址'): Rule => ({
    type: 'email',
    message,
  }),

  /** 用户名 */
  username: (message = '用户名需以字母开头，2-20位字母数字下划线'): Rule => ({
    pattern: PATTERNS.USERNAME,
    message,
  }),

  /** 密码强度 */
  password: (message = '密码需6-20位，包含字母和数字'): Rule => ({
    pattern: PATTERNS.PASSWORD,
    message,
  }),

  /** URL */
  url: (message = '请输入正确的URL地址'): Rule => ({
    pattern: PATTERNS.URL,
    message,
  }),

  /** IP地址 */
  ip: (message = '请输入正确的IP地址'): Rule => ({
    pattern: PATTERNS.IP,
    message,
  }),

  /** 纯数字 */
  number: (message = '请输入数字'): Rule => ({
    pattern: PATTERNS.NUMBER,
    message,
  }),

  /** 正整数 */
  positiveInt: (message = '请输入正整数'): Rule => ({
    pattern: PATTERNS.POSITIVE_INT,
    message,
  }),

  /** 中文 */
  chinese: (message = '请输入中文'): Rule => ({
    pattern: PATTERNS.CHINESE,
    message,
  }),

  /** 字母数字 */
  alphanumeric: (message = '只能输入字母和数字'): Rule => ({
    pattern: PATTERNS.ALPHANUMERIC,
    message,
  }),

  /** 权限标识 */
  permission: (message = '权限标识格式不正确'): Rule => ({
    pattern: PATTERNS.PERMISSION,
    message,
  }),

  /** 自定义正则 */
  pattern: (pattern: RegExp, message: string): Rule => ({
    pattern,
    message,
  }),

  /** 自定义验证器 */
  validator: (
    validatorFn: (value: any) => boolean | Promise<boolean>,
    message: string,
  ): Rule => ({
    validator: async (_, value) => {
      const result = await validatorFn(value);
      if (!result) {
        throw new Error(message);
      }
    },
  }),
};

/** 预定义的常用验证规则组合 */
export const commonRules = {
  /** 用户名验证规则 */
  username: [
    rules.required('请输入用户名'),
    rules.lengthRange(2, 20, '用户名长度为2-20个字符'),
  ],

  /** 密码验证规则 */
  password: [
    rules.required('请输入密码'),
    rules.lengthRange(6, 20, '密码长度为6-20个字符'),
  ],

  /** 手机号验证规则 */
  phone: [rules.phone('请输入正确的手机号')],

  /** 邮箱验证规则 */
  email: [rules.email('请输入正确的邮箱地址')],

  /** 必填手机号 */
  requiredPhone: [
    rules.required('请输入手机号'),
    rules.phone('请输入正确的手机号'),
  ],

  /** 必填邮箱 */
  requiredEmail: [
    rules.required('请输入邮箱'),
    rules.email('请输入正确的邮箱地址'),
  ],

  /** 名称（通用） */
  name: [
    rules.required('请输入名称'),
    rules.lengthRange(1, 50, '名称长度不能超过50个字符'),
  ],

  /** 编码（通用） */
  code: [
    rules.required('请输入编码'),
    rules.lengthRange(1, 50, '编码长度不能超过50个字符'),
    rules.alphanumeric('编码只能包含字母和数字'),
  ],

  /** 备注 */
  remark: [rules.maxLength(500, '备注长度不能超过500个字符')],

  /** 排序号 */
  sort: [rules.positiveInt('请输入正整数')],
};

export default rules;
