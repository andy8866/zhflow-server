package com.andy.zhflow.processModel;

import lombok.Data;

@Data
public class BpmnConstant {
    public static final String PROC_TYPE_LEAVE = "leave";

    public static final String ATTR_INITIATOR = "initiator";

    public static final String ATTR_ASSIGNEE = "assignee";

    public static final String VAR_START_USER = "startUser";
    public static final String VAR_START_USER_NAME = "startUserName";

    public static final String VAR_PROC_CREATE_TIME = "procCreateTime";
    public static final String VAR_APPROVAL = "approval";
    public static final String VAR_REASON = "reason";


}
