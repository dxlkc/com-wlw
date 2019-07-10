package com.lkc.model.DownLink;

//API 调试
public class SensorDebug {
    //要控制的目标 默认为 Sensor
    private String target="sensor";
    //控制指令 默认为 Debug
    private String operation="Debug";
    //设置参数
    private String content;
    //板子返回信息 发布的主题 (本机需要订阅的主题)
    private String topic;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
