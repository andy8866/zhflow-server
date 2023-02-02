package com.andy.zhflow.htmlpage;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("html_page")
public class HtmlPage extends BaseEntity {
    private static HtmlPageMapper htmlPageMapper;
    @Autowired
    public void setHtmlPageMapper(HtmlPageMapper mapper){
        htmlPageMapper =mapper;
    }

    private String name;
    /**
     * 页面json内容
     */
    private String content;

    public static HtmlPage getByName(String name){
        LambdaQueryWrapper<HtmlPage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(HtmlPage::getName,name);
        return htmlPageMapper.selectOne(queryWrapper);
    }
}
