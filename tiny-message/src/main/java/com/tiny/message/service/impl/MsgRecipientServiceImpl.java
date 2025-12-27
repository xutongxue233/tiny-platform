package com.tiny.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.UserMessageQueryDTO;
import com.tiny.message.entity.MsgRecipient;
import com.tiny.message.mapper.MsgRecipientMapper;
import com.tiny.message.service.MsgRecipientService;
import com.tiny.message.vo.UserMessageVO;
import com.tiny.security.utils.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息接收记录Service实现
 */
@Service
@RequiredArgsConstructor
public class MsgRecipientServiceImpl extends ServiceImpl<MsgRecipientMapper, MsgRecipient> implements MsgRecipientService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBatch(Long messageId, List<Long> userIds, List<String> emails) {
        List<MsgRecipient> recipients = new ArrayList<>();

        // 根据用户ID创建接收记录
        if (CollUtil.isNotEmpty(userIds)) {
            for (Long userId : userIds) {
                MsgRecipient recipient = new MsgRecipient();
                recipient.setMessageId(messageId);
                recipient.setUserId(userId);
                recipient.setIsRead("0");
                recipient.setIsDeleted("0");
                recipients.add(recipient);
            }
        }

        // 根据邮箱创建接收记录
        if (CollUtil.isNotEmpty(emails)) {
            for (String email : emails) {
                if (StrUtil.isNotBlank(email)) {
                    MsgRecipient recipient = new MsgRecipient();
                    recipient.setMessageId(messageId);
                    recipient.setRecipientAddress(email);
                    recipient.setIsRead("0");
                    recipient.setIsDeleted("0");
                    recipients.add(recipient);
                }
            }
        }

        if (CollUtil.isNotEmpty(recipients)) {
            this.saveBatch(recipients);
        }
    }

    @Override
    public PageResult<UserMessageVO> pageUserMessages(UserMessageQueryDTO queryDTO) {
        Long userId = LoginUserUtil.getUserId();

        List<UserMessageVO> allMessages = baseMapper.selectUserMessages(
                userId,
                queryDTO.getMessageType(),
                queryDTO.getIsRead(),
                queryDTO.getTitle()
        );

        // 手动分页
        int total = allMessages.size();
        int current = queryDTO.getCurrent().intValue();
        int size = queryDTO.getSize().intValue();
        int start = (current - 1) * size;
        int end = Math.min(start + size, total);

        List<UserMessageVO> pageList = start < total ? allMessages.subList(start, end) : new ArrayList<>();

        return PageResult.of(pageList, (long) total, (long) current, (long) size);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long recipientId) {
        Long userId = LoginUserUtil.getUserId();
        MsgRecipient recipient = this.getById(recipientId);
        if (recipient == null) {
            throw new BusinessException("消息不存在");
        }
        if (!userId.equals(recipient.getUserId())) {
            throw new BusinessException("无权操作此消息");
        }
        if ("1".equals(recipient.getIsRead())) {
            return;
        }
        recipient.setIsRead("1");
        recipient.setReadTime(LocalDateTime.now());
        this.updateById(recipient);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead() {
        Long userId = LoginUserUtil.getUserId();
        List<Long> unreadIds = baseMapper.selectUnreadRecipientIds(userId);
        if (CollUtil.isEmpty(unreadIds)) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<MsgRecipient> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(MsgRecipient::getRecipientId, unreadIds)
                .set(MsgRecipient::getIsRead, "1")
                .set(MsgRecipient::getReadTime, now);
        this.update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUser(Long recipientId) {
        Long userId = LoginUserUtil.getUserId();
        MsgRecipient recipient = this.getById(recipientId);
        if (recipient == null) {
            throw new BusinessException("消息不存在");
        }
        if (!userId.equals(recipient.getUserId())) {
            throw new BusinessException("无权操作此消息");
        }
        recipient.setIsDeleted("1");
        recipient.setDeleteTime(LocalDateTime.now());
        this.updateById(recipient);
    }

    @Override
    public int getUnreadCount() {
        Long userId = LoginUserUtil.getUserId();
        return baseMapper.countUnread(userId);
    }
}
