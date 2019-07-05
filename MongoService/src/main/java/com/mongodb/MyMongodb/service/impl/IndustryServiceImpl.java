package com.mongodb.MyMongodb.service.impl;

import com.mongodb.MyMongodb.dao.DeviceDao;
import com.mongodb.MyMongodb.dao.IndustryDao;
import com.mongodb.MyMongodb.dao.RelayDao;
import com.mongodb.MyMongodb.dao.SensorDao;
import com.mongodb.MyMongodb.model.relayInfo.Machine;
import com.mongodb.MyMongodb.model.relayInfo.Relay;
import com.mongodb.MyMongodb.service.IndustryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Resource
    private IndustryDao industryDao;
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private RelayDao relayDao;
    @Resource
    private SensorDao sensorDao;

    //删除整个产业
    public long deleteIndustry(String industryId) {
        return industryDao.deleteByIndustryId(industryId) &
                deviceDao.deleteAllByIndustryId(industryId) &
                relayDao.deleteAllByIndustryId(industryId) &
                sensorDao.deleteAllByIndustryId(industryId);
    }

    //删除整个采集单元
    public long deleteUnit(String industryId, String unitId) {
        return industryDao.deleteByAcqUnitId(industryId, unitId) &
                deviceDao.deleteAllByIndustryIdAndUnitId(industryId, unitId) &
                relayDao.deleteAllByIndustryIdAndUnitId(industryId, unitId) &
                sensorDao.deleteAllByIndustryIdAndUnitId(industryId, unitId);
    }

    //删除整个设备
    public long deleteDevice(String industryId, String deviceId) {
        return deviceDao.deleteByDeviceId(industryId, deviceId) &
                relayDao.deleteAllByDeviceId(deviceId) &
                sensorDao.deleteAllByDeviceId(deviceId);
    }

    //修改继电器状态 （同时修改继电器下设备的状态）
    public long updateRelayPinsState(String deviceId, String relayAddr, String newPinsState){
        char[] str = newPinsState.toCharArray();
        int size = str.length;
        Relay relay = relayDao.find(deviceId,relayAddr);
        if (null == relay){
            return 1;
        }
        List<Machine> machineList = relay.getMachineList();

        for (Machine machine : machineList){
            int start = size - Integer.valueOf(machine.getMachinePostion());
            String state = newPinsState.substring(start, start+1);
            relayDao.updateMachineState(deviceId,relayAddr,machine.getMachinePostion(),state);
        }
        return relayDao.updatePinsState(deviceId,relayAddr,newPinsState);
    }

}
