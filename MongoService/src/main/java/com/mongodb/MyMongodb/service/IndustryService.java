package com.mongodb.MyMongodb.service;

public interface IndustryService {

    long deleteIndustry(String industryId);

    long deleteUnit(String industryId, String unitId);

    long deleteDevice(String industryId, String deviceId);

    long updateRelayPinsState(String deviceId, String relayAddr, String newPinsState);
}
