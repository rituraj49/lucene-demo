package cl.lcd.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        // Log basic request details
        logger.info("Request URI: {} | Method: {} | Start Time: {}", request.getRequestURI(), request.getMethod(), startTime);

        // Add timing and other details to MDC for correlation across logs
        MDC.put("requestStartTime", String.valueOf(startTime));
        MDC.put("requestUri", request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

       logger.info(" Response Status : {}  ",response.getStatus());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long endTime = System.currentTimeMillis();
        long duration = endTime - Long.parseLong(MDC.get("requestStartTime"));

        // Log response details and time taken
        logger.info("Response Status: {} | Request URI: {} | Duration: {} ms", response.getStatus(), request.getRequestURI(), duration);

        // Clear MDC context after logging
        MDC.clear();
    }
}

