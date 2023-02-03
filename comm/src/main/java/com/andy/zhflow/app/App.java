package com.andy.zhflow.app;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("app")
public class App extends BaseEntity {
    private static AppMapper appMapper;

    @Autowired
    public void setAppMapper(AppMapper mapper){
        appMapper =mapper;
    }

    private String name;

    public static String save(String id,String name) throws Exception {
        if(StringUtils.isEmpty(name)){
            throw new Exception("缺少应用名称");
        }

        App app=new App();
        if(StringUtils.isNoneEmpty(id)){
            app=appMapper.selectById(id);
            if(app==null){
                throw new Exception("未查到id数据");
            }
        }

        app.setBase(true);
        app.setName(name);

        boolean exists = appMapper.exists(new LambdaQueryWrapper<App>().
                eq(App::getName, app.getName())
                .ne(App::getId,app.getId()));
        if(exists){
            throw new Exception("应用名称已存在");
        }

        if(app.getIsNew()){
            appMapper.insert(app);
        }
        else{
            appMapper.updateById(app);
        }

        return app.getId();
    }

    public static IPage<App> selectPage(Integer page, Integer perPage){
        LambdaQueryWrapper<App> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(App::getCreateTime);
        Page<App> appPage = appMapper.selectPage(new Page<>(page, perPage), lambdaQueryWrapper);
        return appPage;
    }

    public static void del(String id){
        appMapper.deleteById(id);
    }
}
