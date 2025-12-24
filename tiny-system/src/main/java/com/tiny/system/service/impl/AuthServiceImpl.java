package com.tiny.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.enums.DataScopeEnum;
import com.tiny.common.enums.LoginTypeEnum;
import com.tiny.common.enums.StatusEnum;
import com.tiny.common.exception.BusinessException;
import com.tiny.core.web.WebUtil;
import com.tiny.security.context.LoginUser;
import com.tiny.security.service.LoginProtectionService;
import com.tiny.security.utils.LoginUserUtil;
import com.tiny.system.dto.LoginDTO;
import com.tiny.system.entity.*;
import com.tiny.system.mapper.*;
import com.tiny.system.service.AuthService;
import com.tiny.system.service.SysLoginLogService;
import com.tiny.system.vo.LoginVO;
import com.tiny.system.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private final SysRoleDeptMapper roleDeptMapper;
    private final SysLoginLogService loginLogService;
    private final LoginProtectionService loginProtectionService;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();

        // 在异步调用前获取请求信息
        String ipAddr = WebUtil.getIpAddr();
        String userAgent = WebUtil.getUserAgent();

        // 检查账号是否被锁定
        if (loginProtectionService.isLocked(username)) {
            long remainSeconds = loginProtectionService.getLockRemainSeconds(username);
            String msg = "账号已被锁定，请" + formatDisableTime(remainSeconds) + "后重试";
            loginLogService.recordLoginLog(username, null, LoginTypeEnum.LOGIN.getCode(), StatusEnum.DISABLE.getCode(), msg, ipAddr, userAgent);
            throw new BusinessException(msg);
        }

        // 查询用户
        SysUser user = userMapper.selectOne(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username)
        );

        if (user == null) {
            int failCount = loginProtectionService.recordFailure(username);
            int remainAttempts = loginProtectionService.getRemainAttempts(username);
            String msg = remainAttempts > 0
                    ? CommonConstants.LOGIN_ERROR + "，剩余尝试次数：" + remainAttempts
                    : "账号已被锁定，请15分钟后重试";
            loginLogService.recordLoginLog(username, null, LoginTypeEnum.LOGIN.getCode(), StatusEnum.DISABLE.getCode(), msg, ipAddr, userAgent);
            throw new BusinessException(msg);
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            int failCount = loginProtectionService.recordFailure(username);
            int remainAttempts = loginProtectionService.getRemainAttempts(username);
            String msg = remainAttempts > 0
                    ? CommonConstants.LOGIN_ERROR + "，剩余尝试次数：" + remainAttempts
                    : "账号已被锁定，请15分钟后重试";
            loginLogService.recordLoginLog(username, user.getUserId(), LoginTypeEnum.LOGIN.getCode(), StatusEnum.DISABLE.getCode(), msg, ipAddr, userAgent);
            throw new BusinessException(msg);
        }

        // 检查用户状态
        if (StatusEnum.DISABLE.getCode().equals(user.getStatus())) {
            loginLogService.recordLoginLog(username, user.getUserId(), LoginTypeEnum.LOGIN.getCode(), StatusEnum.DISABLE.getCode(), "用户已被停用", ipAddr, userAgent);
            throw new BusinessException("用户已被停用");
        }

        // 检查用户是否被封禁
        if (StpUtil.isDisable(user.getUserId())) {
            long disableTime = StpUtil.getDisableTime(user.getUserId());
            String msg = disableTime == -1 ? "账号已被永久封禁" : "账号已被封禁，剩余时间：" + formatDisableTime(disableTime);
            loginLogService.recordLoginLog(username, user.getUserId(), LoginTypeEnum.LOGIN.getCode(), StatusEnum.DISABLE.getCode(), msg, ipAddr, userAgent);
            throw new BusinessException(msg);
        }

        // 查询用户角色
        List<Long> roleIds = userRoleMapper.selectList(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, user.getUserId())
        ).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        Set<String> roles = new HashSet<>();
        String dataScope = DataScopeEnum.SELF.getCode();
        Set<Long> dataScopeDeptIds = new HashSet<>();

        if (CollUtil.isNotEmpty(roleIds)) {
            List<SysRole> roleList = roleMapper.selectList(
                    Wrappers.<SysRole>lambdaQuery()
                            .in(SysRole::getRoleId, roleIds)
                            .eq(SysRole::getStatus, StatusEnum.NORMAL.getCode())
            );

            roles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());

            // 计算最大数据权限范围
            for (SysRole role : roleList) {
                String roleDataScope = role.getDataScope();
                if (StrUtil.isNotBlank(roleDataScope)) {
                    // 数据权限值越小，权限范围越大
                    if (roleDataScope.compareTo(dataScope) < 0) {
                        dataScope = roleDataScope;
                    }
                    // 收集自定义数据权限的部门
                    if (DataScopeEnum.CUSTOM.getCode().equals(roleDataScope)) {
                        List<SysRoleDept> roleDepts = roleDeptMapper.selectList(
                                Wrappers.<SysRoleDept>lambdaQuery().eq(SysRoleDept::getRoleId, role.getRoleId())
                        );
                        dataScopeDeptIds.addAll(roleDepts.stream().map(SysRoleDept::getDeptId).collect(Collectors.toSet()));
                    }
                }
            }
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
                                        .eq(SysMenu::getStatus, StatusEnum.NORMAL.getCode())
                        ).stream()
                        .map(SysMenu::getPerms)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.toSet());
            }
        }

        // 登录
        StpUtil.login(user.getUserId());

        // 登录成功，清除失败计数
        loginProtectionService.clearFailCount(username);

        // 保存登录用户信息
        LoginUser loginUser = BeanUtil.copyProperties(user, LoginUser.class);
        loginUser.setSuperAdmin(Objects.equals(1, user.getSuperAdmin()));
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        loginUser.setRoleIds(new HashSet<>(roleIds));
        loginUser.setDataScope(dataScope);
        loginUser.setDataScopeDeptIds(dataScopeDeptIds);
        LoginUserUtil.setLoginUser(loginUser);

        // 记录登录成功日志
        loginLogService.recordLoginLog(username, user.getUserId(), LoginTypeEnum.LOGIN.getCode(), StatusEnum.NORMAL.getCode(), null, ipAddr, userAgent);

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
        LoginUser loginUser = LoginUserUtil.getLoginUser();
        if (loginUser != null) {
            String ipAddr = WebUtil.getIpAddr();
            String userAgent = WebUtil.getUserAgent();
            loginLogService.recordLoginLog(loginUser.getUsername(), loginUser.getUserId(), LoginTypeEnum.LOGOUT.getCode(), StatusEnum.NORMAL.getCode(), null, ipAddr, userAgent);
        }
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

    /**
     * 格式化封禁剩余时间
     */
    private String formatDisableTime(long seconds) {
        if (seconds <= 0) {
            return "0秒";
        }
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0 && days == 0) {
            sb.append(secs).append("秒");
        }
        return sb.toString();
    }
}