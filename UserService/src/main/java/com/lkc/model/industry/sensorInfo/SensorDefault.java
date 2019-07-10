package com.lkc.model.industry.sensorInfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sensor_default")
public class SensorDefault {
    //站号 固定
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "addr")
    private String sensorAddr;
    @Column(name = "code")
    //485指令
    private String code;
    @Column(name = "typename")
    //传感器测的数据类型
    private String type;
    @Column(name = "bytestart")
    //起始位置 固定
    private String byteStart;
    @Column(name = "bytecount")
    //字节数 固定
    private String byteCount;
    @Column(name = "datatype")
    //数据类型 固定
    private String dataType;
    @Column(name = "dicimal")
    //小数点位数 固定
    private String dicimal;
    @Column(name = "returnlength")
    private String returnLength;

    public String getSensorAddr() {
        return sensorAddr;
    }

    public void setSensorAddr(String sensorAddr) {
        this.sensorAddr = sensorAddr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDicimal() {
        return dicimal;
    }

    public void setDicimal(String dicimal) {
        this.dicimal = dicimal;
    }

    public String getReturnLength() {
        return returnLength;
    }

    public void setReturnLength(String returnLength) {
        this.returnLength = returnLength;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
