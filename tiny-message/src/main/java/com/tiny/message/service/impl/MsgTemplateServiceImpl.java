package com.tiny.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.message.dto.MsgTemplateDTO;
import com.tiny.message.dto.MsgTemplateQueryDTO;
import com.tiny.message.entity.MsgTemplate;
import com.tiny.message.mapper.MsgTemplateMapper;
import com.tiny.message.service.MsgTemplateService;
import com.tiny.message.vo.MsgTemplateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息模板Service实现
 */
@Service
@RequiredArgsConstructor
public class MsgTemplateServiceImpl extends ServiceImpl<MsgTemplateMapper, MsgTemplate> implements MsgTemplateService {

    @Override
    public PageResult<MsgTemplateVO> page(MsgTemplateQueryDTO queryDTO) {
        Page<MsgTemplate> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<MsgTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getTemplateCode()), MsgTemplate::getTemplateCode, queryDTO.getTemplateCode())
                .like(StrUtil.isNotBlank(queryDTO.getTemplateName()), MsgTemplate::getTemplateName, queryDTO.getTemplateName())
                .eq(StrUtil.isNotBlank(queryDTO.getTemplateType()), MsgTemplate::getTemplateType, queryDTO.getTemplateType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), MsgTemplate::getStatus, queryDTO.getStatus())
                .orderByDesc(MsgTemplate::getCreateTime);

        Page<MsgTemplate> result = baseMapper.selectPage(page, wrapper);
        List<MsgTemplateVO> voList = result.getRecords().stream()
                .map(MsgTemplate::toVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public MsgTemplateVO getDetail(Long templateId) {
        MsgTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }
        return template.toVO();
    }

    @Override
    public MsgTemplate getByCode(String templateCode) {
        LambdaQueryWrapper<MsgTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgTemplate::getTemplateCode, templateCode)
                .eq(MsgTemplate::getStatus, "0");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MsgTemplateDTO dto) {
        // 检查模板编码是否重复
        LambdaQueryWrapper<MsgTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgTemplate::getTemplateCode, dto.getTemplateCode());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("模板编码已存在");
        }

        MsgTemplate template = BeanUtil.copyProperties(dto, MsgTemplate.class);
        if (StrUtil.isBlank(template.getStatus())) {
            template.setStatus("0");
        }
        this.save(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MsgTemplateDTO dto) {
        if (dto.getTemplateId() == null) {
            throw new BusinessException("模板ID不能为空");
        }
        MsgTemplate existTemplate = this.getById(dto.getTemplateId());
        if (existTemplate == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查模板编码是否重复(排除自己)
        LambdaQueryWrapper<MsgTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgTemplate::getTemplateCode, dto.getTemplateCode())
                .ne(MsgTemplate::getTemplateId, dto.getTemplateId());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("模板编码已存在");
        }

        MsgTemplate template = BeanUtil.copyProperties(dto, MsgTemplate.class);
        this.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long templateId) {
        MsgTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }
        this.removeById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> templateIds) {
        if (templateIds == null || templateIds.isEmpty()) {
            throw new BusinessException("请选择要删除的模板");
        }
        this.removeByIds(templateIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long templateId, String status) {
        MsgTemplate template = this.getById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }
        template.setStatus(status);
        this.updateById(template);
    }

    @Override
    public List<MsgTemplateVO> listAll() {
        LambdaQueryWrapper<MsgTemplate> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MsgTemplate::getStatus, "0")
                .orderByDesc(MsgTemplate::getCreateTime);
        return this.list(wrapper).stream()
                .map(MsgTemplate::toVO)
                .collect(Collectors.toList());
    }
}
