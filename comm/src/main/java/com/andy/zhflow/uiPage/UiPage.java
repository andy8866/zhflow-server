package com.andy.zhflow.uiPage;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@TableName("ui_page")
public class UiPage extends BaseEntity {
    private static UiPageMapper uiPageMapper;



    @Autowired
    public void setUiPageMapper(UiPageMapper mapper){
        uiPageMapper =mapper;
    }

    private String code;
    private String name;
    private String content;

    public static String save(UiPageInputVO inputVO) throws Exception {

        UiPage item =new UiPage();
        if(StringUtils.isNotEmpty(inputVO.getId())) item = uiPageMapper.selectById(inputVO.getId());

        item.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getCode())) item.setCode(inputVO.getCode());
        if(StringUtils.isNotEmpty(inputVO.getName())) item.setName(inputVO.getName());
        if(StringUtils.isNotEmpty(inputVO.getContent())) item.setContent(inputVO.getContent());

        if(item.getIsNew()){
            uiPageMapper.insert(item);
        }
        else{
            uiPageMapper.updateById(item);
        }

        return item.getId();
    }

    public static void del(String id){
        uiPageMapper.deleteById(id);
    }
    public static UiPage getById(String id) {
       return uiPageMapper.selectById(id);
    }
    public static List<UiPage> getList() {
        LambdaQueryWrapper<UiPage> wrapper=new LambdaQueryWrapper<UiPage>().orderByDesc(UiPage::getCreateTime);
        return uiPageMapper.selectList(wrapper);
    }

    public static String getUiByCode(String code) {
        LambdaQueryWrapper<UiPage> wrapper=new LambdaQueryWrapper<UiPage>()
                .eq(UiPage::getCode,code)
                .last("limit 1");
        UiPage uiPage = uiPageMapper.selectOne(wrapper);
        if(uiPage!=null) return uiPage.getContent();
        return null;
    }
}
