package com.mongodb.MyMongodb.controller;

import com.mongodb.MyMongodb.dao.RelayDao;
import com.mongodb.MyMongodb.model.relayInfo.Machine;
import com.mongodb.MyMongodb.model.relayInfo.Relay;
import com.mongodb.MyMongodb.service.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/relay")
public class RelayController {
    @Resource
    private RelayDao relayDao;
    @Resource
    private IndustryService industryService;

    @PostMapping(value = "/find")
    public Relay find(@RequestParam String deviceId, @RequestParam String relayAddr) {
        return relayDao.find(deviceId, relayAddr);
    }

    @PostMapping(value = "/findAll")
    public List<Relay> findAll(@RequestParam String deviceId) {
        return relayDao.findAll(deviceId);
    }

    //查找指定强电设备
    @PostMapping(value = "/findMachine")
    public Machine findMachineByPosition(@RequestParam String deviceId, @RequestParam String relayAddr,
                                         @RequestParam String machinePosition) {
        return relayDao.findMachineByPosition(deviceId, relayAddr, machinePosition);
    }

    /****************添加********************/

    @PostMapping(value = "/add/relay")
    public Relay addRelay(@RequestBody Relay relay) {
        return relayDao.addRelay(relay);
    }

    @PostMapping(value = "/add/machine")
    public long addMachine(@RequestParam String industryId, @RequestParam String deviceId,
                           @RequestParam String relayAddr, @RequestBody Machine machine) {
        return relayDao.addMachine(industryId, deviceId, relayAddr, machine);
    }

    /****************更新********************/

    @PostMapping(value = "/update/relay")
    public long updateRelayInfo(@RequestParam String industryId, @RequestParam String deviceId,
                                @RequestParam String relayAddr, @RequestParam String newName) {
        return relayDao.updateRelayInfo(industryId, deviceId, relayAddr, newName);
    }

    @PostMapping(value = "/update/state")
    public long updatePinsState(@RequestParam String deviceId, @RequestParam String relayAddr,
                                @RequestParam String newPinsState) {
        //调用service 修改继电器下的设备状态
        return industryService.updateRelayPinsState(deviceId, relayAddr, newPinsState);
    }

    @PostMapping(value = "/update/machine")
    public long updateMachineInfo(@RequestParam String deviceId, @RequestParam String relayAddr,
                                  @RequestParam String machinePosition, @RequestParam String newName) {
        return relayDao.updateMachineInfo(deviceId, relayAddr, machinePosition, newName);
    }

    @PostMapping(value = "/update/machineState")
    public long updateMachineState(@RequestParam String deviceId, @RequestParam String relayAddr,
                                   @RequestParam String machinePosition, @RequestParam String state) {
        return relayDao.updateMachineState(deviceId, relayAddr, machinePosition, state);
    }

    /****************删除********************/

    @PostMapping(value = "/delete/relay")
    public long deleteByRelayAddr(@RequestParam String industryId, @RequestParam String deviceId,
                                  @RequestParam String relayAddr) {
        return relayDao.deleteByRelayAddr(industryId, deviceId, relayAddr);
    }

    @PostMapping(value = "/delete/machine")
    public long deleteByMachinePosition(@RequestParam String industryId, @RequestParam String deviceId,
                                        @RequestParam String relayAddr, @RequestParam String machinePosition) {
        return relayDao.deleteByMachinePosition(industryId, deviceId, relayAddr, machinePosition);
    }
}
