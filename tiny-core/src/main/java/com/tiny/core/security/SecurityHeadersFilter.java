package com.tiny.core.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 安全响应头过滤器
 * 添加常用的安全响应头，防止常见的Web攻击
 */
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 防止点击劫持
        httpResponse.setHeader("X-Frame-Options", "SAMEORIGIN");

        // 启用XSS过滤器（现代浏览器内置）
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // 防止MIME类型嗅探
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // 控制Referer头的发送
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // 限制浏览器功能
        httpResponse.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=()");

        // 内容安全策略（CSP）- 基础配置
        httpResponse.setHeader("Content-Security-Policy",
                "default-src 'self'; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                "style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data: blob:; " +
                "font-src 'self' data:; " +
                "connect-src 'self'; " +
                "frame-ancestors 'self'");

        chain.doFilter(request, response);
    }
}
