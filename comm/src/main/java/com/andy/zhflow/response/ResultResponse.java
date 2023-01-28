package com.andy.zhflow.response;

import lombok.Data;

@Data
public class ResultResponse<T> {
    private Integer status;
    private String msg;
    private T data;

    public ResultResponse() {
        super();
    }

    public ResultResponse(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <B> ResultResponse<B> success(B data){
        return new ResultResponse(0,"",data);
    }
    public static <B> ResultResponse<B> fail(String msg,B data){
        return new ResultResponse(1,msg,data);
    }
    public static <B> ResultResponse<B> fail(Integer status,String msg,B data){
        return new ResultResponse(status,msg,data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}