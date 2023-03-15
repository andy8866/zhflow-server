package com.scyingneng.zhflow.admin.handler;

import com.scyingneng.zhflow.response.ResultResponse;
import com.scyingneng.zhflow.exception.ErrMsgException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ResultHandler {
    @ExceptionHandler(value = ErrMsgException.class)
    @ResponseBody
    public ResultResponse<Object> errMsgException(HttpServletRequest req, HttpServletResponse res, ErrMsgException e){
        return  ResultResponse.fail(e.getMessage(),null);
    }
}
