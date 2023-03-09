package com.andy.zhflow.proc.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.andy.zhflow.proc.BpmnConstant;
import com.andy.zhflow.proc.FlowCommentType;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class TaskCommentVO {
    private String type;
    private String typeName;
    private String comment;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String toJson(){
        return JSON.toJSONString(this);
    }

    public static TaskCommentVO createComment(FlowCommentType type,
                                              Map<String,Object> inputVO){
        TaskCommentVO taskCommentVO=new TaskCommentVO();
        taskCommentVO.setType(type.getType());
        taskCommentVO.setTypeName(type.getRemark());
        taskCommentVO.setCreateTime(new Date());

        taskCommentVO.comment= (String) inputVO.getOrDefault(BpmnConstant.VAR_COMMENT,"");

        return taskCommentVO;
    }
}
