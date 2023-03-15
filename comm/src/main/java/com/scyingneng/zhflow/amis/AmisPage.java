package com.scyingneng.zhflow.amis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class AmisPage<T> {
    private List<T> items;

    private Long total;

    public static <T> AmisPage<T> transitionPage(IPage<T> page){
        AmisPage<T> amisPage=new AmisPage<>();
        amisPage.setItems(page.getRecords());
        amisPage.setTotal(page.getTotal());
        return amisPage;
    }

    public static <T> AmisPage<T> transitionPage(List<T> list,Long total){
        AmisPage<T> amisPage=new AmisPage<>();
        amisPage.setItems(list);
        amisPage.setTotal(total);
        return amisPage;
    }
}
