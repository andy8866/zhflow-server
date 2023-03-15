package com.scyingneng.zhflow.proc.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工作流节点元素视图对象
 */
@Data
public class ProcNodeVO implements Serializable {
    /**
     * 流程ID
     */
    private String procDefId;
    /**
     * 活动ID
     */
    private String activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 活动类型
     */
    private String activityType;
    /**
     * 活动耗时
     */
    private String duration;
    /**
     * 执行人Id
     */
    private String assigneeId;
    /**
     * 执行人名称
     */
    private String assigneeName;
    /**
     * 候选执行人
     */
    private String candidate;
    private String candidateName;
    /**
     * 任务意见
     */
    private List<ProcCommentOutVO> commentList;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
