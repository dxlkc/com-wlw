package com.lkc.model.DownLink;

//下行控制信号模型
//可以控制 继电器 和 溶解氧水泵
public class ControlInfo {
    //要控制的目标
    private String target;
    //控制指令
    private String operation;
    //设备在板子的位置  站号
    private String addr;
    //设置参数
    private String content;
    //板子需要发布的主题
    private String topic;

    public ControlInfo(){
        this.content = "";
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
