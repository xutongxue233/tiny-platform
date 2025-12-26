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
import com.tiny.security.utils.LoginUserUtil;
import com.tiny.system.dto.SysNoticeDTO;
import com.tiny.system.dto.SysNoticeQueryDTO;
import com.tiny.system.entity.SysNotice;
import com.tiny.system.entity.SysNoticeRead;
import com.tiny.system.mapper.SysNoticeMapper;
import com.tiny.system.mapper.SysNoticeReadMapper;
import com.tiny.system.service.SysNoticeService;
import com.tiny.system.vo.SysNoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 通知公告Service实现
 */
@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {

    private final SysNoticeReadMapper noticeReadMapper;

    @Override
    public PageResult<SysNoticeVO> page(SysNoticeQueryDTO queryDTO) {
        Page<SysNotice> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysNotice> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getNoticeTitle()), SysNotice::getNoticeTitle, queryDTO.getNoticeTitle())
                .eq(StrUtil.isNotBlank(queryDTO.getNoticeType()), SysNotice::getNoticeType, queryDTO.getNoticeType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysNotice::getStatus, queryDTO.getStatus())
                .like(StrUtil.isNotBlank(queryDTO.getCreateBy()), SysNotice::getCreateBy, queryDTO.getCreateBy())
                .orderByDesc(SysNotice::getIsTop)
                .orderByDesc(SysNotice::getCreateTime);

        Page<SysNotice> result = baseMapper.selectPage(page, wrapper);
        List<SysNoticeVO> voList = result.getRecords().stream()
                .map(SysNotice::toVO)
                .collect(Collectors.toList());

        // 查询当前用户已读的公告ID
        if (CollUtil.isNotEmpty(voList)) {
            Long userId = LoginUserUtil.getUserId();
            List<Long> noticeIds = voList.stream().map(SysNoticeVO::getNoticeId).collect(Collectors.toList());
            Set<Long> readNoticeIds = noticeReadMapper.selectReadNoticeIds(userId, noticeIds);
            // 设置已读状态
            voList.forEach(vo -> vo.setIsRead(readNoticeIds.contains(vo.getNoticeId()) ? "1" : "0"));
        }

        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public SysNoticeVO getDetail(Long noticeId) {
        SysNotice notice = this.getById(noticeId);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        return notice.toVO();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysNoticeDTO dto) {
        SysNotice notice = BeanUtil.copyProperties(dto, SysNotice.class);
        if (StrUtil.isBlank(notice.getIsTop())) {
            notice.setIsTop("0");
        }
        if (StrUtil.isBlank(notice.getStatus())) {
            notice.setStatus("0");
        }
        this.save(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysNoticeDTO dto) {
        if (dto.getNoticeId() == null) {
            throw new BusinessException("公告ID不能为空");
        }
        SysNotice existNotice = this.getById(dto.getNoticeId());
        if (existNotice == null) {
            throw new BusinessException("通知公告不存在");
        }
        SysNotice notice = BeanUtil.copyProperties(dto, SysNotice.class);
        this.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long noticeId) {
        SysNotice notice = this.getById(noticeId);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        this.removeById(noticeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> noticeIds) {
        if (noticeIds == null || noticeIds.isEmpty()) {
            throw new BusinessException("请选择要删除的公告");
        }
        this.removeByIds(noticeIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long noticeId, String status) {
        SysNotice notice = this.getById(noticeId);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        notice.setStatus(status);
        this.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTop(Long noticeId, String isTop) {
        SysNotice notice = this.getById(noticeId);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        notice.setIsTop(isTop);
        this.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long noticeId) {
        Long userId = LoginUserUtil.getUserId();
        // 检查公告是否存在
        SysNotice notice = this.getById(noticeId);
        if (notice == null) {
            throw new BusinessException("通知公告不存在");
        }
        // 检查是否已经标记已读
        LambdaQueryWrapper<SysNoticeRead> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysNoticeRead::getNoticeId, noticeId)
                .eq(SysNoticeRead::getUserId, userId);
        if (noticeReadMapper.selectCount(wrapper) > 0) {
            return;
        }
        // 插入已读记录
        SysNoticeRead noticeRead = new SysNoticeRead();
        noticeRead.setNoticeId(noticeId);
        noticeRead.setUserId(userId);
        noticeRead.setReadTime(LocalDateTime.now());
        noticeReadMapper.insert(noticeRead);
    }

    @Override
    public int getUnreadCount() {
        Long userId = LoginUserUtil.getUserId();
        return noticeReadMapper.countUnread(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead() {
        Long userId = LoginUserUtil.getUserId();
        // 获取所有正常状态的公告ID
        List<Long> allNoticeIds = noticeReadMapper.selectAllActiveNoticeIds();
        if (CollUtil.isEmpty(allNoticeIds)) {
            return;
        }
        // 获取已读的公告ID
        Set<Long> readNoticeIds = noticeReadMapper.selectReadNoticeIds(userId, allNoticeIds);
        // 过滤出未读的公告ID
        List<Long> unreadNoticeIds = allNoticeIds.stream()
                .filter(id -> !readNoticeIds.contains(id))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(unreadNoticeIds)) {
            return;
        }
        // 批量插入已读记录
        LocalDateTime now = LocalDateTime.now();
        List<SysNoticeRead> readList = new ArrayList<>();
        for (Long noticeId : unreadNoticeIds) {
            SysNoticeRead noticeRead = new SysNoticeRead();
            noticeRead.setNoticeId(noticeId);
            noticeRead.setUserId(userId);
            noticeRead.setReadTime(now);
            readList.add(noticeRead);
        }
        for (SysNoticeRead noticeRead : readList) {
            noticeReadMapper.insert(noticeRead);
        }
    }
}
