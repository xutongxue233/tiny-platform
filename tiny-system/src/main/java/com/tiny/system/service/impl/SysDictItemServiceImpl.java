package com.tiny.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.enums.StatusEnum;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysDictItemDTO;
import com.tiny.system.dto.SysDictItemQueryDTO;
import com.tiny.system.entity.SysDictItem;
import com.tiny.system.mapper.SysDictItemMapper;
import com.tiny.system.service.SysDictItemService;
import com.tiny.system.vo.SysDictItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典项服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements SysDictItemService {

    @Override
    public PageResult<SysDictItemVO> page(SysDictItemQueryDTO queryDTO) {
        Page<SysDictItem> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysDictItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(queryDTO.getDictCode()), SysDictItem::getDictCode, queryDTO.getDictCode())
                .like(StrUtil.isNotBlank(queryDTO.getItemLabel()), SysDictItem::getItemLabel, queryDTO.getItemLabel())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysDictItem::getStatus, queryDTO.getStatus())
                .orderByAsc(SysDictItem::getItemSort);

        Page<SysDictItem> result = baseMapper.selectPage(page, wrapper);
        return PageResult.of(result, SysDictItem::toVO);
    }

    @Override
    public List<SysDictItemVO> listByDictCode(String dictCode) {
        List<SysDictItem> list = this.list(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode)
                .orderByAsc(SysDictItem::getItemSort));
        return list.stream()
                .map(SysDictItem::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysDictItemVO> listEnabledByDictCode(String dictCode) {
        List<SysDictItem> list = this.list(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode)
                .eq(SysDictItem::getStatus, StatusEnum.NORMAL.getCode())
                .orderByAsc(SysDictItem::getItemSort));
        return list.stream()
                .map(SysDictItem::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public SysDictItemVO getDetail(Long itemId) {
        SysDictItem dictItem = this.getById(itemId);
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }
        return dictItem.toVO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysDictItemDTO dto) {
        SysDictItem dictItem = BeanUtil.copyProperties(dto, SysDictItem.class);
        if (dictItem.getItemSort() == null) {
            dictItem.setItemSort(0);
        }
        if (StrUtil.isBlank(dictItem.getStatus())) {
            dictItem.setStatus("0");
        }
        if (StrUtil.isBlank(dictItem.getIsDefault())) {
            dictItem.setIsDefault("N");
        }
        this.save(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictItemDTO dto) {
        SysDictItem dictItem = this.getById(dto.getItemId());
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }

        BeanUtil.copyProperties(dto, dictItem, "itemId");
        this.updateById(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long itemId) {
        SysDictItem dictItem = this.getById(itemId);
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }

        this.removeById(itemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> itemIds) {
        if (CollUtil.isEmpty(itemIds)) {
            return;
        }
        this.removeByIds(itemIds);
    }

    @Override
    public void updateStatus(Long itemId, String status) {
        SysDictItem dictItem = this.getById(itemId);
        if (dictItem == null) {
            throw new BusinessException("字典项不存在");
        }

        dictItem.setStatus(status);
        this.updateById(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDictCode(String dictCode) {
        this.remove(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode));
    }

    @Override
    public long countByDictCode(String dictCode) {
        return this.count(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictCode, dictCode));
    }
}
