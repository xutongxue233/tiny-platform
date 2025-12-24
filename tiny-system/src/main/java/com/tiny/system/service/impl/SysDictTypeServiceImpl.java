package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysDictTypeDTO;
import com.tiny.system.dto.SysDictTypeQueryDTO;
import com.tiny.system.entity.SysDictItem;
import com.tiny.system.entity.SysDictType;
import com.tiny.system.mapper.SysDictTypeMapper;
import com.tiny.system.service.SysDictItemService;
import com.tiny.system.service.SysDictTypeService;
import com.tiny.system.vo.SysDictTypeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    private final SysDictItemService dictItemService;

    @Override
    public PageResult<SysDictTypeVO> page(SysDictTypeQueryDTO queryDTO) {
        Page<SysDictType> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getDictName()), SysDictType::getDictName, queryDTO.getDictName())
                .like(StrUtil.isNotBlank(queryDTO.getDictCode()), SysDictType::getDictCode, queryDTO.getDictCode())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysDictType::getStatus, queryDTO.getStatus())
                .orderByDesc(SysDictType::getCreateTime);

        Page<SysDictType> result = baseMapper.selectPage(page, wrapper);
        return PageResult.of(result, SysDictType::toVO);
    }

    @Override
    public List<SysDictTypeVO> listAll() {
        List<SysDictType> list = this.list(Wrappers.<SysDictType>lambdaQuery()
                .orderByDesc(SysDictType::getCreateTime));
        return list.stream()
                .map(SysDictType::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysDictTypeVO getDetail(Long dictId) {
        SysDictType dictType = this.getById(dictId);
        if (dictType == null) {
            throw new BusinessException("字典类型不存在");
        }
        return dictType.toVO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDictTypeDTO dto) {
        if (checkDictCodeExists(dto.getDictCode(), null)) {
            throw new BusinessException("字典编码已存在");
        }

        SysDictType dictType = BeanUtil.copyProperties(dto, SysDictType.class);
        if (StrUtil.isBlank(dictType.getStatus())) {
            dictType.setStatus("0");
        }
        this.save(dictType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictTypeDTO dto) {
        SysDictType dictType = this.getById(dto.getDictId());
        if (dictType == null) {
            throw new BusinessException("字典类型不存在");
        }

        if (checkDictCodeExists(dto.getDictCode(), dto.getDictId())) {
            throw new BusinessException("字典编码已存在");
        }

        String oldDictCode = dictType.getDictCode();
        BeanUtil.copyProperties(dto, dictType, "dictId");
        this.updateById(dictType);

        if (!oldDictCode.equals(dto.getDictCode())) {
            dictItemService.lambdaUpdate()
                    .eq(SysDictItem::getDictCode, oldDictCode)
                    .set(SysDictItem::getDictCode, dto.getDictCode())
                    .update();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long dictId) {
        SysDictType dictType = this.getById(dictId);
        if (dictType == null) {
            throw new BusinessException("字典类型不存在");
        }

        long itemCount = dictItemService.countByDictCode(dictType.getDictCode());
        if (itemCount > 0) {
            throw new BusinessException("该字典类型下存在字典项，无法删除");
        }

        this.removeById(dictId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> dictIds) {
        if (CollUtil.isEmpty(dictIds)) {
            return;
        }

        List<SysDictType> dictTypes = this.listByIds(dictIds);
        for (SysDictType dictType : dictTypes) {
            long itemCount = dictItemService.countByDictCode(dictType.getDictCode());
            if (itemCount > 0) {
                throw new BusinessException("字典类型[" + dictType.getDictName() + "]下存在字典项，无法删除");
            }
        }

        this.removeByIds(dictIds);
    }

    @Override
    public void updateStatus(Long dictId, String status) {
        SysDictType dictType = this.getById(dictId);
        if (dictType == null) {
            throw new BusinessException("字典类型不存在");
        }

        dictType.setStatus(status);
        this.updateById(dictType);
    }

    @Override
    public SysDictType getByDictCode(String dictCode) {
        return this.getOne(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getDictCode, dictCode));
    }

    private boolean checkDictCodeExists(String dictCode, Long excludeDictId) {
        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysDictType::getDictCode, dictCode);
        if (excludeDictId != null) {
            wrapper.ne(SysDictType::getDictId, excludeDictId);
        }
        return this.count(wrapper) > 0;
    }
}
