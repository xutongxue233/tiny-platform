package com.tiny.message.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.message.vo.MsgSendLogVO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息发送日志表
 */
@Data
@TableName("msg_send_log")
public class MsgSendLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(type = IdType.AUTO)
    private Long logId;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 接收记录ID
     */
    private Long recipientId;

    /**
     * 发送渠道
     */
    private String channel;

    /**
     * 接收地址
     */
    private String recipientAddress;

    /**
     * 发送状态(0待发送 1发送中 2成功 3失败)
     */
    private String sendStatus;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 成功时间
     */
    private LocalDateTime successTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 转换为VO
     */
    public MsgSendLogVO toVO() {
        return BeanUtil.copyProperties(this, MsgSendLogVO.class);
    }
}
