package com.lkc.model;

//产业/采集点 id
public class IdCount {
    public int IndustryCount = 0;
    public int AcqUnitCount = 0;

    private IdCount() {
    }

    private static final IdCount idcount = new IdCount();

    public static IdCount getIntance() {
        return idcount;
    }

    synchronized public int industryCount() {
        IndustryCount++;
        return IndustryCount;
    }

    synchronized public int acqUnitCount() {
        AcqUnitCount++;
        return AcqUnitCount;
    }

}
