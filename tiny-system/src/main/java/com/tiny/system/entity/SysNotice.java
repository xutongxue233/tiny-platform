package com.tiny.system.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tiny.common.core.model.BaseEntity;
import com.tiny.system.vo.SysNoticeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 通知公告表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
public class SysNotice extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型(1通知 2公告)
     */
    private String noticeType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 是否置顶(0否 1是)
     */
    private String isTop;

    /**
     * 状态(0正常 1关闭)
     */
    private String status;

    /**
     * 转换为VO
     */
    public SysNoticeVO toVO() {
        return BeanUtil.copyProperties(this, SysNoticeVO.class);
    }
}
