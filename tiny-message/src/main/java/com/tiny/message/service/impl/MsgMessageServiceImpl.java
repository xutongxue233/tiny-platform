package com.tiny.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.MsgMessageDTO;
import com.tiny.message.dto.MsgMessageQueryDTO;
import com.tiny.message.entity.MsgMessage;
import com.tiny.message.mapper.MsgMessageMapper;
import com.tiny.message.service.MsgMessageService;
import com.tiny.message.vo.MsgMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息Service实现
 */
@Service
@RequiredArgsConstructor
public class MsgMessageServiceImpl extends ServiceImpl<MsgMessageMapper, MsgMessage> implements MsgMessageService {

    @Override
    public PageResult<MsgMessageVO> page(MsgMessageQueryDTO queryDTO) {
        Page<MsgMessage> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<MsgMessage> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(queryDTO.getMessageType()), MsgMessage::getMessageType, queryDTO.getMessageType())
                .eq(StrUtil.isNotBlank(queryDTO.getChannel()), MsgMessage::getChannel, queryDTO.getChannel())
                .like(StrUtil.isNotBlank(queryDTO.getTitle()), MsgMessage::getTitle, queryDTO.getTitle())
                .like(StrUtil.isNotBlank(queryDTO.getCreateBy()), MsgMessage::getCreateBy, queryDTO.getCreateBy())
                .eq(queryDTO.getPriority() != null, MsgMessage::getPriority, queryDTO.getPriority())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), MsgMessage::getStatus, queryDTO.getStatus())
                .orderByDesc(MsgMessage::getCreateTime);

        Page<MsgMessage> result = baseMapper.selectPage(page, wrapper);
        List<MsgMessageVO> voList = result.getRecords().stream()
                .map(message -> {
                    MsgMessageVO vo = message.toVO();
                    // 统计接收人数和成功数
                    vo.setRecipientCount(baseMapper.countRecipients(message.getMessageId()));
                    vo.setSuccessCount(baseMapper.countSuccessSend(message.getMessageId()));
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public MsgMessageVO getDetail(Long messageId) {
        MsgMessage message = this.getById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        MsgMessageVO vo = message.toVO();
        vo.setRecipientCount(baseMapper.countRecipients(messageId));
        vo.setSuccessCount(baseMapper.countSuccessSend(messageId));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MsgMessageDTO dto) {
        MsgMessage message = BeanUtil.copyProperties(dto, MsgMessage.class);
        if (StrUtil.isBlank(message.getStatus())) {
            message.setStatus("0");
        }
        if (message.getPriority() == null) {
            message.setPriority(0);
        }
        message.setSenderType("system");
        this.save(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revoke(Long messageId) {
        MsgMessage message = this.getById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        if ("1".equals(message.getStatus())) {
            throw new BusinessException("消息已撤回");
        }
        if ("email".equals(message.getChannel())) {
            throw new BusinessException("邮件消息不支持撤回");
        }
        message.setStatus("1");
        this.updateById(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long messageId) {
        MsgMessage message = this.getById(messageId);
        if (message == null) {
            throw new BusinessException("消息不存在");
        }
        this.removeById(messageId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            throw new BusinessException("请选择要删除的消息");
        }
        this.removeByIds(messageIds);
    }
}
