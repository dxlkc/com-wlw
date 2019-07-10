package com.lkc.controller.Industry;

import com.lkc.service.serviceInterface.Industry.RelayService;
import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.industry.relayInfo.Relay;
import com.lkc.tool.CRC16M;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relay")
public class RelayController {
    @Resource
    private RelayService relayService;
    @Resource
    private DownDataDao downDataDao;

    @RequestMapping(value = "/find/relay", method = RequestMethod.POST)
    public String findMachine(@RequestParam String deviceId) {
        return relayService.findRelay(deviceId);
    }

    ////relay详情显示
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String findRelay(@RequestParam String deviceId, @RequestParam String relayAddr) {
        return relayService.findRelaydata(deviceId, relayAddr);
    }

    //relay更改前返回数据
    @RequestMapping(value = "/returnbefore", method = RequestMethod.POST)
    public String returnbefore(@RequestParam String deviceId, @RequestParam String relayAddr) {
        return relayService.returnbefore(deviceId, relayAddr);
    }

    /****************添加********************/

    @RequestMapping(value = "/add/relay", method = RequestMethod.POST)
    public String addRelay(@RequestBody Relay relay) {
        return relayService.addRelay(relay);
    }

    @RequestMapping(value = "/add/machine", method = RequestMethod.POST)
    public String addMachine(@RequestParam String industryId, @RequestParam String deviceId,
                             @RequestParam String relayAddr, @RequestParam String machineName, @RequestParam String machinePostion) {
        return relayService.addMachine(industryId, deviceId, relayAddr, machineName, machinePostion);

    }

    /****************更新********************/

    @RequestMapping(value = "/update/relay", method = RequestMethod.POST)
    public String updateRelayInfo(@RequestParam String industryId, @RequestParam String deviceId,
                                  @RequestParam String relayAddr, @RequestParam String newName) {
        return relayService.updateRelayInfo(industryId, deviceId, relayAddr, newName);
    }


    //
    @RequestMapping(value = "/machine", method = RequestMethod.POST)
    public String updateMachineInfo(@RequestBody List<Map<String, String>> mapList) {
        return relayService.updateOradd(mapList);
    }

    @RequestMapping(value = "/update/machineState", method = RequestMethod.POST)
    public String updateMachineState(@RequestParam String industryId, @RequestParam String deviceId,
                                     @RequestParam String relayAddr, @RequestParam String position,
                                     @RequestParam Boolean state) {
        return relayService.updateMachineState(industryId, deviceId, relayAddr, position, state);
    }

    /****************删除********************/

    @RequestMapping(value = "/delete/relay", method = RequestMethod.POST)
    public String deleteByRelayAddr(@RequestParam String industryId, @RequestParam String deviceId,
                                    @RequestParam String relayAddr) {
        return relayService.deleteRelay(industryId, deviceId, relayAddr);
    }

    @RequestMapping(value = "/delete/machine", method = RequestMethod.POST)
    public String deleteByMachineName(@RequestParam String industryId, @RequestParam String deviceId,
                                      @RequestParam String relayAddr, @RequestParam String machinePostion) {
        return relayService.deleteMachine(industryId, deviceId, relayAddr, machinePostion);
    }

    /****************检查连接******************/

    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public String linkTest(@RequestParam String deivceId, @RequestParam String addr) {
        String code = Long.toHexString(Long.valueOf(addr)).toUpperCase();
        code = code + " 01 00 00 00 08 ";
        String replace = code.replace(" ", "");
        byte[] sbuf = CRC16M.getSendBuf(replace);
        String ss = CRC16M.getBufHexStr(sbuf);
        code = code + ss;
        return downDataDao.addRelayTest(deivceId, addr, code);
    }
}
