package com.andy.zhflow.third.sign;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.TreeMap;

@Data
public class BaseSignVO {
    private String appId;
    private String userId;
    private String timeStamp;
    private String randStr;
    private String sign;

    public TreeMap<String,Object> toTreeMap(){
        String str = JSON.toJSONString(this);
        return SignUtil.jsonToTreeMap(str);
    }
}
