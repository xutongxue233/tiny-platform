package com.tiny.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.tiny.common.annotation.DataScope;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.enums.StatusEnum;
import com.tiny.common.exception.BusinessException;
import com.tiny.system.dto.SysUserDTO;
import com.tiny.system.dto.SysUserQueryDTO;
import com.tiny.system.entity.SysDept;
import com.tiny.system.entity.SysRole;
import com.tiny.system.entity.SysUser;
import com.tiny.system.entity.SysUserRole;
import com.tiny.system.mapper.SysDeptMapper;
import com.tiny.system.mapper.SysRoleMapper;
import com.tiny.system.mapper.SysUserMapper;
import com.tiny.system.mapper.SysUserRoleMapper;
import com.tiny.system.service.SysConfigService;
import com.tiny.system.service.SysDeptService;
import com.tiny.system.service.SysUserService;
import com.tiny.system.vo.SysUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysDeptMapper deptMapper;
    private final SysConfigService configService;

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

        // 批量转换VO，避免N+1查询
        List<SysUserVO> voList = toVOListBatch(result.getRecords());
        return PageResult.of(voList, result.getTotal(), result.getCurrent(), result.getSize());
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

        // 校验密码长度
        validatePasswordLength(dto.getPassword());

        SysUser user = BeanUtil.copyProperties(dto, SysUser.class, "password", "status");
        user.setPassword(BCrypt.hashpw(dto.getPassword()));
        user.setStatus(StrUtil.isBlank(dto.getStatus()) ? StatusEnum.NORMAL.getCode() : dto.getStatus());

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
            // 校验密码长度
            validatePasswordLength(dto.getPassword());
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

        // 如果未指定新密码，使用配置的初始密码
        String password = StrUtil.isBlank(newPassword) ? getInitPassword() : newPassword;
        // 校验密码长度
        validatePasswordLength(password);

        user.setPassword(BCrypt.hashpw(password));
        this.updateById(user);
    }

    @Override
    public void updateStatus(Long userId, String status) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 不允许停用超级管理员
        if (CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin()) && StatusEnum.DISABLE.getCode().equals(status)) {
            throw new BusinessException("不允许停用超级管理员");
        }

        user.setStatus(status);
        this.updateById(user);
    }

    @Override
    public void disableUser(Long userId, long disableTime) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 不允许封禁超级管理员
        if (CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin())) {
            throw new BusinessException("不允许封禁超级管理员");
        }

        // 先踢出所有会话
        StpUtil.kickout(userId);
        // 封禁账号
        StpUtil.disable(userId, disableTime);
    }

    @Override
    public void untieDisable(Long userId) {
        StpUtil.untieDisable(userId);
    }

    private boolean checkUsernameExists(String username, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getUsername, username);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    private boolean checkPhoneExists(String phone, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getPhone, phone);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    private boolean checkEmailExists(String email, Long excludeUserId) {
        LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getEmail, email);
        if (excludeUserId != null) {
            wrapper.ne(SysUser::getUserId, excludeUserId);
        }
        return this.count(wrapper) > 0;
    }

    /**
     * 校验密码长度
     */
    private void validatePasswordLength(String password) {
        if (StrUtil.isBlank(password)) {
            return;
        }
        int minLength = getPasswordMinLength();
        int maxLength = getPasswordMaxLength();
        int length = password.length();
        if (length < minLength || length > maxLength) {
            throw new BusinessException("密码长度必须在" + minLength + "-" + maxLength + "位之间");
        }
    }

    /**
     * 获取密码最小长度
     */
    private int getPasswordMinLength() {
        Integer value = configService.getConfigInteger("sys.password.minLength");
        return value != null ? value : 6;
    }

    /**
     * 获取密码最大长度
     */
    private int getPasswordMaxLength() {
        Integer value = configService.getConfigInteger("sys.password.maxLength");
        return value != null ? value : 20;
    }

    /**
     * 获取用户初始密码
     */
    private String getInitPassword() {
        String value = configService.getConfigValue("sys.user.initPassword");
        return StrUtil.isNotBlank(value) ? value : "123456";
    }

    /**
     * 保存用户角色关联（批量插入）
     */
    private void saveUserRoles(Long userId, List<Long> roleIds) {
        List<SysUserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .collect(Collectors.toList());
        Db.saveBatch(userRoles);
    }

    /**
     * 批量转换VO（优化N+1查询）
     */
    private List<SysUserVO> toVOListBatch(List<SysUser> users) {
        if (CollUtil.isEmpty(users)) {
            return new ArrayList<>();
        }

        List<Long> userIds = users.stream().map(SysUser::getUserId).collect(Collectors.toList());
        List<Long> deptIds = users.stream().map(SysUser::getDeptId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

        // 批量查询部门
        Map<Long, String> deptNameMap = CollUtil.isEmpty(deptIds) ? Collections.emptyMap() :
                deptMapper.selectList(Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIds))
                        .stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));

        // 批量查询用户角色关联
        List<SysUserRole> userRoles = userRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, userIds));
        Map<Long, List<Long>> userRoleMap = userRoles.stream()
                .collect(Collectors.groupingBy(SysUserRole::getUserId,
                        Collectors.mapping(SysUserRole::getRoleId, Collectors.toList())));

        // 批量查询角色信息
        List<Long> allRoleIds = userRoles.stream().map(SysUserRole::getRoleId).distinct().collect(Collectors.toList());
        Map<Long, String> roleNameMap = CollUtil.isEmpty(allRoleIds) ? Collections.emptyMap() :
                roleMapper.selectList(Wrappers.<SysRole>lambdaQuery().in(SysRole::getRoleId, allRoleIds))
                        .stream().collect(Collectors.toMap(SysRole::getRoleId, SysRole::getRoleName));

        // 组装VO
        return users.stream().map(user -> {
            SysUserVO vo = user.toVO();
            if (user.getDeptId() != null) {
                vo.setDeptName(deptNameMap.get(user.getDeptId()));
            }

            List<Long> roleIds = userRoleMap.getOrDefault(user.getUserId(), new ArrayList<>());
            vo.setRoleIds(roleIds);
            vo.setRoleNames(roleIds.stream().map(roleNameMap::get).filter(Objects::nonNull).collect(Collectors.toList()));

            // 查询封禁状态
            boolean disabled = StpUtil.isDisable(user.getUserId());
            vo.setDisabled(disabled);
            if (disabled) {
                vo.setDisableTime(StpUtil.getDisableTime(user.getUserId()));
            }

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 实体转VO（包含关联数据，用于单条查询）
     */
    private SysUserVO toVO(SysUser user) {
        SysUserVO vo = user.toVO();

        // 查询部门名称
        if (user.getDeptId() != null) {
            SysDept dept = deptMapper.selectById(user.getDeptId());
            if (dept != null) {
                vo.setDeptName(dept.getDeptName());
            }
        }

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
        } else {
            vo.setRoleNames(new ArrayList<>());
        }

        // 查询封禁状态
        boolean disabled = StpUtil.isDisable(user.getUserId());
        vo.setDisabled(disabled);
        if (disabled) {
            vo.setDisableTime(StpUtil.getDisableTime(user.getUserId()));
        }

        return vo;
    }
}
