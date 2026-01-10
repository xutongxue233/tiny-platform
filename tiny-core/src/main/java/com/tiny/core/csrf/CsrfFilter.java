package com.tiny.core.csrf;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * CSRF防护过滤器
 * 通过验证Origin和Referer头来防止跨站���求伪造
 */
public class CsrfFilter implements Filter {

    private final CsrfProperties properties;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public CsrfFilter(CsrfProperties properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 只检查修改数据的请求方法
        String method = httpRequest.getMethod();
        if (isSafeMethod(method)) {
            chain.doFilter(request, response);
            return;
        }

        // 检查是否在排除列表中
        if (isExcluded(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // 验证请求来源
        if (!validateOrigin(httpRequest)) {
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write("{\"code\":403,\"message\":\"CSRF验证失败：非法的请求来源\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 判断是否为安全的HTTP方法（不修改数据）
     */
    private boolean isSafeMethod(String method) {
        return HttpMethod.GET.name().equalsIgnoreCase(method)
                || HttpMethod.HEAD.name().equalsIgnoreCase(method)
                || HttpMethod.OPTIONS.name().equalsIgnoreCase(method)
                || HttpMethod.TRACE.name().equalsIgnoreCase(method);
    }

    /**
     * 检查请求是否在排除列表中
     */
    private boolean isExcluded(HttpServletRequest request) {
        List<String> excludeUrls = properties.getExcludeUrls();
        if (excludeUrls == null || excludeUrls.isEmpty()) {
            return false;
        }
        String requestUri = request.getRequestURI();
        for (String pattern : excludeUrls) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证请求来源
     */
    private boolean validateOrigin(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        // 如果Origin和Referer都为空，可能是同源请求或API调用
        if (StrUtil.isBlank(origin) && StrUtil.isBlank(referer)) {
            // 对于API调用（如Postman），允许通过
            return true;
        }

        // 获取当前请求的主机
        String serverHost = getHost(request.getRequestURL().toString());

        // 检查Origin头
        if (properties.isCheckOrigin() && StrUtil.isNotBlank(origin)) {
            if (!isAllowedOrigin(origin, serverHost)) {
                return false;
            }
        }

        // 检查Referer头
        if (properties.isCheckReferer() && StrUtil.isNotBlank(referer)) {
            String refererHost = getHost(referer);
            if (!isAllowedOrigin(refererHost, serverHost)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查来源是否被允许
     */
    private boolean isAllowedOrigin(String origin, String serverHost) {
        // 同源请求
        String originHost = getHost(origin);
        if (originHost.equalsIgnoreCase(serverHost)) {
            return true;
        }

        // 检查配置的允许来源
        List<String> allowedOrigins = properties.getAllowedOrigins();
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            for (String allowed : allowedOrigins) {
                if (matchOrigin(originHost, allowed)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 匹配来源（支持通配符）
     */
    private boolean matchOrigin(String origin, String pattern) {
        if ("*".equals(pattern)) {
            return true;
        }
        if (pattern.startsWith("*.")) {
            // 通配符匹配子域名
            String domain = pattern.substring(2);
            return origin.endsWith(domain) || origin.equals(domain.substring(1));
        }
        return origin.equalsIgnoreCase(pattern);
    }

    /**
     * 从URL中提取主机名
     */
    private String getHost(String url) {
        if (StrUtil.isBlank(url)) {
            return "";
        }
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            int port = uri.getPort();
            if (port > 0 && port != 80 && port != 443) {
                return host + ":" + port;
            }
            return host != null ? host : "";
        } catch (URISyntaxException e) {
            return "";
        }
    }
}
