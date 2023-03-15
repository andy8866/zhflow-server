package com.scyingneng.zhflow.third.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.scyingneng.zhflow.third.app.App;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.TreeMap;

@Slf4j
@WebFilter(urlPatterns = {"/api/serviceThirdApp/*"},filterName = "signFilter")
public class SignFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String appId=null;
        String times=null;
        String randKey=null;
        String sign=null;

        RepeatedlyReadRequestWrapper requestWrapper = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);

        if(StringUtils.isNotEmpty(requestWrapper.getContentType()) && requestWrapper.getContentType().contains("json")){

            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = requestWrapper.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }

            try {
                JSONObject jsonObject = JSON.parseObject(jb.toString());

                appId=jsonObject.getString("appId");
                times=jsonObject.getString(SignUtil.TIMESTAMP_KEY);
                randKey=jsonObject.getString(SignUtil.RAND_KEY);
                sign=jsonObject.getString(SignUtil.SIGN_KEY);

            } catch (JSONException e) {
                throw new IOException("json字符串错误");
            }

        }else{

            appId=requestWrapper.getParameter("appId");
            times=requestWrapper.getParameter(SignUtil.TIMESTAMP_KEY);
            randKey=requestWrapper.getParameter(SignUtil.RAND_KEY);
            sign=requestWrapper.getParameter(SignUtil.SIGN_KEY);
        }

        if(StringUtils.isEmpty(appId)) throw new RuntimeException("缺少appId");

        String key= App.getAppKey(appId);
        if(StringUtils.isEmpty(key)) throw new RuntimeException("appKey未配置");

        TreeMap<String,Object> map=new TreeMap<>();
        if(StringUtils.isNotEmpty(appId)) map.put("appId",appId);
        if(StringUtils.isNotEmpty(times)) map.put(SignUtil.TIMESTAMP_KEY,times);
        if(StringUtils.isNotEmpty(randKey)) map.put(SignUtil.RAND_KEY,randKey);
        if(StringUtils.isNotEmpty(sign)) map.put(SignUtil.SIGN_KEY,sign);

        try {
            SignUtil.verify(map,key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(requestWrapper,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
