package com.scyingneng.zhflow.proc;

import org.apache.commons.io.IOUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class BpmnUtil {
    public static List<FlowNode> getFlowList(BpmnModelInstance bpmnModelInstance, ModelElementType type){
        Map<String,Boolean> checkedIdMap=new HashMap<>();
        List<FlowNode> list=new ArrayList<>();

        Collection<StartEvent> startEvents = bpmnModelInstance.getModelElementsByType(StartEvent.class);
        for (StartEvent startEvent:startEvents){
            checkFlowList(startEvent,type,list,checkedIdMap);
        }

        return list;
    }

    private static void checkFlowList(FlowNode flowNode,ModelElementType type,List<FlowNode> list, Map<String,Boolean> checkedIdMap){
        if(checkedIdMap.containsKey(flowNode.getId())) return ;

        if(Objects.equals(flowNode.getElementType().getTypeName(), type.getTypeName())){
            list.add(flowNode);
            checkedIdMap.put(flowNode.getId(),true);
        }

        for (SequenceFlow sequenceFlow:flowNode.getOutgoing()){
            checkFlowList(sequenceFlow.getTarget(),type,list,checkedIdMap);
        }
    }

    /**
     * 深搜递归获取流程未通过的节点
     * @param bpmnModel 流程模型
     * @param unfinishedTaskSet 未结束的任务节点
     * @param finishedSequenceFlowSet 已经完成的连线
     * @param finishedTaskSet 已完成的任务节点
     * @return
     */
    public static Set<String> dfsFindRejects(BpmnModelInstance bpmnModel, Set<String> unfinishedTaskSet,
                                             Set<String> finishedSequenceFlowSet, Set<String> finishedTaskSet) {

        List<Process> processes = bpmnModel.getModelElementsByType(Process.class).stream().collect(Collectors.toList());

        Collection<FlowElement> allElements = getAllElements(processes.get(0).getFlowElements(), null);
        Set<String> rejectedSet = new HashSet<>();
        for (FlowElement flowElement : allElements) {
            // 用户节点且未结束元素
            if (flowElement instanceof UserTask && unfinishedTaskSet.contains(flowElement.getId())) {
                List<String> hasSequenceFlow = iteratorFindFinishes(flowElement, null);
                List<String> rejects = iteratorFindRejects(flowElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, null);
                rejectedSet.addAll(rejects);
            }
        }
        return rejectedSet;
    }

    public static Collection<FlowElement> getAllElements(Collection<FlowElement> flowElements, Collection<FlowElement> allElements) {
        allElements = allElements == null ? new ArrayList<>() : allElements;

        for (FlowElement flowElement : flowElements) {
            allElements.add(flowElement);
            if (flowElement instanceof SubProcess) {
                // 继续深入子流程，进一步获取子流程
                allElements = getAllElements(((SubProcess) flowElement).getFlowElements(), allElements);
            }
        }
        return allElements;
    }

    /**
     * 迭代获取父级节点列表，向前找
     * @param source 起始节点
     * @param hasSequenceFlow 已经经过的连线的ID，用于判断线路是否重复
     * @return
     */
    public static List<String> iteratorFindFinishes(FlowElement source, List<String> hasSequenceFlow) {
        hasSequenceFlow = hasSequenceFlow == null ? new ArrayList<>() : hasSequenceFlow;

        // 根据类型，获取入口连线
        List<SequenceFlow> sequenceFlows = getElementIncomingFlows(source);

        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow: sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                FlowElement finishedElement = sequenceFlow.getSource();
                // 类型为子流程，则添加子流程开始节点出口处相连的节点
                if (finishedElement instanceof SubProcess) {
                    FlowElement firstElement = (StartEvent) ((SubProcess) finishedElement).getFlowElements().toArray()[0];
                    // 获取子流程的连线
                    hasSequenceFlow.addAll(iteratorFindFinishes(firstElement, null));
                }
                // 继续迭代
                hasSequenceFlow = iteratorFindFinishes(finishedElement, hasSequenceFlow);
            }
        }
        return hasSequenceFlow;
    }

    /**
     * 根据正在运行的任务节点，迭代获取子级任务节点列表，向后找
     * @param source 起始节点
     * @param finishedSequenceFlowSet 已经完成的连线
     * @param finishedTaskSet 已经完成的任务节点
     * @param hasSequenceFlow 已经经过的连线的 ID，用于判断线路是否重复
     * @param rejectedList 未通过的元素
     * @return
     */
    public static List<String> iteratorFindRejects(FlowElement source, Set<String> finishedSequenceFlowSet, Set<String> finishedTaskSet
            , List<String> hasSequenceFlow, List<String> rejectedList) {
        hasSequenceFlow = hasSequenceFlow == null ? new ArrayList<>() : hasSequenceFlow;
        rejectedList = rejectedList == null ? new ArrayList<>() : rejectedList;

        // 根据类型，获取出口连线
        List<SequenceFlow> sequenceFlows = getElementOutgoingFlows(source);

        if (sequenceFlows != null) {
            // 循环找到目标元素
            for (SequenceFlow sequenceFlow: sequenceFlows) {
                // 如果发现连线重复，说明循环了，跳过这个循环
                if (hasSequenceFlow.contains(sequenceFlow.getId())) {
                    continue;
                }
                // 添加已经走过的连线
                hasSequenceFlow.add(sequenceFlow.getId());
                FlowElement targetElement = sequenceFlow.getTarget();
                // 添加未完成的节点
                if (finishedTaskSet.contains(targetElement.getId())) {
                    rejectedList.add(targetElement.getId());
                }
                // 添加未完成的连线
                if (finishedSequenceFlowSet.contains(sequenceFlow.getId())) {
                    rejectedList.add(sequenceFlow.getId());
                }
                // 如果节点为子流程节点情况，则从节点中的第一个节点开始获取
                if (targetElement instanceof SubProcess) {
                    FlowElement firstElement = (FlowElement) (((SubProcess) targetElement).getFlowElements().toArray()[0]);
                    List<String> childList = iteratorFindRejects(firstElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, null);
                    // 如果找到节点，则说明该线路找到节点，不继续向下找，反之继续
                    if (childList != null && childList.size() > 0) {
                        rejectedList.addAll(childList);
                        continue;
                    }
                }
                // 继续迭代
                rejectedList = iteratorFindRejects(targetElement, finishedSequenceFlowSet, finishedTaskSet, hasSequenceFlow, rejectedList);
            }
        }
        return rejectedList;
    }

    /**
     * 根据节点，获取入口连线
     * @param source
     * @return
     */
    public static List<SequenceFlow> getElementIncomingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getIncoming().stream().collect(Collectors.toList());
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getIncoming().stream().collect(Collectors.toList());
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getIncoming().stream().collect(Collectors.toList());
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getIncoming().stream().collect(Collectors.toList());
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getIncoming().stream().collect(Collectors.toList());
        }
        return sequenceFlows;
    }

    /**
     * 根据节点，获取出口连线
     * @param source
     * @return
     */
    public static List<SequenceFlow> getElementOutgoingFlows(FlowElement source) {
        List<SequenceFlow> sequenceFlows = null;
        if (source instanceof FlowNode) {
            sequenceFlows = ((FlowNode) source).getOutgoing().stream().collect(Collectors.toList());
        } else if (source instanceof Gateway) {
            sequenceFlows = ((Gateway) source).getOutgoing().stream().collect(Collectors.toList());
        } else if (source instanceof SubProcess) {
            sequenceFlows = ((SubProcess) source).getOutgoing().stream().collect(Collectors.toList());
        } else if (source instanceof StartEvent) {
            sequenceFlows = ((StartEvent) source).getOutgoing().stream().collect(Collectors.toList());
        } else if (source instanceof EndEvent) {
            sequenceFlows = ((EndEvent) source).getOutgoing().stream().collect(Collectors.toList());
        }
        return sequenceFlows;
    }

    public static EndEvent getEndEvent(BpmnModelInstance bpmnModel) {
        List<Process> processes = bpmnModel.getModelElementsByType(Process.class).stream().collect(Collectors.toList());
        return getEndEvent(processes.get(0).getFlowElements());
    }

    /**
     * 获取结束节点
     *
     * @param flowElements 流程元素集合
     * @return 结束节点（未找到开始节点，返回null）
     */
    public static EndEvent getEndEvent(Collection<FlowElement> flowElements) {
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof EndEvent) {
                return (EndEvent) flowElement;
            }
        }
        return null;
    }

    public static String getAttributeValue(ModelElementInstance modelElementInstance, String name){
        return modelElementInstance.getDomElement().getAttribute(BpmnConstant.NAMASPASE_ATTR,name);
    }

    public static String getProcKey(String bpmnContent){
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(IOUtils.toInputStream(bpmnContent, StandardCharsets.UTF_8));
        Collection<Process> collections = bpmnModelInstance.getModelElementsByType(Process.class);
        if(collections.size()>0) return collections.stream().collect(Collectors.toList()).get(0).getId();
        return "";
    }
}
