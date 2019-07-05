package com.lkc.model.DownLink;

//下发的规则格式
public class RuleInfo {
    private String target = "rule";
    private String operation;
    //规则唯一标识
    private String id;
    //device 站号(传感器位置)
    private String addr;
    //传感器所测的数据的类型
    private String typeName;
    //大于、小于、等于等比较符号  >_<
    private String logic;
    //比较符号后面的值  >20
    private String value;
    //对应执行的485指令
    private String code_485;
    //是否启用这条规则
    private String switchState;
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCode_485() {
        return code_485;
    }

    public void setCode_485(String code_485) {
        this.code_485 = code_485;
    }

    public String getSwitchState() {
        return switchState;
    }

    public void setSwitchState(String switchState) {
        this.switchState = switchState;
    }
}
