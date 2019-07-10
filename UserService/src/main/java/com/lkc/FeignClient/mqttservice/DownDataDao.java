package com.lkc.FeignClient.mqttservice;

import com.lkc.model.CustomLogger;
import com.lkc.model.industry.deviceInfo.Rule;
import com.lkc.model.industry.sensorInfo.Sensor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "middleware-server")
public interface DownDataDao {

    //下行 用户API调试接口（debug）
    @PostMapping(value = "/user/down/debug")
    String debug(@RequestParam String content, @RequestParam String deviceId);

    //xingjia
    //下行 用户API调试接口（coap debug）
    @PostMapping(value = "/user/coap/debug")
    String coapDebug(@RequestParam String ip, @RequestParam String content);

    //下行 控制信号（控制继电器 溶解氧 等设备）
    @PostMapping(value = "/user/down/control")
    String downCtl(@RequestParam String target, @RequestParam String operation,
                   @RequestParam String content, @RequestParam String deviceId,
                   @RequestParam String addr);

    //下行 控制继电器所有开关
    @PostMapping(value = "/user/down/relayall")
    String relayAll(@RequestParam String operation, @RequestParam String deviceId);

    //下行 添加传感器时 测试    "success"/失败原因
    @PostMapping(value = "/user/down/addsensor/test")
    String addTest(@RequestBody Sensor sensor, @RequestParam String deviceId);

    //下行 添加传感器时 确认
    @PostMapping(value = "/user/down/addsensor/confirm")
    String addConfirm(@RequestBody Sensor sensor, @RequestParam String deviceId);


    //下行 添加继电器时 测试
    @PostMapping(value = "/user/down/addrelay/test")
    String addRelayTest(@RequestParam String deviceId, @RequestParam String addr,
                        @RequestParam String code485);

    //下行 添加继电器时 确认
    @PostMapping(value = "/user/down/addrelay/confirm")
    String addRelaConfirm(@RequestParam String deviceId, @RequestParam String addr,
                          @RequestParam String code485);

    //start 开始时间(10位时间戳)   deviced 设备ID   types  sensortypes+sensorid   (" tenoerature:"01" ")
    @PostMapping(value = "/history")
    String findHistory(@RequestParam String start, @RequestParam String end,
                       @RequestParam String deviceId);

    @PostMapping(value = "/realtime")
    String realTime(@RequestParam String deviceId, @RequestParam String types);

    @PostMapping(value = "/subscribe")
    void subscribe(@RequestParam String deviceId);


    @PostMapping(value = "/user/down/heart")
    String heart(@RequestParam(value = "deviceId") String deviceId);

    @PostMapping(value = "/opslog")
    void addOpsLog(@RequestParam String industryId, @RequestBody CustomLogger customLogger);

    @PostMapping(value = "/user/down/updatestate")
    String updateState(@RequestParam String deviceId, @RequestParam String ruleId,
                       @RequestParam String switchState);

    //按照 deviceID 起始时间，结束时间来查询log
    @RequestMapping(value = "/log/findByDT", method = RequestMethod.POST)
    String findLogByDeviceIdAndTime(@RequestParam String industryId, @RequestParam String deviceId,
                                    @RequestParam String start, @RequestParam String end);

    @PostMapping(value = "/user/down/addrule")
    String addRule(@RequestParam String deviceId, @RequestBody Rule rule);

    //下行 删除一条规则
    @PostMapping(value = "/user/down/deleterule")
    String deleteRule(@RequestParam String deviceId, @RequestParam String ruleId);

    //取消订阅
    @RequestMapping(value = "/dissubscribe", method = RequestMethod.POST)
    void cancelsub(@RequestParam String deviceId);

    //用coap 调用 webssh
    @PostMapping(value = "/user/coap/webssh")
    String useWebssh(@RequestParam String destip);
}