package com.lkc.FeignClient.mongoservice;

import com.lkc.model.industry.relayInfo.Machine;
import com.lkc.model.industry.relayInfo.Relay;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "service")
public interface RelayDao {

    @RequestMapping(value = "/relay/find", method = RequestMethod.POST)
    Relay find(@RequestParam String deviceId,
               @RequestParam String relayAddr);

    @RequestMapping(value = "/relay/findAll", method = RequestMethod.POST)
    List<Relay> findAll(@RequestParam String deviceId);

    @RequestMapping(value = "/relay/findMachine", method = RequestMethod.POST)
    Machine findMachine(@RequestParam String deviceId, @RequestParam String relayAddr, @RequestParam String machinePosition);

    /****************添加********************/

    @RequestMapping(value = "/relay/add/relay", method = RequestMethod.POST)
    Relay addRelay(@RequestBody Relay relay);

    @RequestMapping(value = "/relay/add/machine", method = RequestMethod.POST)
    long addMachine(@RequestParam String industryId, @RequestParam String deviceId,
                    @RequestParam String relayAddr, @RequestBody Machine machine);

    /****************更新********************/

    @RequestMapping(value = "/relay/update/relay", method = RequestMethod.POST)
    long updateRelayInfo(@RequestParam String industryId, @RequestParam String deviceId,
                         @RequestParam String relayAddr, @RequestParam String newName);


    @RequestMapping(value = "/relay/update/machine", method = RequestMethod.POST)
    long updateMachineInfo(@RequestParam String deviceId,
                           @RequestParam String relayAddr, @RequestParam String machinePosition,
                           @RequestParam String newName);

    @RequestMapping(value = "/relay/update/machineState", method = RequestMethod.POST)
    long updateMachineState(@RequestParam String deviceId, @RequestParam String relayAddr,
                            @RequestParam String machinePosition, @RequestParam String state);

    /****************删除********************/

    @RequestMapping(value = "/relay/delete/relay", method = RequestMethod.POST)
    long deleteByRelayAddr(@RequestParam String industryId, @RequestParam String deviceId,
                           @RequestParam String relayAddr);

    @RequestMapping(value = "/relay/delete/machine", method = RequestMethod.POST)
    long deleteByMachineName(@RequestParam String industryId, @RequestParam String deviceId,
                             @RequestParam String relayAddr, @RequestParam String machinePosition);
}
