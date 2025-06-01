package com.wazzups.analytics.userapi.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class TimingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(request.getRequestURI());
        long start = System.nanoTime();
        request.setAttribute("startTime", start);
        request.setAttribute("requestTimestamp", Instant.now().toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long endTime = System.nanoTime();
        long startTime = (Long) request.getAttribute("startTime");

        long timeSpent = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        response.addHeader("X-Processing-Time-ms", String.valueOf(timeSpent));
        response.addHeader("X-Request-Timestamp", (String) request.getAttribute("requestTimestamp"));
    }
}
