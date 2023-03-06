package com.andy.zhflow.third.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SignUtil {

    //时间戳
    private static final String TIMESTAMP_KEY = "timeStamp";

    //随机字符串
    private static final String RAND_KEY = "randStr";

    //签名值
    private static final String SIGN_KEY = "sign";

    //过期时间，15分钟
    private static final Long EXPIRE_TIME = 15 * 60L;

    public static String toStr(TreeMap<String, String> map){
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!SIGN_KEY.equals(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                buf.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return buf.substring(1);
    }

    //加签
    public static String sign(TreeMap<String, String> map, String key) {
        if (!map.containsKey(TIMESTAMP_KEY)) {
            map.put(TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis() / 1000));
        }
        if (!map.containsKey(RAND_KEY)) {
            map.put(RAND_KEY, String.valueOf(new Random().nextDouble()));
        }
        String preSign=toStr(map)+ "&key=" + key;
        String sign = MD5.create().digestHex(preSign).toUpperCase();
        if (!map.containsKey(SIGN_KEY)) {
            map.put(SIGN_KEY, sign);
        }
        return sign;
    }

    //验签
    public static void verify(TreeMap<String, String> map, String key) throws Exception {
        if (StrUtil.isBlank(map.get(TIMESTAMP_KEY))
                || StrUtil.isBlank(map.get(RAND_KEY))
                || StrUtil.isBlank(map.get(SIGN_KEY))) {
            throw new Exception("必填参数为空");
        }
        long timeStamp = Long.valueOf(map.get(TIMESTAMP_KEY));
        long expireTime = timeStamp + EXPIRE_TIME;
        if (System.currentTimeMillis() / 1000 > expireTime) {
            throw new Exception("请求已过期");
        }
        String sign = sign(map, key);
        if (!Objects.equals(sign, map.get(SIGN_KEY))) {
            throw new Exception("签名错误");
        }
    }

    public static TreeMap<String,String> jsonToTreeMap(String str){
       return JSON.parseObject(str,new TypeReference<TreeMap<String, String>>() {});
    }
}
