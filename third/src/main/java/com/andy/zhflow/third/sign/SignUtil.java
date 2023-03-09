package com.andy.zhflow.third.sign;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public class SignUtil {

    //时间戳
    public static final String TIMESTAMP_KEY = "timeStamp";

    //随机字符串
    public static final String RAND_KEY = "randStr";

    //签名值
    public static final String SIGN_KEY = "sign";

    //过期时间，15分钟
    public static final Long EXPIRE_TIME = 15 * 60L;

    public static String mapToStr(Map<String, Object> map,boolean containSignKey){
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if ((containSignKey || !SIGN_KEY.equals(entry.getKey())) && ObjectUtils.isNotEmpty(entry.getValue())) {
                buf.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return buf.substring(1);
    }

    //加签
    public static String sign(TreeMap<String, Object> map, String key) {
        if (!map.containsKey(TIMESTAMP_KEY)) {
            map.put(TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis() / 1000));
        }
        if (!map.containsKey(RAND_KEY)) {
            map.put(RAND_KEY, NanoIdUtils.randomNanoId());
        }
        String preSign=mapToStr(map,false)+ "&key=" + key;
        String sign = MD5.create().digestHex(preSign).toUpperCase();
        if (!map.containsKey(SIGN_KEY)) {
            map.put(SIGN_KEY, sign);
        }
        return sign;
    }

    //验签
    public static void verify(TreeMap<String, Object> map, String key) throws Exception {
        if (ObjectUtils.isEmpty(map.get(TIMESTAMP_KEY))
                || ObjectUtils.isEmpty(map.get(RAND_KEY))
                || ObjectUtils.isEmpty(map.get(SIGN_KEY))) {
            throw new Exception("签名必填参数为空");
        }

        long timeStamp = Long.valueOf((String) map.get(TIMESTAMP_KEY));
        long expireTime = timeStamp + EXPIRE_TIME;
        if (System.currentTimeMillis() / 1000 > expireTime) {
            throw new Exception("请求已过期");
        }
        String sign = sign(map, key);
        if (!Objects.equals(sign, map.get(SIGN_KEY))) {
            throw new Exception("签名错误");
        }
    }

    public static TreeMap<String,Object> jsonToTreeMap(String str){
        Map map = JSON.parseObject(str,Map.class);
        return new TreeMap<>(map);
    }
}
