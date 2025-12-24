package com.tiny.system.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tiny.common.constant.CommonConstants;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.exception.BusinessException;
import com.tiny.security.context.LoginUser;
import com.tiny.system.dto.OnlineUserQueryDTO;
import com.tiny.system.entity.SysDept;
import com.tiny.system.entity.SysUser;
import com.tiny.system.mapper.SysDeptMapper;
import com.tiny.system.mapper.SysUserMapper;
import com.tiny.system.service.OnlineUserService;
import com.tiny.system.vo.OnlineUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在线用户Service实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {

    private static final String LOGIN_USER_KEY = "loginUser";

    private final SysDeptMapper deptMapper;
    private final SysUserMapper userMapper;

    @Override
    public PageResult<OnlineUserVO> page(OnlineUserQueryDTO queryDTO) {
        List<OnlineUserVO> allOnlineUsers = new ArrayList<>();
        List<Long> deptIds = new ArrayList<>();

        // 搜索所有Token会话（返回的是带前缀的key，如：satoken:login:token:xxx）
        List<String> tokenKeyList = StpUtil.searchTokenValue("", 0, -1, false);
        log.debug("Found {} token keys", tokenKeyList.size());

        for (String tokenKey : tokenKeyList) {
            try {
                // 从key中提取真正的token值（去掉前缀 satoken:login:token:）
                String token = tokenKey;
                if (tokenKey.contains(":")) {
                    token = tokenKey.substring(tokenKey.lastIndexOf(":") + 1);
                }

                // 通过token获取loginId
                Object loginId = StpUtil.getLoginIdByToken(token);
                if (loginId == null) {
                    continue;
                }

                // 获取用户的Session
                SaSession session = StpUtil.getSessionByLoginId(loginId, false);
                if (session == null) {
                    continue;
                }

                LoginUser loginUser = (LoginUser) session.get(LOGIN_USER_KEY);
                if (loginUser == null) {
                    continue;
                }

                // 根据查询条件过滤
                if (StrUtil.isNotBlank(queryDTO.getUsername()) &&
                        !loginUser.getUsername().contains(queryDTO.getUsername())) {
                    continue;
                }

                OnlineUserVO vo = new OnlineUserVO();
                vo.setTokenId(token);
                vo.setUserId(loginUser.getUserId());
                vo.setUsername(loginUser.getUsername());
                vo.setRealName(loginUser.getRealName());
                vo.setDeptId(loginUser.getDeptId());
                vo.setLoginTime(session.getCreateTime());
                vo.setTokenTimeout(StpUtil.getTokenTimeout(token));

                if (loginUser.getDeptId() != null) {
                    deptIds.add(loginUser.getDeptId());
                }

                allOnlineUsers.add(vo);
            } catch (Exception e) {
                log.debug("Error processing token: {}", e.getMessage());
            }
        }

        // 批量查询部门名称
        if (!deptIds.isEmpty()) {
            List<SysDept> depts = deptMapper.selectList(
                    Wrappers.<SysDept>lambdaQuery().in(SysDept::getDeptId, deptIds)
            );
            Map<Long, String> deptNameMap = depts.stream()
                    .collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));

            for (OnlineUserVO vo : allOnlineUsers) {
                if (vo.getDeptId() != null) {
                    vo.setDeptName(deptNameMap.get(vo.getDeptId()));
                }
            }
        }

        // 手动分页
        int current = queryDTO.getCurrent() != null ? queryDTO.getCurrent() : 1;
        int size = queryDTO.getSize() != null ? queryDTO.getSize() : 10;
        int total = allOnlineUsers.size();
        int fromIndex = (current - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);

        List<OnlineUserVO> pageRecords;
        if (fromIndex >= total) {
            pageRecords = new ArrayList<>();
        } else {
            pageRecords = allOnlineUsers.subList(fromIndex, toIndex);
        }

        return PageResult.of(pageRecords, (long) total, (long) current, (long) size);
    }

    @Override
    public void kickout(String token) {
        try {
            // 通过token获取loginId
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId != null) {
                // 检查是否为超级管理员
                checkNotSuperAdmin(Long.parseLong(loginId.toString()), "踢下线");
            }
            // 根据token踢出
            StpUtil.kickoutByTokenValue(token);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Kickout failed: {}", e.getMessage());
        }
    }

    @Override
    public void kickoutByUserId(Long userId) {
        checkNotSuperAdmin(userId, "踢下线");
        StpUtil.kickout(userId);
    }

    @Override
    public void disableUser(Long userId, long disableTime) {
        checkNotSuperAdmin(userId, "封禁");
        // 先踢出所有会话
        StpUtil.kickout(userId);
        // 封禁账号
        StpUtil.disable(userId, disableTime);
    }

    /**
     * 检查用户是否为超级管理员
     */
    private void checkNotSuperAdmin(Long userId, String operation) {
        SysUser user = userMapper.selectById(userId);
        if (user != null && CommonConstants.SUPER_ADMIN.equals(user.getSuperAdmin())) {
            throw new BusinessException("不允许" + operation + "超级管理员");
        }
    }

    @Override
    public void untieDisable(Long userId) {
        StpUtil.untieDisable(userId);
    }

    @Override
    public boolean isDisabled(Long userId) {
        return StpUtil.isDisable(userId);
    }

    @Override
    public long getDisableTime(Long userId) {
        return StpUtil.getDisableTime(userId);
    }
}
