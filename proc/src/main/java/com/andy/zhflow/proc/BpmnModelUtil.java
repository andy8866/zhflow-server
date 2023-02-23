package com.andy.zhflow.proc;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.util.*;

public class BpmnModelUtil {
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
}
