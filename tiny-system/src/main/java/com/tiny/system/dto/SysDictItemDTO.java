package com.tiny.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字典项DTO
 */
@Data
public class SysDictItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典项主键
     */
    private Long itemId;

    /**
     * 字典编码
     */
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 100, message = "字典编码长度不能超过100个字符")
    private String dictCode;

    /**
     * 字典项标签
     */
    @NotBlank(message = "字典项标签不能为空")
    @Size(max = 100, message = "字典项标签长度不能超过100个字符")
    private String itemLabel;

    /**
     * 字典项值
     */
    @NotBlank(message = "字典项值不能为空")
    @Size(max = 100, message = "字典项值长度不能超过100个字符")
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
}