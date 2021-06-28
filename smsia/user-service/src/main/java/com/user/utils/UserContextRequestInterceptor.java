package com.user.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserContextRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header(UserContext.CORRELATION_ID, UserContextHolder.getContext().getCorrelationId());
        template.header(UserContext.AUTH_TOKEN, UserContextHolder.getContext().getAuthToken());
    }
}
