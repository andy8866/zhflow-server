package com.andy.zhflow.admin.handler;

import com.andy.zhflow.exception.ErrMsgException;
import com.andy.zhflow.response.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ResultHandler {
    @ExceptionHandler(value = ErrMsgException.class)
    @ResponseBody
    public ResultResponse<Object> errMsgException(HttpServletRequest req, HttpServletResponse res, ErrMsgException e){
        return  new ResultResponse<>(1,e.getMessage(),null);
    }
}
