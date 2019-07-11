package com.lkc.controller.middleware;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.industry.sensorInfo.Type;
import com.lkc.service.serviceInterface.MqttService.DowndataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class DownDataController {
    @Resource
    private DownDataDao downDataDao;
    @Resource
    private DowndataService downdataService;

    //下行 用户API调试接口（debug）
    @RequestMapping(value = "/debug", method = RequestMethod.POST)
    public String debug(@RequestParam String content, @RequestParam String deviceId) {
        if (content == null || content.isEmpty()) {
            return "指令为空";
        }
        System.out.println("content      " + content + " deviceId     " + deviceId);
        return downDataDao.debug(content, deviceId);
    }

    //用Coap进行debug
    @RequestMapping(value = "/coap/debug", method = RequestMethod.POST)
    public String coapDebug(@RequestParam String ip, @RequestParam String content) {
        System.out.println("ip    " + ip + " content   " + content);
        return downDataDao.coapDebug(ip, content);
    }

    //下行 控制信号（控制继电器 溶解氧 等设备）
    @RequestMapping(value = "/down/control", method = RequestMethod.POST)
    public String downCtl(@RequestParam String target, @RequestParam String operation,
                          @RequestParam String content, @RequestParam String deviceId,
                          @RequestParam String addr) {
        return downDataDao.downCtl(target, operation, content, deviceId, addr);
    }

    //下行 控制继电器所有开关
    @RequestMapping(value = "/down/relayall", method = RequestMethod.POST)
    public String relayAll(@RequestParam String operation, @RequestParam String deviceId) {
        return downDataDao.relayAll(operation, deviceId);
    }

    //下行 添加传感器时 测试
    @RequestMapping(value = "/down/addtest", method = RequestMethod.POST)
    public String addTest(@RequestParam String target, @RequestParam String operation,
                          @RequestParam String addr, @RequestParam String code485,
                          @RequestParam String returnLength, @RequestParam String topic,
                          @RequestParam List<Type> types, @RequestParam String deviceId) {
        return downdataService.addTest(target, operation, addr, code485, returnLength, topic,
                types, deviceId);
    }

    //下行 添加传感器时 确认
    @RequestMapping(value = "/down/addconfirm", method = RequestMethod.POST)
    public String addConfirm(@RequestParam String target, @RequestParam String operation,
                             @RequestParam String addr, @RequestParam String code485,
                             @RequestParam String returnLength, @RequestParam String topic,
                             @RequestParam List<Type> types, @RequestParam String deviceId) {
        return downdataService.addConfirm(target, operation, addr, code485, returnLength, topic,
                types, deviceId);
    }

    //查询历史数据
    @RequestMapping(value = "/find/history", method = RequestMethod.POST)
    public String findHistory(@RequestParam String start, @RequestParam String end,
                              @RequestParam String deviceId) {
        return downDataDao.findHistory(start, end, deviceId);
    }

    //用coap 调用 webssh
    @RequestMapping(value = "/webssh", method = RequestMethod.POST)
    public String coapWebssh(@RequestParam String ip) {
        return downDataDao.useWebssh(ip);
    }

}
