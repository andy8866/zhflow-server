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

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}