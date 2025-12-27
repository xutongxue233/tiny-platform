package com.tiny.message.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.message.vo.MsgMessageVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_message")
public class MsgMessage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long messageId;

    /**
     * 消息类型(system系统消息/user用户消息)
     */
    private String messageType;

    /**
     * 发送渠道(site站内/email邮件)
     */
    private String channel;

    /**
     * 关联模板ID
     */
    private Long templateId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送者ID(系统消息为空)
     */
    private Long senderId;

    /**
     * 发送者类型(system系统/user用户)
     */
    private String senderType;

    /**
     * 业务类型(用于分类)
     */
    private String bizType;

    /**
     * 业务ID(关联业务数据)
     */
    private String bizId;

    /**
     * 优先级(0普通 1重要 2紧急)
     */
    private Integer priority;

    /**
     * 状态(0正常 1撤回)
     */
    private String status;

    /**
     * 转换为VO
     */
    public MsgMessageVO toVO() {
        return BeanUtil.copyProperties(this, MsgMessageVO.class);
    }
}
