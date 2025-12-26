package com.tiny.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 通知公告DTO
 */
@Data
public class SysNoticeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @Size(max = 100, message = "公告标题长度不能超过100个字符")
    private String noticeTitle;

    /**
     * 公告类型(1通知 2公告)
     */
    @NotBlank(message = "公告类型不能为空")
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
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
}
