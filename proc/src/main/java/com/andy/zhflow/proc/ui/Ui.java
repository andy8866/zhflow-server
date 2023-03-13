package com.andy.zhflow.proc.ui;

import com.andy.zhflow.entity.AppEntity;
import com.andy.zhflow.proc.model.Model;
import com.andy.zhflow.security.utils.AuthService;
import com.andy.zhflow.service.security.IAuthService;
import com.andy.zhflow.third.app.App;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@TableName("proc_ui")
public class Ui extends AppEntity {
    private static UiMapper uiMapper;

    @Autowired
    public void setUiMapper(UiMapper mapper){
        uiMapper =mapper;
    }

    private static IAuthService authService;
    @Autowired
    public void setIAuthService(IAuthService authService){
        Ui.authService =authService;
    }

    private String name;
    private String content;

    public static String save(UiInputVO inputVO) throws Exception {
        boolean find=false;

        Ui ui =new Ui();
        if(StringUtils.isNotEmpty(inputVO.getId())) {
            ui = uiMapper.selectById(inputVO.getId());
            find=true;

            if(ui==null){
                ui=new Ui();
                ui.setId(inputVO.getId());
                find=false;
            }
        }

        ui.setBase(true);

        if(StringUtils.isNotEmpty(inputVO.getName())) ui.setName(inputVO.getName());
        if(StringUtils.isNotEmpty(inputVO.getContent())) ui.setContent(inputVO.getContent());

        if(StringUtils.isNotEmpty(authService.getAppId())) {
            ui.setAppId(authService.getAppId());
            ui.setAppName(App.getName(authService.getAppId()));
        }
        if(StringUtils.isNotEmpty(authService.getUserId())) ui.setCreateUserId(authService.getUserId());


        if(ui.getIsNew() || !find){
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
        LambdaQueryWrapper<Ui> wrapper=new LambdaQueryWrapper<Ui>().eq(Ui::getAppId, authService.getAppId())
                .orderByDesc(Ui::getUpdateTime);
        if(StringUtils.isNotEmpty(name)) wrapper.like(Ui::getName,name);
        return uiMapper.selectList(wrapper);
    }


    public static List<UiSelectOutVO> getListToSelect(String name) {
        List<Ui> list =getCompList(name);

        List<UiSelectOutVO> outList=new ArrayList<>();
        for (Ui item:list){
            outList.add(new UiSelectOutVO(item.getId(),item.getName()));
        }
        return outList;
    }

    public static void deleteById(String id) {
        uiMapper.deleteById(id);
    }
}
