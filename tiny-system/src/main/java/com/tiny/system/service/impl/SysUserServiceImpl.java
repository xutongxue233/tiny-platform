package com.tiny.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysUserDTO;
import com.tiny.system.dto.SysUserQueryDTO;
import com.tiny.system.entity.SysRole;
import com.tiny.system.entity.SysUser;
import com.tiny.system.entity.SysUserRole;
import com.tiny.system.mapper.SysRoleMapper;
import com.tiny.system.mapper.SysUserMapper;
import com.tiny.system.mapper.SysUserRoleMapper;
import com.tiny.system.service.SysUserService;
import com.tiny.system.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public PageResult<SysUserVO> page(SysUserQueryDTO queryDTO) {
        Page<SysUser> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), SysUser::getUsername, queryDTO.getUsername())
                .like(StrUtil.isNotBlank(queryDTO.getRealName()), SysUser::getRealName, queryDTO.getRealName())
                .like(StrUtil.isNotBlank(queryDTO.getPhone()), SysUser::getPhone, queryDTO.getPhone())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysUser::getStatus, queryDTO.getStatus())
                .eq(queryDTO.getDeptId() != null, SysUser::getDeptId, queryDTO.getDeptId())
                .and(w -> w.isNull(SysUser::getSuperAdmin).or().ne(SysUser::getSuperAdmin, CommonConstants.SUPER_ADMIN))
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = baseMapper.selectPage(page, wrapper);
        return PageResult.of(result, this::toVO);
    }

    @Override
    public SysUserVO getDetail(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(SysUserDTO dto) {
        // 检查用户名是否已存在
        if (checkUsernameExists(dto.getUsername(), null)) {
            throw new BusinessException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (StrUtil.isNotBlank(dto.getPhone()) && checkPhoneExists(dto.getPhone(), null)) {
            throw new BusinessException("手机号已存在");
        }

        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(dto.getEmail()) && checkEmailExists(dto.getEmail(), null)) {
            throw new BusinessException("邮箱已存在");
        }

        SysUser user = BeanUtil.copyProperties(dto, SysUser.class, "password", "status");
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setStatus(StrUtil.isBlank(dto.getStatus()) ? CommonConstants.STATUS_NORMAL : dto.getStatus());

        this.save(user);

        // 保存用户角色关联
        if (CollUtil.isNotEmpty(dto.getRoleIds())) {
            saveUserRoles(user.getUserId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserDTO dto) {
        SysUser user = this.getById(dto.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查用户名是否已存在
        if (checkUsernameExists(dto.getUsername(), dto.getUserId())) {
            throw new BusinessException("用户名已存在");
        }

        // 检查手机号是否已存在
        if (StrUtil.isNotBlank(dto.getPhone()) && checkPhoneExists(dto.getPhone(), dto.getUserId())) {
            throw new BusinessException("手机号已存在");
        }

        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(dto.getEmail()) && checkEmailExists(dto.getEmail(), dto.getUserId())) {
            throw new BusinessException("邮箱已存在");
        }

        BeanUtil.copyProperties(dto, user, "userId", "password");
        if (StrUtil.isNotBlank(dto.getPassword())) {
            user.setPassword(BCrypt.hashpw(dto.getPassword()));
        }

        this.updateById(user);

        // 更新用户角色关联
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, dto.getUserId()));
        if (CollUtil.isNotEmpty(dto.getRoleIds())) {
            saveUserRoles(dto.getUserId(), dto.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 不允许删除超级管理员
        if (CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin())) {
            throw new BusinessException("不允许删除超级管理员");
        }

        this.removeById(userId);

        // 删除用户角色关联
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return;
        }

        // 检查是否包含超级管理员
        List<SysUser> users = this.listByIds(userIds);
        for (SysUser user : users) {
            if (CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin())) {
                throw new BusinessException("不允许删除超级管理员");
            }
        }

        this.removeByIds(userIds);

        // 删除用户角色关联
        userRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        user.setPassword(BCrypt.hashpw(newPassword));
        this.updateById(user);
    }

    @Override
    public void updateStatus(Long userId, String status) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 不允许停用超级管理员
        if (CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin()) && CommonConstants.STATUS_DISABLE.equals(status)) {
            throw new BusinessException("不允许停用超级管理员");
        }

        user.setStatus(status);
        this.updateById(user);
    }

    @Override
    public boolean checkUsernameExists(String username, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getUsername, username);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getPhone, phone);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    @Override
    public boolean checkEmailExists(String email, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getEmail, email);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 保存用户角色关联
     */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 实体转VO（包含关联数据）
     */
    private SysUserVO toVO(SysUser user) {
        SysUserVO vo = user.toVO();
        // 查询用户角色
        List<Long> roleIds = userRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, user.getUserId())
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        vo.setRoleIds(roleIds);

        if (CollUtil.isNotEmpty(roleIds)) {
            List<String> roleNames = roleMapper.selectList(
                    Wrappers.<SysRole>lambdaQuery().in(SysRole::getRoleId, roleIds)
            ).stream().map(SysRole::getRoleName).collect(Collectors.toList());
            vo.setRoleNames(roleNames);
            return vo;
        }

        vo.setRoleNames(new ArrayList<>());

        return vo;
    }
}
