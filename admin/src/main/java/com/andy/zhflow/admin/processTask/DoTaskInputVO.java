package com.andy.zhflow.admin.processTask;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Data
public class DoTaskInputVO {
    private String taskId;

    private String opinion;
    private String result;
}
