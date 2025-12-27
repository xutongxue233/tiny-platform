package com.tiny.message.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.SendMessageDTO;
import com.tiny.message.entity.MsgMessage;
import com.tiny.message.entity.MsgRecipient;
import com.tiny.message.entity.MsgSendLog;
import com.tiny.message.entity.MsgTemplate;
import com.tiny.message.mapper.MsgMessageMapper;
import com.tiny.message.mapper.MsgRecipientMapper;
import com.tiny.message.sender.MessageSender;
import com.tiny.message.sender.MessageSenderFactory;
import com.tiny.message.sender.SendContext;
import com.tiny.message.sender.SendResult;
import com.tiny.message.service.*;
import com.tiny.message.template.TemplateEngine;
import com.tiny.security.utils.LoginUserUtil;
import com.tiny.websocket.dto.WebSocketMessage;
import com.tiny.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 消息发送Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendServiceImpl implements MessageSendService {

    private final MsgTemplateService templateService;
    private final MsgMessageMapper messageMapper;
    private final MsgRecipientMapper recipientMapper;
    private final MsgRecipientService recipientService;
    private final MsgSendLogService sendLogService;
    private final UserProvider userProvider;
    private final MessageSenderFactory senderFactory;
    private final TemplateEngine templateEngine;
    private final WebSocketService webSocketService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(SendMessageDTO dto) {
        // 参数校验
        validateSendDTO(dto);

        String title = dto.getTitle();
        String content = dto.getContent();

        // 如果使用模板，渲染模板内容
        if (StrUtil.isNotBlank(dto.getTemplateCode())) {
            MsgTemplate template = templateService.getByCode(dto.getTemplateCode());
            if (template == null) {
                throw new BusinessException("消息模板不存在或已停用");
            }
            // 标题优先级：模板主题 > 传入标题 > 模板名称
            if (StrUtil.isNotBlank(template.getTemplateSubject())) {
                title = templateEngine.render(template.getTemplateSubject(), dto.getVariables());
            } else if (StrUtil.isBlank(title)) {
                title = template.getTemplateName();
            }
            content = templateEngine.render(template.getTemplateContent(), dto.getVariables());
        }

        if (StrUtil.isBlank(title)) {
            throw new BusinessException("消息标题不能为空");
        }
        if (StrUtil.isBlank(content)) {
            throw new BusinessException("消息内容不能为空");
        }

        // 创建消息记录
        MsgMessage message = new MsgMessage();
        message.setMessageType(StrUtil.isNotBlank(dto.getNoticeType()) ? "notice" : "system");
        message.setChannel(dto.getChannel());
        message.setTitle(title);
        message.setContent(content);
        message.setSenderId(LoginUserUtil.getUserId());
        message.setSenderType("user");
        message.setBizType(dto.getBizType());
        message.setBizId(dto.getBizId());
        message.setPriority(dto.getPriority() != null ? dto.getPriority() : 0);
        message.setIsTop(StrUtil.isNotBlank(dto.getIsTop()) ? dto.getIsTop() : "0");
        message.setNoticeType(dto.getNoticeType());
        message.setStatus("0");
        messageMapper.insert(message);

        // 创建接收记录并发送
        doSend(message, dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendByTemplate(String templateCode, List<Long> userIds, Map<String, Object> variables) {
        SendMessageDTO dto = new SendMessageDTO();
        dto.setChannel("site");
        dto.setTemplateCode(templateCode);
        dto.setUserIds(userIds);
        dto.setVariables(variables);
        send(dto);
    }

    @Override
    @Async("messageExecutor")
    public void asyncSend(SendMessageDTO dto) {
        try {
            send(dto);
        } catch (Exception e) {
            log.error("异步发送消息失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retrySend(Long logId) {
        MsgSendLog sendLog = sendLogService.getById(logId);
        if (sendLog == null) {
            throw new BusinessException("发送记录不存在");
        }
        if (!"3".equals(sendLog.getSendStatus())) {
            throw new BusinessException("只能重试失败的记录");
        }

        MsgMessage message = messageMapper.selectById(sendLog.getMessageId());
        if (message == null) {
            throw new BusinessException("消息不存在");
        }

        // 更新日志状态
        sendLogService.retry(logId);

        // 重新发送
        SendContext context = new SendContext();
        context.setMessageId(message.getMessageId());
        context.setRecipientId(sendLog.getRecipientId());
        context.setRecipientAddress(sendLog.getRecipientAddress());
        context.setTitle(message.getTitle());
        context.setContent(message.getContent());
        context.setChannel(sendLog.getChannel());

        MessageSender sender = senderFactory.getSender(sendLog.getChannel());
        SendResult result = sender.send(context);

        sendLogService.updateResult(logId, result.isSuccess(),
                result.getErrorCode(), result.getErrorMsg(),
                result.getRequestData(), result.getResponseData());
    }

    /**
     * 执行发送
     */
    private void doSend(MsgMessage message, SendMessageDTO dto) {
        String channel = dto.getChannel();
        MessageSender sender = senderFactory.getSender(channel);

        // 站内消息：只需要创建接收记录
        if ("site".equals(channel)) {
            // 广播模式：获取所有活跃用户
            List<Long> userIds = dto.getUserIds();
            if (Boolean.TRUE.equals(dto.getBroadcast())) {
                userIds = userProvider.getAllActiveUserIds();
            }

            if (CollUtil.isEmpty(userIds)) {
                log.warn("站内消息无接收用户: messageId={}", message.getMessageId());
                return;
            }

            recipientService.createBatch(message.getMessageId(), userIds, null);
            // 为每个接收者创建发送日志(直接成功)
            for (Long userId : userIds) {
                MsgSendLog log = sendLogService.createLog(message.getMessageId(), null, channel, null);
                sendLogService.updateResult(log.getLogId(), true, null, null, null, null);
            }

            // WebSocket实时推送站内消息
            if (Boolean.TRUE.equals(dto.getBroadcast())) {
                // 广播模式：推送到所有在线用户
                broadcastSiteMessage(message);
            } else {
                // 定向推送
                pushSiteMessage(message, userIds);
            }
            return;
        }

        // 邮件消息：需要获取用户邮箱并发送
        if ("email".equals(channel)) {
            List<Long> userIds = dto.getUserIds();
            List<String> emails = dto.getEmails();

            // 根据用户ID获取邮箱
            if (CollUtil.isNotEmpty(userIds)) {
                for (Long userId : userIds) {
                    String email = userProvider.getEmailByUserId(userId);
                    if (StrUtil.isNotBlank(email)) {
                        // 创建接收记录
                        MsgRecipient recipient = new MsgRecipient();
                        recipient.setMessageId(message.getMessageId());
                        recipient.setUserId(userId);
                        recipient.setRecipientAddress(email);
                        recipient.setIsRead("0");
                        recipient.setIsDeleted("0");
                        recipientMapper.insert(recipient);

                        // 发送邮件
                        sendEmail(message, recipient, sender);
                    }
                }
            }

            // 直接发送到指定邮箱
            if (CollUtil.isNotEmpty(emails)) {
                for (String email : emails) {
                    if (StrUtil.isNotBlank(email)) {
                        MsgRecipient recipient = new MsgRecipient();
                        recipient.setMessageId(message.getMessageId());
                        recipient.setRecipientAddress(email);
                        recipient.setIsRead("0");
                        recipient.setIsDeleted("0");
                        recipientMapper.insert(recipient);

                        sendEmail(message, recipient, sender);
                    }
                }
            }
        }
    }

    /**
     * 发送邮件
     */
    private void sendEmail(MsgMessage message, MsgRecipient recipient, MessageSender sender) {
        MsgSendLog log = sendLogService.createLog(
                message.getMessageId(),
                recipient.getRecipientId(),
                "email",
                recipient.getRecipientAddress()
        );

        SendContext context = new SendContext();
        context.setMessageId(message.getMessageId());
        context.setRecipientId(recipient.getRecipientId());
        context.setUserId(recipient.getUserId());
        context.setRecipientAddress(recipient.getRecipientAddress());
        context.setTitle(message.getTitle());
        context.setContent(message.getContent());
        context.setChannel("email");

        SendResult result = sender.send(context);
        sendLogService.updateResult(log.getLogId(), result.isSuccess(),
                result.getErrorCode(), result.getErrorMsg(),
                result.getRequestData(), result.getResponseData());
    }

    /**
     * 校验发送参数
     */
    private void validateSendDTO(SendMessageDTO dto) {
        if (StrUtil.isBlank(dto.getChannel())) {
            throw new BusinessException("发送渠道不能为空");
        }
        if (!senderFactory.supports(dto.getChannel())) {
            throw new BusinessException("不支持的发送渠道: " + dto.getChannel());
        }
        // 广播模式不需要指定用户
        boolean isBroadcast = Boolean.TRUE.equals(dto.getBroadcast());
        if (!isBroadcast && CollUtil.isEmpty(dto.getUserIds()) && CollUtil.isEmpty(dto.getEmails())) {
            throw new BusinessException("接收用户不能为空");
        }
        // 广播只支持站内消息
        if (isBroadcast && !"site".equals(dto.getChannel())) {
            throw new BusinessException("广播模式仅支持站内消息");
        }
        if (StrUtil.isBlank(dto.getTemplateCode()) && StrUtil.isBlank(dto.getTitle())) {
            throw new BusinessException("模板编码和消息标题不能同时为空");
        }
    }

    /**
     * 推送站内消息到在线用户
     */
    private void pushSiteMessage(MsgMessage message, List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        try {
            // 构建推送消息
            WebSocketMessage wsMessage = WebSocketMessage.siteMessage(
                    message.getTitle(),
                    message.getContent(),
                    message.getMessageId()
            );

            // 推送到所有接收用户
            webSocketService.sendToUsers(userIds, "/queue/message", wsMessage);

            log.info("站内消息WebSocket推送完成: messageId={}, userCount={}", message.getMessageId(), userIds.size());
        } catch (Exception e) {
            // WebSocket推送失败不影响主流程
            log.error("站内消息WebSocket推送失败: messageId={}, error={}", message.getMessageId(), e.getMessage());
        }
    }

    /**
     * 广播站内消息到所有在线用户
     */
    private void broadcastSiteMessage(MsgMessage message) {
        try {
            // 构建推送消息
            WebSocketMessage wsMessage = WebSocketMessage.siteMessage(
                    message.getTitle(),
                    message.getContent(),
                    message.getMessageId()
            );

            // 广播到所有在线用户
            webSocketService.broadcast("/topic/broadcast", wsMessage);

            log.info("站内消息广播推送完成: messageId={}", message.getMessageId());
        } catch (Exception e) {
            // WebSocket推送失败不影响主流程
            log.error("站内消息广播推送失败: messageId={}, error={}", message.getMessageId(), e.getMessage());
        }
    }
}
