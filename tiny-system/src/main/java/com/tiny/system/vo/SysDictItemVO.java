package com.tiny.system.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典项VO
 */
@Data
public class SysDictItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典项主键
     */
    private Long itemId;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 字典项标签
     */
    private String itemLabel;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 字典项排序
     */
    private Integer itemSort;

    /**
     * 样式属性
     */
    private String cssClass;

    /**
     * 表格回显样式
     */
    private String listClass;

    /**
     * 是否默认（Y是 N否）
     */
    private String isDefault;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}