package com.example.moneyapp2.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;
import java.util.Objects;

@Configuration
public class MaintenanceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {


        LocalTime now = LocalTime.now();

        if (isBetweenMidnightAnd10PastMidnight(now)) {

            ResponseEntity<String> responseEntity = new ResponseEntity<>(
                    "Service is currently under maintenance", HttpStatus.SERVICE_UNAVAILABLE);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));

            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private static boolean isBetweenMidnightAnd10PastMidnight(LocalTime now) {
        return now.isAfter(LocalTime.of(0, 0)) &&
                now.isBefore(LocalTime.of(0, 10));
    }
}
