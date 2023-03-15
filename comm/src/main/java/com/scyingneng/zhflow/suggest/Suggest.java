package com.scyingneng.zhflow.suggest;

import com.scyingneng.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 建议
 */
@Data
@Component
@TableName("suggest")
public class Suggest extends BaseEntity {

    private static SuggestMapper suggestMapper;

    @Autowired
    public void setSuggestMapper(SuggestMapper mapper){
        suggestMapper =mapper;
    }

    private String name;
    private String companyName;
    private String phone;
    private String email;
    private String content;
    private String ip;

    public String save(){
        setBase(true);
        suggestMapper.insert(this);

        return getId();
    }

    public static IPage<Suggest> selectPage(Integer page,Integer perPage){
        LambdaQueryWrapper<Suggest> lambdaQueryWrapper=new LambdaQueryWrapper<Suggest>().orderByDesc(Suggest::getCreateTime);
        Page<Suggest> suggestPage = suggestMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return suggestPage;
    }

    public static void del(String id){
        suggestMapper.deleteById(id);
    }
}
