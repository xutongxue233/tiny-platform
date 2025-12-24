package com.tiny.system.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.system.vo.SysDictItemVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 字典数据表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_item")
public class SysDictItem extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典项主键
     */
    @TableId(type = IdType.AUTO)
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
     * 转换为VO
     */
    public SysDictItemVO toVO() {
        return BeanUtil.copyProperties(this, SysDictItemVO.class);
    }
}