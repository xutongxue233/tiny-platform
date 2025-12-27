package com.tiny.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.message.entity.MsgRecipient;
import com.tiny.message.vo.UserMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消息接收记录Mapper接口
 */
@Mapper
public interface MsgRecipientMapper extends BaseMapper<MsgRecipient> {

    /**
     * 查询用户消息列表
     */
    @Select("<script>" +
            "SELECT r.recipient_id, r.message_id, m.title, m.content, m.message_type, " +
            "m.create_by as sender_name, m.priority, r.is_read, r.read_time, m.create_time " +
            "FROM msg_recipient r " +
            "INNER JOIN msg_message m ON r.message_id = m.message_id " +
            "WHERE r.user_id = #{userId} AND r.is_deleted = '0' AND m.del_flag = '0' AND m.status = '0' " +
            "<if test='messageType != null and messageType != \"\"'>" +
            "AND m.message_type = #{messageType} " +
            "</if>" +
            "<if test='isRead != null and isRead != \"\"'>" +
            "AND r.is_read = #{isRead} " +
            "</if>" +
            "<if test='title != null and title != \"\"'>" +
            "AND m.title LIKE CONCAT('%', #{title}, '%') " +
            "</if>" +
            "ORDER BY m.priority DESC, m.create_time DESC " +
            "</script>")
    List<UserMessageVO> selectUserMessages(@Param("userId") Long userId,
                                           @Param("messageType") String messageType,
                                           @Param("isRead") String isRead,
                                           @Param("title") String title);

    /**
     * 统计用户未读消息数量
     */
    @Select("SELECT COUNT(*) FROM msg_recipient r " +
            "INNER JOIN msg_message m ON r.message_id = m.message_id " +
            "WHERE r.user_id = #{userId} AND r.is_read = '0' AND r.is_deleted = '0' " +
            "AND m.del_flag = '0' AND m.status = '0'")
    int countUnread(@Param("userId") Long userId);

    /**
     * 查询用户未读的消息ID列表
     */
    @Select("SELECT r.recipient_id FROM msg_recipient r " +
            "INNER JOIN msg_message m ON r.message_id = m.message_id " +
            "WHERE r.user_id = #{userId} AND r.is_read = '0' AND r.is_deleted = '0' " +
            "AND m.del_flag = '0' AND m.status = '0'")
    List<Long> selectUnreadRecipientIds(@Param("userId") Long userId);
}
