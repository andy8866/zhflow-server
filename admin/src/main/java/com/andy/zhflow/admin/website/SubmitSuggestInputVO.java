package com.andy.zhflow.admin.website;

import lombok.Data;

/**
 * 添加建议
 */
@Data
public class SubmitSuggestInputVO {
    private String companyName;
    private String phone;
    private String email;
    private String content;
}
