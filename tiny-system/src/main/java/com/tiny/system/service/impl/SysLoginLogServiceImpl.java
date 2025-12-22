package com.tiny.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiny.common.core.page.PageResult;
import com.tiny.common.utils.IpUtil;
import com.tiny.system.dto.SysLoginLogQueryDTO;
import com.tiny.system.entity.SysLoginLog;
import com.tiny.system.mapper.SysLoginLogMapper;
import com.tiny.system.service.SysLoginLogService;
import com.tiny.system.vo.SysLoginLogVO;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {

    @Override
    public PageResult<SysLoginLogVO> page(SysLoginLogQueryDTO queryDTO) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), SysLoginLog::getUsername, queryDTO.getUsername())
                .like(StrUtil.isNotBlank(queryDTO.getIpAddr()), SysLoginLog::getIpAddr, queryDTO.getIpAddr())
                .eq(StrUtil.isNotBlank(queryDTO.getLoginType()), SysLoginLog::getLoginType, queryDTO.getLoginType())
                .eq(StrUtil.isNotBlank(queryDTO.getStatus()), SysLoginLog::getStatus, queryDTO.getStatus())
                .ge(queryDTO.getBeginTime() != null, SysLoginLog::getLoginTime, queryDTO.getBeginTime())
                .le(queryDTO.getEndTime() != null, SysLoginLog::getLoginTime, queryDTO.getEndTime())
                .orderByDesc(SysLoginLog::getLoginTime);

        Page<SysLoginLog> page = page(new Page<>(queryDTO.getCurrent(), queryDTO.getSize()), wrapper);
        return PageResult.of(page, SysLoginLog::toVO);
    }

    @Async
    @Override
    public void recordLoginLog(String username, Long userId, String loginType, String status, String errorMsg,
                               String ipAddr, String userAgent) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUsername(username);
        loginLog.setUserId(userId);
        loginLog.setLoginType(loginType);
        loginLog.setStatus(status);
        loginLog.setErrorMsg(errorMsg);
        loginLog.setIpAddr(ipAddr);
        loginLog.setLoginLocation(IpUtil.getRegion(ipAddr));
        loginLog.setBrowser(parseBrowser(userAgent));
        loginLog.setOs(parseOs(userAgent));
        loginLog.setUserAgent(userAgent);
        loginLog.setLoginTime(LocalDateTime.now());
        save(loginLog);
    }

    /**
     * 解析浏览器信息
     */
    private String parseBrowser(String userAgentStr) {
        if (StrUtil.isBlank(userAgentStr)) {
            return "unknown";
        }
        cn.hutool.http.useragent.UserAgent userAgent = cn.hutool.http.useragent.UserAgentUtil.parse(userAgentStr);
        if (userAgent == null || userAgent.getBrowser() == null) {
            return "unknown";
        }
        String browser = userAgent.getBrowser().getName();
        String version = userAgent.getVersion();

        // Edge浏览器特殊处理
        if (userAgentStr.contains("Edg/")) {
            browser = "Edge";
            int edgIndex = userAgentStr.indexOf("Edg/");
            if (edgIndex != -1) {
                String edgePart = userAgentStr.substring(edgIndex + 4);
                int spaceIndex = edgePart.indexOf(" ");
                version = spaceIndex > 0 ? edgePart.substring(0, spaceIndex) : edgePart;
            }
        }

        if (StrUtil.isNotBlank(version)) {
            return browser + " " + version;
        }
        return browser;
    }

    /**
     * 解析操作系统信息
     */
    private String parseOs(String userAgentStr) {
        if (StrUtil.isBlank(userAgentStr)) {
            return "unknown";
        }
        cn.hutool.http.useragent.UserAgent userAgent = cn.hutool.http.useragent.UserAgentUtil.parse(userAgentStr);
        if (userAgent == null || userAgent.getOs() == null) {
            return "unknown";
        }
        String os = userAgent.getOs().getName();

        // Windows 11 特殊处理
        if ("Windows 10".equals(os) || (os != null && os.startsWith("Windows"))) {
            if (userAgentStr.contains("Windows NT 10.0")) {
                if (userAgentStr.contains("Windows 11") || userAgentStr.contains("Win11")) {
                    return "Windows 11";
                }
                return "Windows 10";
            }
        }

        String osVersion = userAgent.getOsVersion();
        if (StrUtil.isNotBlank(osVersion)) {
            return os + " " + osVersion;
        }
        return os;
    }

    @Override
    public void deleteBatch(List<Long> loginLogIds) {
        removeByIds(loginLogIds);
    }

    @Override
    public void clean() {
        remove(new LambdaQueryWrapper<>());
    }
}
