package com.mongodb.MyMongodb.dao;

import com.mongodb.MyMongodb.model.relayInfo.Machine;
import com.mongodb.MyMongodb.model.relayInfo.Relay;

import java.util.List;

public interface RelayDao {

    Relay find(String deviceId, String relayAddr);

    List<Relay> findAll(String deviceId);

    /****************添加********************/

    Relay addRelay(Relay relay);

    long addMachine(String industryId, String deviceId, String relayAddr, Machine machine);

    /****************更新********************/

    long updateRelayInfo(String industryId, String deviceId, String relayAddr, String newName);

    long updatePinsState(String deviceId, String relayAddr, String newPinsState);

    long updateMachineInfo(String deviceId, String relayAddr, String machinePosition,
                           String newName, String newPosition);

    long updateMachineState(String deviceId, String relayAddr, String machinePosition, String state);

    /****************删除********************/

    long deleteAllByIndustryId(String industryId);

    long deleteAllByIndustryIdAndUnitId(String industryId, String unitId);

    long deleteAllByDeviceId(String deviceId);

    long deleteByRelayAddr(String industryId, String deviceId, String relayAddr);

    long deleteByMachineName(String industryId, String deviceId, String relayAddr, String machineName);
}
