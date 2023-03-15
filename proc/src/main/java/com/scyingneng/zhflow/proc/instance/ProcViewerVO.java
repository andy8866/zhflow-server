package com.scyingneng.zhflow.proc.instance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * 任务追踪视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcViewerVO {

    private String bpmnXmlData;

    /**
     * 历史流程节点信息
     */
    private List<ProcNodeVO> historyProcNodeList;

    /**
     * 获取流程实例的历史节点（去重）
     */
    private Set<String> finishedTaskSet;

    /**
     * 已完成
     */
    private Set<String> finishedSequenceFlowSet;

    /**
     * 获取流程实例当前正在待办的节点（去重）
     */
    private Set<String> unfinishedTaskSet;

    /**
     * 已拒绝
     */
    private Set<String> rejectedTaskSet;
}
