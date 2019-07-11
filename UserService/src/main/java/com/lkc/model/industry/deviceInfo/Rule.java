package com.lkc.model.industry.deviceInfo;

//规则设备规则
public class Rule {
    //自增,唯一
    private String ruleId;
    //判断值传感器addr
    private String addr;
    //判断值传感器type
    private String typeName;
    //规则 morethan(>)/lessthan(<)/notlessthan(>=)/notmorethan(<=)
    private String logic;
    //规则判断值
    private String value;
    //动作 485code
    private String code;
    //开(1),关(0)
    private String switchState;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSwitchState() {
        return switchState;
    }

    public void setSwitchState(String switchState) {
        this.switchState = switchState;
    }
}
