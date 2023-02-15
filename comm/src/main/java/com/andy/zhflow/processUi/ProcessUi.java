package com.andy.zhflow.processUi;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("process_ui")
public class ProcessUi extends BaseEntity {
    private static ProcessUiMapper processUiMapper;


    @Autowired
    public void setProcessUiMapper(ProcessUiMapper mapper){
        processUiMapper =mapper;
    }

    private String content;

    public static String save(String id,String content) throws Exception {
        ProcessUi processUi =new ProcessUi();
        if(StringUtils.isNoneEmpty(id)){
            processUi = processUiMapper.selectById(id);
            if(processUi ==null){
                throw new Exception("未查到id数据");
            }
        }

        processUi.setBase(true);

        if(StringUtils.isNoneEmpty(content)){
            processUi.setContent(content);
        }

        if(processUi.getIsNew()){
            processUiMapper.insert(processUi);
        }
        else{
            processUiMapper.updateById(processUi);
        }

        return processUi.getId();
    }


    public static void del(String id) {
        if(StringUtils.isEmpty(id)){
            return ;
        }

        processUiMapper.deleteById(id);
    }


    public static ProcessUi getById(String id) {
        return processUiMapper.selectById(id);
    }
}
