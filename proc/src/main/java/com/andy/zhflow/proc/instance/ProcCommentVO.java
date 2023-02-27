package com.andy.zhflow.proc.instance;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    private String fullMessage;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public static ProcCommentVO convert(Comment comment){
        return new ProcCommentVO(comment.getId(),
                comment.getTaskId(),
                comment.getUserId(),
                comment.getFullMessage(),
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
