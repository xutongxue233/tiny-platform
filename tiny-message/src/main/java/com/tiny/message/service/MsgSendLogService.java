package com.tiny.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiny.common.core.page.PageResult;
import com.tiny.message.dto.MsgSendLogQueryDTO;
import com.tiny.message.entity.MsgSendLog;
import com.tiny.message.vo.MsgSendLogVO;

import java.util.List;

/**
 * 消息发送日志Service接口
 */
public interface MsgSendLogService extends IService<MsgSendLog> {

    /**
     * 分页查询发送日志
     */
    PageResult<MsgSendLogVO> page(MsgSendLogQueryDTO queryDTO);

    /**
     * 查询日志详情
     */
    MsgSendLogVO getDetail(Long logId);

    /**
     * 创建发送日志
     */
    MsgSendLog createLog(Long messageId, Long recipientId, String channel, String recipientAddress);

    /**
     * 更新发送结果
     */
    void updateResult(Long logId, boolean success, String errorCode, String errorMsg,
                      String requestData, String responseData);

    /**
     * 重试发送
     */
    void retry(Long logId);

    /**
     * 删除发送日志
     */
    void delete(Long logId);

    /**
     * 批量删除发送日志
     */
    void deleteBatch(List<Long> logIds);
}
