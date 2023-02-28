package com.andy.zhflow.proc.instance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
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
public class ProcCommentOutVO {
    private String id;
    private String taskId;

    private String userId;

    private String type;

    private String typeName;

    private String fullMessage;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public static ProcCommentOutVO convert(Comment comment){
        String type="";
        String typeName="";
        String fullMessage=comment.getFullMessage();
        if(StringUtils.isNotEmpty(fullMessage)){
            try{
                TaskCommentVO taskCommentVO = JSON.parseObject(comment.getFullMessage(), TaskCommentVO.class);
                type=taskCommentVO.getType();
                typeName=taskCommentVO.getTypeName();
                fullMessage=taskCommentVO.getComment();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ProcCommentOutVO(comment.getId(),
                comment.getTaskId(),
                comment.getUserId(),
                type,
                typeName,
                fullMessage,
                comment.getTime());
    }

    public static List<ProcCommentOutVO> convertList(List<Comment> list){
        List<ProcCommentOutVO> voList=new ArrayList<>();

        for (Comment comment:list){
            voList.add(convert(comment));
        }

        return voList;
    }
}
