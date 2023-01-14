package com.andy.zhflow.security.response;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

public class ResponseUtil {
    public static void writeObject(HttpServletResponse response, Object obj){
        String json = JSON.toJSONString(obj);
        writeString(response,json);
    }
    public static void writeString(HttpServletResponse response, String str){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            out.append(str);
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (out != null){
                out.close();
            }
        }
    }
}
