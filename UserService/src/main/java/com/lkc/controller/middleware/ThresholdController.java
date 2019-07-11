package com.lkc.controller.middleware;

import com.lkc.WebSocket.SendThread;
import com.lkc.service.serviceInterface.MqttService.ThresholdService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ThresholdController {
    @Resource
    private ThresholdService thresholdService;
    @Resource
    private SendThread sendThread;

    //预警接口  发送邮件
    @PostMapping(value = "/handler/threshold")
    public void thresholdHandler(@RequestParam String deviceId, @RequestBody Map<String, String> map,
                                 @RequestParam String time) {
        thresholdService.thresholdHandler(deviceId, map, time);
    }

    //发送一条实时数据
    @PostMapping(value = "/send/realtime")
    public void notic() {
        sendThread.send();
    }

}
