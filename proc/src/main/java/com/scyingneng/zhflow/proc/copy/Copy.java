package com.scyingneng.zhflow.proc.copy;

import com.scyingneng.zhflow.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@TableName("proc_copy")
public class Copy extends BaseEntity {
    private static CopyMapper copyMapper;
    @Autowired
    public void setCopyMapper(CopyMapper mapper){
        copyMapper =mapper;
    }

    private String appId;

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

    public static List<Copy> getList(String appId,String userId) {
        LambdaQueryWrapper<Copy> wrapper=new LambdaQueryWrapper<Copy>().eq(Copy::getAppId,appId).orderByDesc(Copy::getUpdateTime);
        if(StringUtils.isNotEmpty(userId)) wrapper.eq(Copy::getUserId,userId);
        return copyMapper.selectList(wrapper);
    }
}
