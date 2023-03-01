package com.andy.zhflow.proc.copy;

import com.andy.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("proc_copy")
public class Copy extends BaseEntity {
    private static CopyMapper copyMapper;
    @Autowired
    public void setCopyMapper(CopyMapper mapper){
        copyMapper =mapper;
    }

    private String title;

    private String taskId;
    private String taskName;

    private String procInsId;
    private String procName;

    private String userId;
    private String userName;

    private String originatorId;
    private String originatorName;

    public String save(){
        setBase(true);
        copyMapper.insert(this);

        return getId();
    }
}
