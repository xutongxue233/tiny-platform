package com.tiny.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.system.entity.SysNoticeRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 公告已读记录Mapper
 */
@Mapper
public interface SysNoticeReadMapper extends BaseMapper<SysNoticeRead> {

    /**
     * 查询用户已读的公告ID集合
     *
     * @param userId    用户ID
     * @param noticeIds 公告ID列表
     * @return 已读的公告ID集合
     */
    @Select("<script>" +
            "SELECT notice_id FROM sys_notice_read " +
            "WHERE user_id = #{userId} AND notice_id IN " +
            "<foreach item='id' collection='noticeIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    Set<Long> selectReadNoticeIds(@Param("userId") Long userId, @Param("noticeIds") List<Long> noticeIds);

    /**
     * 统计用户未读公告数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    @Select("SELECT COUNT(*) FROM sys_notice n " +
            "WHERE n.del_flag = '0' AND n.status = '0' " +
            "AND NOT EXISTS (SELECT 1 FROM sys_notice_read r WHERE r.notice_id = n.notice_id AND r.user_id = #{userId})")
    int countUnread(@Param("userId") Long userId);

    /**
     * 查询所有正常状态的公告ID
     *
     * @return 公告ID列表
     */
    @Select("SELECT notice_id FROM sys_notice WHERE del_flag = '0' AND status = '0'")
    List<Long> selectAllActiveNoticeIds();
}
