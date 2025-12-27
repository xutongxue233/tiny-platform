package com.tiny.message.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.MsgSendLogQueryDTO;
import com.tiny.message.entity.MsgMessage;
import com.tiny.message.entity.MsgSendLog;
import com.tiny.message.mapper.MsgMessageMapper;
import com.tiny.message.mapper.MsgSendLogMapper;
import com.tiny.message.service.MsgSendLogService;
import com.tiny.message.vo.MsgSendLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息发送日志Service实现
 */
@Service
@RequiredArgsConstructor
public class MsgSendLogServiceImpl extends ServiceImpl<MsgSendLogMapper, MsgSendLog> implements MsgSendLogService {

    private final MsgMessageMapper messageMapper;

    @Override
    public PageResult<MsgSendLogVO> page(MsgSendLogQueryDTO queryDTO) {
        Page<MsgSendLog> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<MsgSendLog> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(queryDTO.getMessageId() != null, MsgSendLog::getMessageId, queryDTO.getMessageId())
                .eq(StrUtil.isNotBlank(queryDTO.getChannel()), MsgSendLog::getChannel, queryDTO.getChannel())
                .like(StrUtil.isNotBlank(queryDTO.getRecipientAddress()), MsgSendLog::getRecipientAddress, queryDTO.getRecipientAddress())
                .eq(StrUtil.isNotBlank(queryDTO.getSendStatus()), MsgSendLog::getSendStatus, queryDTO.getSendStatus())
                .orderByDesc(MsgSendLog::getSendTime);

        Page<MsgSendLog> result = baseMapper.selectPage(page, wrapper);
        List<MsgSendLogVO> voList = result.getRecords().stream()
                .map(log -> {
                    MsgSendLogVO vo = log.toVO();
                    // 查询消息标题（使用原生SQL忽略逻辑删除）
                    String title = messageMapper.selectTitleById(log.getMessageId());
                    vo.setMessageTitle(title);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public MsgSendLogVO getDetail(Long logId) {
        MsgSendLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("日志不存在");
        }
        MsgSendLogVO vo = log.toVO();
        // 查询消息标题（使用原生SQL忽略逻辑删除）
        String title = messageMapper.selectTitleById(log.getMessageId());
        vo.setMessageTitle(title);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MsgSendLog createLog(Long messageId, Long recipientId, String channel, String recipientAddress) {
        MsgSendLog log = new MsgSendLog();
        log.setMessageId(messageId);
        log.setRecipientId(recipientId);
        log.setChannel(channel);
        log.setRecipientAddress(recipientAddress);
        log.setSendStatus("0");
        log.setRetryCount(0);
        log.setSendTime(LocalDateTime.now());
        this.save(log);
        return log;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateResult(Long logId, boolean success, String errorCode, String errorMsg,
                             String requestData, String responseData) {
        MsgSendLog log = this.getById(logId);
        if (log == null) {
            return;
        }

        if (success) {
            log.setSendStatus("2");
            log.setSuccessTime(LocalDateTime.now());
        } else {
            log.setSendStatus("3");
            log.setErrorCode(errorCode);
            log.setErrorMsg(errorMsg);
        }
        log.setRequestData(requestData);
        log.setResponseData(responseData);
        this.updateById(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retry(Long logId) {
        MsgSendLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("日志不存在");
        }
        if (!"3".equals(log.getSendStatus())) {
            throw new BusinessException("只能重试失败的记录");
        }

        // 更新状态为待发送
        log.setSendStatus("0");
        log.setRetryCount(log.getRetryCount() + 1);
        log.setSendTime(LocalDateTime.now());
        log.setErrorCode(null);
        log.setErrorMsg(null);
        this.updateById(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long logId) {
        MsgSendLog log = this.getById(logId);
        if (log == null) {
            throw new BusinessException("日志不存在");
        }
        this.removeById(logId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> logIds) {
        if (logIds == null || logIds.isEmpty()) {
            throw new BusinessException("请选择要删除的记录");
        }
        this.removeByIds(logIds);
    }
}
