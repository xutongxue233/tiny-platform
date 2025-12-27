package com.tiny.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiny.message.entity.MsgSendLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息发送日志Mapper接口
 */
@Mapper
public interface MsgSendLogMapper extends BaseMapper<MsgSendLog> {

}
