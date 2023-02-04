package com.andy.zhflow.security.config;

import io.jsonwebtoken.JwtException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class MyErrorController extends BasicErrorController {

    private ErrorAttributes errorAttributes;

    public MyErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes,errorProperties,errorViewResolvers);
        this.errorAttributes=errorAttributes;
    }

    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Throwable error = this.errorAttributes.getError(new ServletWebRequest(request));
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        body.put("msg",body.remove("message"));
        body.put("status",1);

        if(error instanceof JwtException ||
            error instanceof AuthenticationException){
            body.put("status",2);
        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
