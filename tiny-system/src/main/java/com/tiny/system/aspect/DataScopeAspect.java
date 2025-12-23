package com.tiny.system.aspect;

import com.tiny.common.annotation.DataScope;
import com.tiny.core.datascope.DataScopeContext;
import com.tiny.core.datascope.DataScopeParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据权限切面
 */
@Aspect
@Order(1)
@Component
public class DataScopeAspect {

    @Around("@annotation(com.tiny.common.annotation.DataScope)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            // 获取注解
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            DataScope dataScope = method.getAnnotation(DataScope.class);

            // 设置数据权限参数
            DataScopeParam param = DataScopeParam.builder()
                    .deptAlias(dataScope.deptAlias())
                    .userAlias(dataScope.userAlias())
                    .deptIdColumn(dataScope.deptIdColumn())
                    .userIdColumn(dataScope.userIdColumn())
                    .build();
            DataScopeContext.set(param);

            // 执行方法
            return point.proceed();
        } finally {
            // 清除上下文
            DataScopeContext.clear();
        }
    }
}
