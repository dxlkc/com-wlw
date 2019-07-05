package com.lkc.controller;

import com.lkc.model.DownLink.ControlInfo;
import com.lkc.model.DownLink.RuleInfo;
import com.lkc.model.DownLink.Sensor;
import com.lkc.model.DownLink.SensorDebug;
import com.lkc.model.Industry.deviceInfo.Rule;
import com.lkc.service.DownInfoService;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class DownLinkController {
    @Resource
    private DownInfoService downInfoService;

    //下行 用户API调试接口（debug） ok
    @PostMapping(value = "/down/debug")
    public String debug(@RequestParam String content, @RequestParam String deviceId,
                        HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        //组成 SensorDebug 转成json字符串发到 service层
        SensorDebug sensorDebug = new SensorDebug();
        sensorDebug.setContent(content);
        sensorDebug.setTopic(deviceId + "_" + uid + "_debug");

        JSONObject jsonObject = JSONObject.fromObject(sensorDebug);
        String message = jsonObject.toString();

        return downInfoService.downData(message, deviceId, uid, sensorDebug.getTopic());
    }

    //通用下行信号（控制继电器 溶解氧 等设备）
    //addr 可以为空 ， 不为空表示是一个继电器
    @PostMapping(value = "/down/control")
    public String downCtl(@RequestParam String target, @RequestParam String operation,
                          @RequestParam String content, @RequestParam String deviceId,
                          @RequestParam String addr,
                          HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        ControlInfo controlInfo = new ControlInfo();
        controlInfo.setAddr(addr);
        controlInfo.setTarget(target);
        controlInfo.setOperation(operation);
        controlInfo.setContent(content);
        controlInfo.setTopic(deviceId + "_" + uid + "_" + target + "_" + operation);

        JSONObject jsonObject = JSONObject.fromObject(controlInfo);
        String message = jsonObject.toString();

        return downInfoService.downData(message, deviceId, uid, controlInfo.getTopic());
    }

    //下行 添加传感器时 测试
    @PostMapping(value = "/down/addtest")
    public String addTest(@RequestBody Sensor sensor, @RequestParam String deviceId, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        sensor.setOperation("Addcustomtest");
        sensor.setTopic(deviceId + "_" + uid + "_" + sensor.getTarget() + "_" + sensor.getOperation());

        JSONObject jsonObject = JSONObject.fromObject(sensor);
        String message = jsonObject.toString();

        return downInfoService.downData(message, deviceId, uid, sensor.getTopic());
    }

    //下行 添加传感器时 确认
    @PostMapping(value = "/down/addconfirm")
    public String addConfirm(@RequestBody Sensor sensor, @RequestParam String deviceId, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        sensor.setOperation("Addcustomconfirm");
        sensor.setTopic(deviceId + "_" + uid + "_" + sensor.getTarget() + "_" + sensor.getOperation());

        JSONObject jsonObject = JSONObject.fromObject(sensor);
        String message = jsonObject.toString();

        return downInfoService.downData(message, deviceId, uid, sensor.getTopic());
    }

    //下行 连通性测试
    @PostMapping(value = "/down/heart")
    public String heart(@RequestParam String deviceId, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        ControlInfo controlInfo = new ControlInfo();
        controlInfo.setTarget("heart");
        controlInfo.setOperation("heart");
        controlInfo.setContent("heart");
        controlInfo.setTopic(deviceId + "_" + uid + "_heart");

        JSONObject jsonObject = JSONObject.fromObject(controlInfo);
        String message = jsonObject.toString();
        return downInfoService.downData(message, deviceId, uid, controlInfo.getTopic());
    }

    //下行 添加一条规则
    @PostMapping(value = "/down/addrule")
    public String addRule(@RequestParam String deviceId, @RequestBody Rule rule, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setAddr(rule.getAddr());
        ruleInfo.setCode_485(rule.getCode());
        ruleInfo.setId(rule.getRuleId());
        ruleInfo.setLogic(rule.getLogic());
        ruleInfo.setTypeName(rule.getTypeName());
        ruleInfo.setSwitchState("0");
        ruleInfo.setValue(rule.getValue());
        ruleInfo.setOperation("add");
        ruleInfo.setTopic(deviceId + "_" + uid + "_" + ruleInfo.getTarget() + "_" + ruleInfo.getOperation());

        JSONObject jsonObject = JSONObject.fromObject(ruleInfo);
        String message = jsonObject.toString();
        return downInfoService.downData(message, deviceId, uid, ruleInfo.getTopic());
    }

    //下行 删除一条规则
    @PostMapping(value = "/down/deleterule")
    public String deleteRule(@RequestParam String deviceId, @RequestParam String ruleId, HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setId(ruleId);
        ruleInfo.setOperation("delete");
        ruleInfo.setTopic(deviceId + "_" + uid + "_" + ruleInfo.getTarget() + "_" + ruleInfo.getOperation());

        JSONObject jsonObject = JSONObject.fromObject(ruleInfo);
        String message = jsonObject.toString();
        return downInfoService.downData(message, deviceId, uid, ruleInfo.getTopic());

    }

    //下行 更新开关状态
    @PostMapping(value = "/down/updatestate")
    public String updateState(@RequestParam String deviceId, @RequestParam String ruleId, @RequestParam String switchState,
                              HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        String uid = httpSession.getId();

        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setId(ruleId);
        ruleInfo.setSwitchState(switchState);
        ruleInfo.setOperation("setSwitchState");
        ruleInfo.setTopic(deviceId + "_" + uid + "_" + ruleInfo.getTarget() + "_" + ruleInfo.getOperation());

        JSONObject jsonObject = JSONObject.fromObject(ruleInfo);
        String message = jsonObject.toString();
        return downInfoService.downData(message, deviceId, uid, ruleInfo.getTopic());
    }

}


