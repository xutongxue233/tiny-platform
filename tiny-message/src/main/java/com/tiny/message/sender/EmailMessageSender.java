package com.tiny.message.sender;

import cn.hutool.core.util.StrUtil;
import com.tiny.message.entity.MsgEmailConfig;
import com.tiny.message.service.MsgEmailConfigService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 邮件发送器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageSender extends AbstractMessageSender {

    public static final String CHANNEL = "email";

    private final MsgEmailConfigService emailConfigService;

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    protected SendResult doSend(SendContext context) {
        if (StrUtil.isBlank(context.getRecipientAddress())) {
            return SendResult.fail("INVALID_EMAIL", "邮箱地址不能为空");
        }

        // 获取默认邮件配置
        MsgEmailConfig config = emailConfigService.getDefaultConfig();
        if (config == null) {
            return SendResult.fail("NO_CONFIG", "未配置邮件服务器");
        }

        try {
            JavaMailSender mailSender = createMailSender(config);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置发件人
            if (StrUtil.isNotBlank(config.getFromName())) {
                helper.setFrom(config.getFromAddress(), config.getFromName());
            } else {
                helper.setFrom(config.getFromAddress());
            }

            // 设置收件人
            helper.setTo(context.getRecipientAddress());

            // 设置主题和内容
            helper.setSubject(context.getTitle());
            helper.setText(context.getContent(), true);

            // 发送邮件
            mailSender.send(message);

            return SendResult.success();
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return SendResult.fail("SEND_FAILED", e.getMessage());
        }
    }

    /**
     * 创建邮件发送器
     */
    private JavaMailSender createMailSender(MsgEmailConfig config) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getHost());
        mailSender.setPort(config.getPort());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");

        // SSL配置
        if ("1".equals(config.getSslEnable())) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", config.getPort());
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.connectiontimeout", "10000");

        return mailSender;
    }
}
