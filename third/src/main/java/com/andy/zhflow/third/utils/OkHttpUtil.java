package com.andy.zhflow.third.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andy.zhflow.third.sign.SignUtil;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
public class OkHttpUtil{
    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    private static OkHttpClient  okHttpClient;

    @Autowired
    public OkHttpUtil(OkHttpClient  okHttpClient) {
        OkHttpUtil.okHttpClient= okHttpClient;
    }

    public static Map<String, Object> doSign(Map<String, Object> params,Map<String, Object> signParams,String key){
        if(params==null) params=new HashMap<>();
        if(signParams==null) signParams=new HashMap<>();

        TreeMap<String,Object> signMap=new TreeMap<>(signParams);

        if (signMap.keySet().size() > 0 && StringUtils.isNotEmpty(key)) {
            SignUtil.sign(signMap,key);
        }

        params.putAll(signMap);

        return params;
    }
    /**
     * get
     * @param url     请求的url
     * @param params 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return
     */
    public static String get(String url, Map<String, Object> params,Map<String, Object> signParams,String key) {
        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);

        params=doSign(params,signParams,key);
        if (params.keySet().size() > 0) {
            sb.append("?").append(SignUtil.mapToStr(params,false));
        }

        Request request = new Request.Builder().url(sb.toString()).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            logger.error("okhttp3 put error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    /**
     * post
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return
     */
    public static String post(String url, Map<String, Object> params,Map<String, Object> signParams,String key) {
        String responseBody = "";
        FormBody.Builder builder = new FormBody.Builder();

        params=doSign(params,signParams,key);
        for (String k : params.keySet()) {
            builder.add(k, params.get(k).toString());
        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            logger.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }

    public static String postJsonParams(String url,Map<String, Object> signParams,String key, Object obj) {
        return postJsonParams(url,signParams,key, JSON.toJSONString(obj));
    }

    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     * 参数一：请求Url
     * 参数二：请求的JSON
     * 参数三：请求回调
     */
    public static String postJsonParams(String url, Map<String, Object> signParams,String key, String jsonParams) {
        String responseBody = "";
        StringBuffer sb = new StringBuffer(url);

        Map<String, Object> params=doSign(new HashMap<>(),signParams,key);
        JSONObject jsonObject=JSON.parseObject(jsonParams);
        jsonObject.putAll(params);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
        Request request = new Request.Builder().url(sb.toString()).post(requestBody).build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            logger.error("okhttp3 post error >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return responseBody;
    }
}