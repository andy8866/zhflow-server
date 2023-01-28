package com.andy.zhflow.page;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("page")
public class Page extends BaseEntity {
    private static PageMapper pageMapper;
    @Autowired
    public void setPageMapper(PageMapper mapper){
        pageMapper =mapper;
    }

    private String name;
    /**
     * 页面json内容
     */
    private String content;

    public static Page getByName(String name){
        LambdaQueryWrapper<Page> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Page::getName,name);
        return pageMapper.selectOne(queryWrapper);
    }
}
