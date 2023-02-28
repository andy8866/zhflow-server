package com.andy.zhflow.proc.instance;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import com.andy.zhflow.proc.task.TaskCommentVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.task.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ProcCommentVO {
    private String id;
    private String taskId;

    private String userId;

    private String type;

    private String typeName;

    private String fullMessage;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public static ProcCommentVO convert(Comment comment){
        String type="";
        String typeName="";
        String fullMessage=comment.getFullMessage();
        if(StringUtils.isNotEmpty(comment.getFullMessage())){
            TaskCommentVO taskCommentVO = JSON.parseObject(comment.getFullMessage(), TaskCommentVO.class);
            if(taskCommentVO!=null){
                type=taskCommentVO.getType();
                typeName=taskCommentVO.getTypeName();
                fullMessage=taskCommentVO.getComment();
            }
        }
        return new ProcCommentVO(comment.getId(),
                comment.getTaskId(),
                comment.getUserId(),
                type,
                typeName,
                fullMessage,
                comment.getTime());
    }

    public static List<ProcCommentVO> convertList(List<Comment> list){
        List<ProcCommentVO> voList=new ArrayList<>();

        for (Comment comment:list){
            voList.add(convert(comment));
        }

        return voList;
    }
}
