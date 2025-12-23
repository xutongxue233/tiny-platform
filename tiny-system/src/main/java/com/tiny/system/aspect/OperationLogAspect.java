package com.tiny.system.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson2.JSON;
import com.tiny.common.annotation.OperationLog;
import com.tiny.common.utils.IpUtil;
import com.tiny.common.utils.WebUtil;
import com.tiny.core.security.LoginUser;
import com.tiny.core.utils.LoginUserUtil;
import com.tiny.system.entity.SysOperationLog;
import com.tiny.system.service.SysOperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 操作日志切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final SysOperationLogService operationLogService;

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    private static final ThreadLocal<RequestInfo> REQUEST_INFO = new ThreadLocal<>();

    /**
     * 请求信息封装类
     */
    private static class RequestInfo {
        String ipAddr;
        String userAgent;
        String requestMethod;
        String requestUrl;

        RequestInfo(String ipAddr, String userAgent, String requestMethod, String requestUrl) {
            this.ipAddr = ipAddr;
            this.userAgent = userAgent;
            this.requestMethod = requestMethod;
            this.requestUrl = requestUrl;
        }
    }

    @Before("@annotation(operationLog)")
    public void doBefore(JoinPoint joinPoint, OperationLog operationLog) {
        START_TIME.set(System.currentTimeMillis());
        // 在主线程中提前获取请求信息
        HttpServletRequest request = WebUtil.getRequest();
        String ipAddr = WebUtil.getIpAddr();
        String userAgent = WebUtil.getUserAgent();
        String requestMethod = request != null ? request.getMethod() : "";
        String requestUrl = request != null ? request.getRequestURI() : "";
        REQUEST_INFO.set(new RequestInfo(ipAddr, userAgent, requestMethod, requestUrl));
    }

    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        handleLog(joinPoint, operationLog, null, result);
    }

    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLog operationLog, Exception e) {
        handleLog(joinPoint, operationLog, e, null);
    }

    private void handleLog(JoinPoint joinPoint, OperationLog operationLogAnnotation, Exception e, Object result) {
        try {
            SysOperationLog operationLog = new SysOperationLog();

            // 设置操作用户信息
            LoginUser loginUser = LoginUserUtil.getLoginUser();
            if (loginUser != null) {
                operationLog.setUserId(loginUser.getUserId());
                operationLog.setUsername(loginUser.getUsername());
            }

            // 设置模块和操作信息
            operationLog.setModuleName(operationLogAnnotation.module());
            operationLog.setOperationType(operationLogAnnotation.type().name());
            operationLog.setOperationDesc(operationLogAnnotation.desc());

            // 从ThreadLocal获取请求信息
            RequestInfo requestInfo = REQUEST_INFO.get();
            if (requestInfo != null) {
                operationLog.setRequestMethod(requestInfo.requestMethod);
                operationLog.setRequestUrl(requestInfo.requestUrl);
                operationLog.setIpAddr(requestInfo.ipAddr);
                operationLog.setOperationLocation(IpUtil.getRegion(requestInfo.ipAddr));
                operationLog.setBrowser(parseBrowser(requestInfo.userAgent));
                operationLog.setOs(parseOs(requestInfo.userAgent));
            }

            // 设置方法信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            operationLog.setMethodName(className + "." + methodName + "()");

            // 设置请求参数
            if (operationLogAnnotation.saveRequestParam()) {
                String params = getRequestParams(joinPoint, operationLogAnnotation.excludeParams());
                operationLog.setRequestParam(truncate(params, 2000));
            }

            // 设置响应结果
            if (operationLogAnnotation.saveResponseResult() && result != null) {
                String resultJson = JSON.toJSONString(result);
                operationLog.setResponseResult(truncate(resultJson, 2000));
            }

            // 设置操作状态
            if (e != null) {
                operationLog.setStatus("1");
                operationLog.setErrorMsg(truncate(e.getMessage(), 2000));
            } else {
                operationLog.setStatus("0");
            }

            // 设置执行时间
            Long startTime = START_TIME.get();
            if (startTime != null) {
                operationLog.setExecutionTime(System.currentTimeMillis() - startTime);
            }

            operationLog.setOperationTime(LocalDateTime.now());

            // 异步保存日志
            operationLogService.recordOperationLog(operationLog);
        } catch (Exception ex) {
            log.error("记录操作日志异常", ex);
        } finally {
            START_TIME.remove();
            REQUEST_INFO.remove();
        }
    }

    /**
     * 解析浏览器信息
     */
    private String parseBrowser(String userAgentStr) {
        if (StrUtil.isBlank(userAgentStr)) {
            return "unknown";
        }
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
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
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
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

    /**
     * 获取请求参数
     */
    private String getRequestParams(JoinPoint joinPoint, String[] excludeParams) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        if (parameterNames == null || args == null) {
            return "";
        }

        Set<String> excludeSet = Arrays.stream(excludeParams)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            String paramName = parameterNames[i];
            Object paramValue = args[i];

            // 排除敏感参数
            if (excludeSet.contains(paramName.toLowerCase())) {
                continue;
            }

            // 排除不可序列化的参数
            if (paramValue instanceof HttpServletRequest
                    || paramValue instanceof HttpServletResponse
                    || paramValue instanceof MultipartFile) {
                continue;
            }

            // 处理MultipartFile数组
            if (paramValue instanceof MultipartFile[]) {
                continue;
            }

            params.put(paramName, paramValue);
        }

        return JSON.toJSONString(params);
    }

    /**
     * 截断字符串
     */
    private String truncate(String str, int maxLength) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength);
    }
}
