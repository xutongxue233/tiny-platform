package com.tiny.system.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.system.vo.SysConfigVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 系统参数配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
public class SysConfig extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 参数配置ID
     */
    @TableId(type = IdType.AUTO)
    private Long configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 参数类型(STRING/NUMBER/BOOLEAN/JSON)
     */
    private String configType;

    /**
     * 参数分组(SYSTEM/BUSINESS)
     */
    private String configGroup;

    /**
     * 是否内置(Y是 N否)
     */
    private String isBuiltin;

    /**
     * 状态(0正常 1停用)
     */
    private String status;

    /**
     * 转换为VO
     */
    public SysConfigVO toVO() {
        return BeanUtil.copyProperties(this, SysConfigVO.class);
    }
}
