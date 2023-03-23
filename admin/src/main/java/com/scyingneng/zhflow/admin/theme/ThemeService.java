package com.scyingneng.zhflow.admin.theme;

import com.scyingneng.zhflow.response.ResponseUtil;
import com.scyingneng.zhflow.theme.Theme;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class ThemeService  {
    public void getCss(HttpServletResponse response, String id) {
        Map<String,Object> map= Theme.getEdit(id);

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(":root{").append(System.lineSeparator());

        append(map,stringBuilder);

        stringBuilder.append("}");

        ResponseUtil.writeString(response,stringBuilder.toString(),"text/css");
    }

    private void append(Map<String,Object> map,StringBuilder stringBuilder){
        map.forEach((key,value)->{
            if("kv".equals(key) || "colorKV".equals(key) ){
                Map<String,Object> kvMap= (Map<String, Object>) value;
                append(kvMap,stringBuilder);
            }
            else{
                stringBuilder.append(key).append(": ").append(value).append(";").append(System.lineSeparator());
            }

        });
    }

    public void getUseCss(HttpServletResponse response) {
        Theme theme=Theme.getUse();
        getCss(response,theme.getId());
    }
}
