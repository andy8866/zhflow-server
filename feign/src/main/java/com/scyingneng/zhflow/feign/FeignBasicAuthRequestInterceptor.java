package com.scyingneng.zhflow.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 重写后feign转发请求会携带原请求的Head信息
 */
public class FeignBasicAuthRequestInterceptor implements RequestInterceptor {

    final String[] copyHeaders = new String[]{"token"};

    @Override
    public void apply(RequestTemplate requestTemplate) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    if (iscopy(name)) {
                        requestTemplate.header(name, values);
                    }
                }
            }
        }
    }

    private Boolean iscopy(String name) {
        for (String header : copyHeaders) {
            if (header.equals(name)) {
                return true;
            }
        }
        return false;
    }
}