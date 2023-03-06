package com.andy.zhflow.proc.model;

import com.andy.zhflow.third.ThirdBaseVO;
import lombok.Data;

@Data
public class ModelInputVO extends ThirdBaseVO {
    private String id;
    private String name;
    private String content;
    private String procKey;
    private String type;
}
