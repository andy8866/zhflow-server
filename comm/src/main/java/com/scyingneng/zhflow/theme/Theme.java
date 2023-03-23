package com.scyingneng.zhflow.theme;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.scyingneng.zhflow.entity.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 主题
 */
@Data
@Component
@TableName("theme")
public class Theme extends BaseEntity {

    private static ThemeMapper themeMapper;


    @Autowired
    public void setThemeMapper(ThemeMapper mapper){
        themeMapper =mapper;
    }

    private String name;

    private Boolean used;

    private String content;

    public static String save(ThemeInputVO inputVO){
        Theme item =new Theme();

        if(StringUtils.isNotEmpty(inputVO.getId())) item = themeMapper.selectById(inputVO.getId());

        item.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName()))  item.setName(inputVO.getName());

        if(item.getIsNew()){
            themeMapper.insert(item);
        }
        else{
            themeMapper.updateById(item);
        }

        return item.getId();
    }

    public static void saveEdit(String id, Map<String, Object> map) {
        Theme theme= themeMapper.selectById(id);

        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();){
            Map.Entry<String, Object> item = it.next();
            Object v=item.getValue();
            if(v instanceof String && StringUtils.isEmpty((String)v)) {
                it.remove();
            }
        }

        String str= JSON.toJSONString(map);
        theme.setContent(str);

        themeMapper.updateById(theme);
    }

    public static Map<String, Object> getEdit(String id) {
        Theme theme= themeMapper.selectById(id);
        if(StringUtils.isEmpty(theme.getContent())) return new HashMap<>();

        return (Map<String, Object>) JSON.parse(theme.getContent());
    }

    public static List<Theme> getList(){
        LambdaQueryWrapper<Theme> lambdaQueryWrapper=new LambdaQueryWrapper<Theme>().orderByDesc(Theme::getUpdateTime);
        List<Theme> liset = themeMapper.selectList(lambdaQueryWrapper);
        return liset;
    }

    public static void del(String id){
        themeMapper.deleteById(id);
    }

    public static void use(String id) {
        LambdaQueryWrapper<Theme> lambdaQueryWrapper=new LambdaQueryWrapper<Theme>();

        Theme theme=new Theme();
        theme.setUsed(false);
        themeMapper.update(theme,lambdaQueryWrapper);

        if(StringUtils.isNotEmpty(id)){
            theme.setId(id);
            theme.setUsed(true);
            themeMapper.updateById(theme);
        }
    }


    public static Theme getUse() {
        LambdaQueryWrapper<Theme> lambdaQueryWrapper=new LambdaQueryWrapper<Theme>().eq(Theme::getUsed,true);
        return themeMapper.selectOne(lambdaQueryWrapper);
    }

}
