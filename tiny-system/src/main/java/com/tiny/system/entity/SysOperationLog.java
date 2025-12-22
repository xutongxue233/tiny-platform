package com.tiny.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import com.tiny.system.vo.SysOperationLogVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志表
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 操作日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long operationLogId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String operationDesc;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应结果
     */
    private String responseResult;

    /**
     * 操作状态（0成功 1失败）
     */
    private String status;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * IP地址
     */
    private String ipAddr;

    /**
     * 操作地点
     */
    private String operationLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 执行时长（毫秒）
     */
    private Long executionTime;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 转换为VO
     */
    public SysOperationLogVO toVO() {
        return BeanUtil.copyProperties(this, SysOperationLogVO.class);
    }
}
