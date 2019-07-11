package com.mongodb.MyMongodb.service.impl;

import com.mongodb.MyMongodb.dao.DeviceDao;
import com.mongodb.MyMongodb.dao.IndustryDao;
import com.mongodb.MyMongodb.dao.RelayDao;
import com.mongodb.MyMongodb.dao.SensorDao;
import com.mongodb.MyMongodb.model.deviceInfo.Device;
import com.mongodb.MyMongodb.model.relayInfo.Machine;
import com.mongodb.MyMongodb.model.relayInfo.Relay;
import com.mongodb.MyMongodb.service.IndustryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Resource
    private RelayDao relayDao;

    //修改继电器状态 （同时修改继电器下设备的状态）
    public long updateRelayPinsState(String deviceId, String relayAddr, String newPinsState) {
        int pinsState = Integer.valueOf(newPinsState);

        Relay relay = relayDao.find(deviceId, relayAddr);
        if (null == relay) {
            return 1;
        }
        List<Machine> machineList = relay.getMachineList();

        for (Machine machine : machineList) {
            int state = 0x1 & (pinsState >> (Integer.valueOf(machine.getMachinePosition()) - 1));
            relayDao.updateMachineState(deviceId, relayAddr, machine.getMachinePosition(), String.valueOf(state));
        }

        //状态转换成 "10000011" 右边开始1-8路状态 1为开 0为关
        StringBuilder allState = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            allState.append(0x1 & pinsState >> i);
        }

        return relayDao.updatePinsState(deviceId, relayAddr, allState.toString());
    }

}
