package com.andy.zhflow.proc.ui;

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
@TableName("proc_ui")
public class Ui extends BaseEntity {
    private static UiMapper uiMapper;



    @Autowired
    public void setUiMapper(UiMapper mapper){
        uiMapper =mapper;
    }

    private String name;
    private String code;
    private String content;

    public static String save(UiInputVO inputVO) throws Exception {
        Ui ui =new Ui();
        if(StringUtils.isNotEmpty(inputVO.getId())) ui = uiMapper.selectById(inputVO.getId());

        ui.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) ui.setName(inputVO.getName());
        if(StringUtils.isNotEmpty(inputVO.getCode())) ui.setCode(inputVO.getCode());
        if(StringUtils.isNotEmpty(inputVO.getContent())) ui.setContent(inputVO.getContent());

        if(ui.getIsNew()){
            uiMapper.insert(ui);
        }
        else{
            uiMapper.updateById(ui);
        }

        return ui.getId();
    }

    public static Ui getById(String id) {
        return uiMapper.selectById(id);
    }

    public static List<Ui> getCompList(String name) {
        LambdaQueryWrapper<Ui> wrapper=new LambdaQueryWrapper<Ui>().orderByDesc(Ui::getCreateTime).isNotNull(Ui::getCode);
        if(StringUtils.isNotEmpty(name)) wrapper.like(Ui::getName,name);
        return uiMapper.selectList(wrapper);
    }


    public static Ui getByCode(String code) {
        LambdaQueryWrapper<Ui> wrapper=new LambdaQueryWrapper<Ui>().eq(Ui::getCode,code);
        return uiMapper.selectOne(wrapper);
    }

    public static void deleteById(String id) {
        uiMapper.deleteById(id);
    }
}
