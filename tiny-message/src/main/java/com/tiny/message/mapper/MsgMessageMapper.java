package com.tiny.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.message.entity.MsgMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 消息Mapper接口
 */
@Mapper
public interface MsgMessageMapper extends BaseMapper<MsgMessage> {

    /**
     * 统计消息的接收人数
     */
    @Select("SELECT COUNT(*) FROM msg_recipient WHERE message_id = #{messageId}")
    int countRecipients(@Param("messageId") Long messageId);

    /**
     * 统计消息的成功发送数
     */
    @Select("SELECT COUNT(*) FROM msg_send_log WHERE message_id = #{messageId} AND send_status = '2'")
    int countSuccessSend(@Param("messageId") Long messageId);

    /**
     * 查询消息标题（忽略逻辑删除）
     */
    @Select("SELECT title FROM msg_message WHERE message_id = #{messageId}")
    String selectTitleById(@Param("messageId") Long messageId);
}
