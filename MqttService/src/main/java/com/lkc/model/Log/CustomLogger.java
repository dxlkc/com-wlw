package com.lkc.model.Log;

public class CustomLogger {
    //操作的服务器时间
    private String time;
    //操作类型  0:其他类  1:控制类
    private String type;
    //操作的板子
    private String deviceId;
    //具体操作内容
    private String message;
    //操作结果
    private String result;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
