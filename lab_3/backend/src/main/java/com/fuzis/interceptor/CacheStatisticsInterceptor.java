package com.fuzis.interceptor;

import com.fuzis.annotation.CacheStatisticsLogging;
import com.fuzis.service.CacheStatisticsService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@CacheStatisticsLogging
@Priority(Interceptor.Priority.APPLICATION + 10)
public class CacheStatisticsInterceptor {

    @Inject
    private CacheStatisticsService cacheStatisticsService;

    @AroundInvoke
    public Object logCacheStatistics(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        String className = context.getMethod().getDeclaringClass().getSimpleName();

        Object result = context.proceed();

        cacheStatisticsService.logStatistics(className + "." + methodName);

        return result;
    }
}