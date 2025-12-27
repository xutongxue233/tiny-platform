package com.tiny.message.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.message.vo.MsgEmailConfigVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 邮件配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("msg_email_config")
public class MsgEmailConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * SMTP服务器
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码/授权码
     */
    private String password;

    /**
     * 发件人地址
     */
    private String fromAddress;

    /**
     * 发件人名称
     */
    private String fromName;

    /**
     * 启用SSL(0否 1是)
     */
    private String sslEnable;

    /**
     * 是否默认(0否 1是)
     */
    private String isDefault;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 转换为VO
     */
    public MsgEmailConfigVO toVO() {
        return BeanUtil.copyProperties(this, MsgEmailConfigVO.class);
    }
}
