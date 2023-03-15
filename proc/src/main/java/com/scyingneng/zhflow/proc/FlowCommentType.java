package com.scyingneng.zhflow.proc;

/**
 * 流程意见类型
 *
 */
public enum FlowCommentType {

    /**
     * 说明
     */
    NORMAL("1", "正常"),
    REBACK("2", "退回"),
    REJECT("3", "拒绝"),
    DELEGATE("4", "委派"),
    TRANSFER("5", "转办"),
    STOP("6", "终止"),
    PASS("7", "通过");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    FlowCommentType(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
