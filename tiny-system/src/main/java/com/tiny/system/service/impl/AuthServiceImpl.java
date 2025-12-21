package com.tiny.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.exception.BusinessException;
import com.tiny.core.security.LoginUser;
import com.tiny.core.utils.LoginUserUtil;
import com.tiny.system.dto.LoginDTO;
import com.tiny.system.entity.*;
import com.tiny.system.mapper.*;
import com.tiny.system.service.AuthService;
import com.tiny.system.vo.LoginVO;
import com.tiny.system.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 查询用户
        SysUser user = userMapper.selectOne(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, loginDTO.getUsername())
        );

        if (user == null) {
            throw new BusinessException(CommonConstants.LOGIN_ERROR);
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(CommonConstants.LOGIN_ERROR);
        }

        // 检查用户状态
        if (CommonConstants.STATUS_DISABLE.equals(user.getStatus())) {
            throw new BusinessException("用户已被停用");
        }

        // 查询用户角色
        List<Long> roleIds = userRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, user.getUserId())
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        Set<String> roles = new HashSet<>();
        if (CollUtil.isNotEmpty(roleIds)) {
            roles = roleMapper.selectList(
                    Wrappers.<SysRole>lambdaQuery()
                            .in(SysRole::getRoleId, roleIds)
                            .eq(SysRole::getStatus, CommonConstants.STATUS_NORMAL)
            ).stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        }

        // 查询用户权限
        Set<String> permissions = new HashSet<>();
        if (CollUtil.isNotEmpty(roleIds)) {
            List<Long> menuIds = roleMenuMapper.selectList(
                    Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds)
            ).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

            if (CollUtil.isNotEmpty(menuIds)) {
                permissions = menuMapper.selectList(
                                Wrappers.<SysMenu>lambdaQuery()
                                        .in(SysMenu::getMenuId, menuIds)
                                        .eq(SysMenu::getStatus, CommonConstants.STATUS_NORMAL)
                        ).stream()
                        .map(SysMenu::getPerms)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.toSet());
            }
        }

        // 登录
        StpUtil.login(user.getUserId());

        // 保存登录用户信息
        LoginUser loginUser = BeanUtil.copyProperties(user, LoginUser.class);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        LoginUserUtil.setLoginUser(loginUser);

        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(StpUtil.getTokenValue());

        UserInfoVO userInfo = BeanUtil.copyProperties(user, UserInfoVO.class);
        userInfo.setRoles(roles);
        userInfo.setPermissions(permissions);
        loginVO.setUserInfo(userInfo);

        return loginVO;
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    @Override
    public UserInfoVO getUserInfo() {
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException("未登录");
        }

        UserInfoVO userInfo = BeanUtil.copyProperties(loginUser, UserInfoVO.class);

        // 查询头像
        SysUser user = userMapper.selectById(loginUser.getUserId());
        if (user != null) {
            userInfo.setAvatar(user.getAvatar());
        }

        return userInfo;
    }
}