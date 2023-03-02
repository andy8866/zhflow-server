package com.andy.zhflow.proc;

import lombok.Data;

@Data
public class BpmnConstant {

    public static final String NAMASPASE_ATTR = "http://camunda.org/schema/1.0/bpmn";

    public static final String BPMN_CUSTOM_DATA_TYPE = "dataType";

    public static final String PROC_TYPE_APPROVAL = "approval";

    public static final String ATTR_INITIATOR = "initiator";
    public static final String ATTR_INITIATOR_NAME = "initiatorName";
    public static final String ATTR_ASSIGNEE = "assignee";

    public static final String VAR_PROC_CREATE_TIME = "procCreateTime";
    public static final String VAR_COMMENT_TYPE = "commentType";
    public static final String VAR_COMMENT = "comment";
    public static final String VAR_COPY_USER_IDS = "copyUserIds";

    public static final String VAR_MULTI_INSTANCE_LOOP_USER_LIST = "multiInstanceLoopUserList";


    public static final String ELEMENT_SEQUENCE_FLOW = "sequenceFlow";
    public static final String ELEMENT_FLOW_CONDITION = "conditionExpression";
    public static final String ELEMENT_EVENT_START = "startEvent";
    public static final String ELEMENT_EVENT_END = "endEvent";
    public static final String ELEMENT_TASK_USER = "userTask";


    public static final String ATTRIBUTE_FLOW_SOURCE_REF = "sourceRef";
    public static final String ATTRIBUTE_FLOW_TARGET_REF = "targetRef";
    public static final String ATTRIBUTE_FLOW_SKIP_EXPRESSION = "skipExpression";


    /**
     * 角色候选组前缀
     */
    public static final String CANDIDATE_ROLE_GROUP_PREFIX = "ROLE";

    /**
     * 部门候选组前缀
     */
    public static final String CANDIDATE_DEPT_GROUP_PREFIX = "DEPT";

}
