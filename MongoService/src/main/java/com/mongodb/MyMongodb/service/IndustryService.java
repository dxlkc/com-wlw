package com.mongodb.MyMongodb.service;

public interface IndustryService {

    long updateRelayPinsState(String deviceId, String relayAddr, String newPinsState);
}
