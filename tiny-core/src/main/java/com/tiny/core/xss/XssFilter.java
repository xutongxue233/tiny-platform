package com.tiny.core.xss;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;

/**
 * XSS过滤器
 */
public class XssFilter implements Filter {

    /**
     * 排除的URL路径
     */
    private List<String> excludeUrls;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public XssFilter() {
    }

    public XssFilter(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 检查是否在排除列表中
        if (isExcluded(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // 使用XSS包装器包装请求
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(httpRequest);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 检查请求是否在排除列表中
     */
    private boolean isExcluded(HttpServletRequest request) {
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

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
}
