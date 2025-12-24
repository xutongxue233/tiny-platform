package com.tiny.core.web;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Web工具类
 */
public class WebUtil {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取当前HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return UNKNOWN;
        }
        return getIpAddr(request);
    }

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (isUnknown(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isUnknown(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (isUnknown(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时，取第一个IP
        if (StrUtil.isNotBlank(ip) && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取User-Agent
     */
    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        return request.getHeader("User-Agent");
    }

    /**
     * 获取浏览器名称（含版本）
     */
    public static String getBrowser() {
        String userAgentStr = getUserAgent();
        if (StrUtil.isBlank(userAgentStr)) {
            return UNKNOWN;
        }
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        if (userAgent == null || userAgent.getBrowser() == null) {
            return UNKNOWN;
        }
        String browser = userAgent.getBrowser().getName();
        String version = userAgent.getVersion();

        // Edge浏览器特殊处理（基于Chromium的新版Edge）
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
     * 获取操作系统名称（含版本）
     */
    public static String getOs() {
        String userAgentStr = getUserAgent();
        if (StrUtil.isBlank(userAgentStr)) {
            return UNKNOWN;
        }
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        if (userAgent == null || userAgent.getOs() == null) {
            return UNKNOWN;
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

    /**
     * 判断是否为空或unknown
     */
    private static boolean isUnknown(String ip) {
        return StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip);
    }
}
