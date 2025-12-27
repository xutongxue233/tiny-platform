package com.tiny.message.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.message.vo.MsgTemplateVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 消息模板表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_template")
public class MsgTemplate extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableId(type = IdType.AUTO)
    private Long templateId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型(site站内/email邮件)
     */
    private String templateType;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 邮件主题
     */
    private String templateSubject;

    /**
     * 变量列表JSON
     */
    private String variables;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 转换为VO
     */
    public MsgTemplateVO toVO() {
        return BeanUtil.copyProperties(this, MsgTemplateVO.class);
    }
}
