package com.lkc.model.returndata;

import java.util.List;

public class Industrydata {
    private String industryDeviceNum;
    private String industryId;
    private String industryName;
    private String industryRemark;
    private String industryUnitNum;
    private String industryTime;
    private List<Industrygps> industryGps;

    public String getIndustryDeviceNum() {
        return industryDeviceNum;
    }

    public void setIndustryDeviceNum(String industryDeviceNum) {
        this.industryDeviceNum = industryDeviceNum;
    }

    public String getIndustryId() {
        return industryId;
    }

    public void setIndustryId(String industryId) {
        this.industryId = industryId;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getIndustryRemark() {
        return industryRemark;
    }

    public void setIndustryRemark(String industryRemark) {
        this.industryRemark = industryRemark;
    }

    public String getIndustryUnitNum() {
        return industryUnitNum;
    }

    public void setIndustryUnitNum(String industryUnitNum) {
        this.industryUnitNum = industryUnitNum;
    }

    public String getIndustryTime() {
        return industryTime;
    }

    public void setIndustryTime(String industryTime) {
        this.industryTime = industryTime;
    }

    public List<Industrygps> getIndustryGps() {
        return industryGps;
    }

    public void setIndustryGps(List<Industrygps> industryGps) {
        this.industryGps = industryGps;
    }
}
