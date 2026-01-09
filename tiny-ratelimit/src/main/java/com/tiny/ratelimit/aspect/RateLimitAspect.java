package com.tiny.ratelimit.aspect;

import cn.hutool.core.util.StrUtil;
import com.tiny.common.exception.BusinessException;
import com.tiny.ratelimit.annotation.RateLimit;
import com.tiny.ratelimit.enums.LimitType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 * 基于Redis的滑动窗口算法实现接口限流
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String RATE_LIMIT_KEY_PREFIX = "rate_limit:";

    private final SpelExpressionParser spelParser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = buildKey(joinPoint, rateLimit);
        String redisKey = RATE_LIMIT_KEY_PREFIX + key;

        long now = System.currentTimeMillis();
        long windowStart = now - rateLimit.window() * 1000L;

        // 移除窗口外的记录
        stringRedisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, windowStart);

        // 获取当前窗口请求数
        Long count = stringRedisTemplate.opsForZSet().zCard(redisKey);

        if (count != null && count >= rateLimit.limit()) {
            log.warn("接口限流触发, key: {}, count: {}, limit: {}", key, count, rateLimit.limit());
            throw new BusinessException(rateLimit.message());
        }

        // 添加当前请求记录
        stringRedisTemplate.opsForZSet().add(redisKey, String.valueOf(now), now);

        // 设置过期时间（窗口时间+1秒的缓冲）
        stringRedisTemplate.expire(redisKey, rateLimit.window() + 1, TimeUnit.SECONDS);

        return joinPoint.proceed();
    }

    /**
     * 构建限流Key
     */
    private String buildKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getDeclaringClass().getName())
                .append(".")
                .append(method.getName());

        switch (rateLimit.type()) {
            case IP:
                keyBuilder.append(":").append(getClientIp());
                break;
            case USER:
                keyBuilder.append(":").append(getCurrentUserId());
                break;
            case CUSTOM:
                if (StrUtil.isNotBlank(rateLimit.key())) {
                    String customKey = parseSpelExpression(rateLimit.key(), joinPoint);
                    keyBuilder.append(":").append(customKey);
                }
                break;
            case GLOBAL:
            default:
                break;
        }

        return keyBuilder.toString();
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }

        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip != null ? ip : "unknown";
    }

    /**
     * 获取当前用户ID
     * 需要配合Sa-Token使用，这里通过反射调用避免强依赖
     */
    private String getCurrentUserId() {
        try {
            Class<?> stpUtilClass = Class.forName("cn.dev33.satoken.stp.StpUtil");
            Method getLoginIdMethod = stpUtilClass.getMethod("getLoginIdDefaultNull");
            Object loginId = getLoginIdMethod.invoke(null);
            return loginId != null ? loginId.toString() : "anonymous";
        } catch (Exception e) {
            return "anonymous";
        }
    }

    /**
     * 解析SpEL表达式
     */
    private String parseSpelExpression(String expressionString, ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

            if (parameterNames == null || parameterNames.length == 0) {
                return expressionString;
            }

            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                ((StandardEvaluationContext) context).setVariable(parameterNames[i], args[i]);
            }

            Expression expression = spelParser.parseExpression(expressionString);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            log.warn("解析SpEL表达式失败: {}", expressionString, e);
            return expressionString;
        }
    }
}
