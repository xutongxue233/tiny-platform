package com.tiny.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.MsgEmailConfigDTO;
import com.tiny.message.dto.MsgTemplateQueryDTO;
import com.tiny.message.entity.MsgEmailConfig;
import com.tiny.message.mapper.MsgEmailConfigMapper;
import com.tiny.message.service.MsgEmailConfigService;
import com.tiny.message.vo.MsgEmailConfigVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 邮件配置Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MsgEmailConfigServiceImpl extends ServiceImpl<MsgEmailConfigMapper, MsgEmailConfig> implements MsgEmailConfigService {

    @Override
    public List<MsgEmailConfigVO> listAll() {
        LambdaQueryWrapper<MsgEmailConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgEmailConfig::getStatus, "0")
                .orderByDesc(MsgEmailConfig::getIsDefault)
                .orderByDesc(MsgEmailConfig::getCreateTime);
        return this.list(wrapper).stream()
                .map(MsgEmailConfig::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<MsgEmailConfigVO> page(MsgTemplateQueryDTO queryDTO) {
        Page<MsgEmailConfig> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<MsgEmailConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(queryDTO.getStatus()), MsgEmailConfig::getStatus, queryDTO.getStatus())
                .orderByDesc(MsgEmailConfig::getIsDefault)
                .orderByDesc(MsgEmailConfig::getCreateTime);

        Page<MsgEmailConfig> result = baseMapper.selectPage(page, wrapper);
        List<MsgEmailConfigVO> voList = result.getRecords().stream()
                .map(MsgEmailConfig::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public MsgEmailConfigVO getDetail(Long configId) {
        MsgEmailConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        return config.toVO();
    }

    @Override
    public MsgEmailConfig getDefaultConfig() {
        LambdaQueryWrapper<MsgEmailConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgEmailConfig::getIsDefault, "1")
                .eq(MsgEmailConfig::getStatus, "0");
        MsgEmailConfig config = this.getOne(wrapper);
        if (config == null) {
            // 没有默认配置，取第一个可用配置
            wrapper = Wrappers.lambdaQuery();
            wrapper.eq(MsgEmailConfig::getStatus, "0")
                    .orderByDesc(MsgEmailConfig::getCreateTime)
                    .last("LIMIT 1");
            config = this.getOne(wrapper);
        }
        return config;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MsgEmailConfigDTO dto) {
        MsgEmailConfig config = BeanUtil.copyProperties(dto, MsgEmailConfig.class);
        if (StrUtil.isBlank(config.getStatus())) {
            config.setStatus("0");
        }
        if (StrUtil.isBlank(config.getSslEnable())) {
            config.setSslEnable("1");
        }
        if (StrUtil.isBlank(config.getIsDefault())) {
            config.setIsDefault("0");
        }

        // 如果设置为默认，清除其他默认配置
        if ("1".equals(config.getIsDefault())) {
            clearDefault();
        }

        this.save(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MsgEmailConfigDTO dto) {
        if (dto.getConfigId() == null) {
            throw new BusinessException("配置ID不能为空");
        }
        MsgEmailConfig existConfig = this.getById(dto.getConfigId());
        if (existConfig == null) {
            throw new BusinessException("配置不存在");
        }

        MsgEmailConfig config = BeanUtil.copyProperties(dto, MsgEmailConfig.class);
        // 密码为空时保留原密码
        if (StrUtil.isBlank(config.getPassword())) {
            config.setPassword(existConfig.getPassword());
        }

        // 如果设置为默认，清除其他默认配置
        if ("1".equals(config.getIsDefault()) && !"1".equals(existConfig.getIsDefault())) {
            clearDefault();
        }

        this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long configId) {
        MsgEmailConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        this.removeById(configId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long configId) {
        MsgEmailConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        if ("1".equals(config.getStatus())) {
            throw new BusinessException("停用的配置不能设为默认");
        }

        clearDefault();
        config.setIsDefault("1");
        this.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long configId, String status) {
        MsgEmailConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        // 如果是停用且是默认配置，需要清除默认
        if ("1".equals(status) && "1".equals(config.getIsDefault())) {
            config.setIsDefault("0");
        }
        config.setStatus(status);
        this.updateById(config);
    }

    @Override
    public boolean testConnection(Long configId) {
        MsgEmailConfig config = this.getById(configId);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }

        try {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(config.getHost());
            mailSender.setPort(config.getPort());
            mailSender.setUsername(config.getUsername());
            mailSender.setPassword(config.getPassword());

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.timeout", "5000");
            props.put("mail.smtp.connectiontimeout", "5000");

            if ("1".equals(config.getSslEnable())) {
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            } else {
                props.put("mail.smtp.starttls.enable", "true");
            }

            mailSender.testConnection();
            return true;
        } catch (Exception e) {
            log.error("邮件服务器连接测试失败", e);
            throw new BusinessException("连接测试失败: " + e.getMessage());
        }
    }

    /**
     * 清除所有默认配置
     */
    private void clearDefault() {
        LambdaUpdateWrapper<MsgEmailConfig> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MsgEmailConfig::getIsDefault, "0")
                .eq(MsgEmailConfig::getIsDefault, "1");
        this.update(wrapper);
    }
}
