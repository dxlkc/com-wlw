package com.lkc.model.industry.sensorInfo;

//传感器能测得的数据类型
public class Type {
    //数据类型名
    private String typeName;
    //环境数据起始地址
    private String byteStart;
    //环境数据所占字节数
    private String byteCount;
    //环境数据类型  int,float....
    private String dataDefine;
    //小数点位数
    private String dicimal;

    private String negative;

    public String getNegative() {
        return negative;
    }

    public void setNegative(String negative) {
        this.negative = negative;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getByteStart() {
        return byteStart;
    }

    public void setByteStart(String byteStart) {
        this.byteStart = byteStart;
    }

    public String getByteCount() {
        return byteCount;
    }

    public void setByteCount(String byteCount) {
        this.byteCount = byteCount;
    }

    public String getDataDefine() {
        return dataDefine;
    }

    public void setDataDefine(String dataDefine) {
        this.dataDefine = dataDefine;
    }

    public String getDicimal() {
        return dicimal;
    }

    public void setDicimal(String dicimal) {
        this.dicimal = dicimal;
    }
}
