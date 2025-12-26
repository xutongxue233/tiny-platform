package com.tiny.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.system.dto.SysNoticeDTO;
import com.tiny.system.dto.SysNoticeQueryDTO;
import com.tiny.system.entity.SysNotice;
import com.tiny.system.vo.SysNoticeVO;

import java.util.List;

/**
 * 通知公告Service接口
 */
public interface SysNoticeService extends IService<SysNotice> {

    /**
     * 分页查询通知公告
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageResult<SysNoticeVO> page(SysNoticeQueryDTO queryDTO);

    /**
     * 查询通知公告详情
     *
     * @param noticeId 公告ID
     * @return 公告详情
     */
    SysNoticeVO getDetail(Long noticeId);

    /**
     * 新增通知公告
     *
     * @param dto 公告信息
     */
    void add(SysNoticeDTO dto);

    /**
     * 修改通知公告
     *
     * @param dto 公告信息
     */
    void update(SysNoticeDTO dto);

    /**
     * 删除通知公告
     *
     * @param noticeId 公告ID
     */
    void delete(Long noticeId);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 公告ID列表
     */
    void deleteBatch(List<Long> noticeIds);

    /**
     * 修改通知公告状态
     *
     * @param noticeId 公告ID
     * @param status   状态
     */
    void updateStatus(Long noticeId, String status);

    /**
     * 修改通知公告置顶状态
     *
     * @param noticeId 公告ID
     * @param isTop    是否置顶
     */
    void updateTop(Long noticeId, String isTop);

    /**
     * 标记公告已读
     *
     * @param noticeId 公告ID
     */
    void markAsRead(Long noticeId);

    /**
     * 获取当前用户未读公告数量
     *
     * @return 未读数量
     */
    int getUnreadCount();

    /**
     * 将所有公告标记为已读
     */
    void markAllAsRead();
}
