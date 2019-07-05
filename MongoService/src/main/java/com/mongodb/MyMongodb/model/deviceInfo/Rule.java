package com.mongodb.MyMongodb.model.deviceInfo;

public class Rule {
    //规则唯一标识
    private String ruleId;
    //device 站号(传感器位置)
    private String addr;
    //传感器所测的数据的类型
    private String typeName;
    //大于、小于、等于等比较符号  >_<
    private String logic;
    //比较符号后面的值  >20
    private String value;
    //对应执行的485指令
    private String code;
    //是否启用这条规则
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
