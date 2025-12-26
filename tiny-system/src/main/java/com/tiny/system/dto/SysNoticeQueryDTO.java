package com.tiny.system.dto;

import com.tiny.common.core.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 通知公告查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysNoticeQueryDTO extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型(1通知 2公告)
     */
    private String noticeType;

    /**
     * 状态(0正常 1关闭)
     */
    private String status;

    /**
     * 创建者
     */
    private String createBy;
}
