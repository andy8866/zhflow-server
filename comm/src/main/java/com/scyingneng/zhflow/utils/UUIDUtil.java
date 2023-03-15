package com.scyingneng.zhflow.utils;

import java.util.UUID;

public class UUIDUtil {
    public static String generate(){
        String uuid= UUID.randomUUID().toString().toUpperCase();
        return uuid.replaceAll("-","");
    }
}
