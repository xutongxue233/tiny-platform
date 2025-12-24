package com.tiny.core.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * 链路追踪拦截器
 * 为每个请求生成唯一的TraceId，用于日志追踪
 */
public class TraceInterceptor implements HandlerInterceptor {

    /**
     * MDC中TraceId的Key
     */
    public static final String TRACE_ID = "traceId";

    /**
     * 响应头中TraceId的Key
     */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 优先从请求头获取（支持分布式链路传递）
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }

        // 放入MDC
        MDC.put(TRACE_ID, traceId);

        // 放入响应头
        response.setHeader(TRACE_ID_HEADER, traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 清理MDC，防止内存泄漏
        MDC.remove(TRACE_ID);
    }

    /**
     * 生成TraceId
     * 格式：时间戳后6位 + 随机8位字符
     */
    private String generateTraceId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp.substring(timestamp.length() - 6) + uuid.substring(0, 8);
    }
}