package com.lkc.controller;

import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.model.Log.CustomLogger;
import com.lkc.model.ReturnUser.ReturnData;
import com.lkc.mqttUp.UpMqtt;
import com.lkc.service.HistoryService;
import com.lkc.service.LogService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Resource
    private InfluxdbDao influxdbDao;
    @Resource
    private LogService logService;
    @Resource
    private HistoryService historyService;

    //查询历史数据
    @PostMapping(value = "/history")
    public String findHistory(@RequestParam String start, @RequestParam String end,
                              @RequestParam String deviceId) {
        //原因：传进来的时间是 例如 2019-07-06-00:00:00
        //需要的是 2019-07-06 00:00:00
        start = start.substring(0,10) + " " + start.substring(11);
        end = end.substring(0,10) + " " + end.substring(11);

        List<ReturnData> list = historyService.findHistory(start, end, deviceId);
        JSONArray jsonArray = JSONArray.fromObject(list);

        return jsonArray.toString();
    }

    //删表
    @PostMapping(value = "/delete")
    public String drop(@RequestParam String measurement){
        return influxdbDao.deleteMeasurement(measurement);
    }

    //添加设备后进行订阅
    @PostMapping(value = "/subscribe")
    public void subscribe(@RequestParam String deviceId){
        UpMqtt.getInstance().subscribe(deviceId);
    }

    /***********************************************************************************/

    //添加操作日志
    @PostMapping(value = "/opslog")
    public void addOpsLog(@RequestParam String industryId, @RequestBody CustomLogger customLogger){
        logService.addOpsLog(industryId, customLogger);
    }

    //查询操作日志  一个产业的所有操作日志
    @PostMapping(value = "/log/findAll")
    public String findAllLog(@RequestParam String industryId){
        ArrayList<CustomLogger> customLoggers = influxdbDao.findAllLog(industryId+"_opslog");

        JSONArray jsonArray= JSONArray.fromObject(customLoggers);
        return jsonArray.toString();
    }

    //查询操作日志   按时间段查询
    @PostMapping(value = "/log/findByTime")
    public String findLogByTime(@RequestParam String industryId, @RequestParam String start,
                                @RequestParam String end){
        //原因：传进来的时间是 例如 2019-07-06-00:00:00
        //需要的是 2019-07-06 00:00:00
        start = start.substring(0,10) + " " + start.substring(11);
        end = end.substring(0,10) + " " + end.substring(11);

        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByTime(start,end,industryId+"_opslog");

        JSONArray jsonArray= JSONArray.fromObject(customLoggers);
        return jsonArray.toString();
    }

    //查询操作日志  按deviceId查询
    @PostMapping(value = "/log/findByDeviceId")
    public String findLogByDeviceId(@RequestParam String industryId, @RequestParam String deviceId){
        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByDeviceId(deviceId,industryId+"_opslog");

        JSONArray jsonArray= JSONArray.fromObject(customLoggers);
        return jsonArray.toString();
    }

    //查询操作日志  按deviceId和时间段 查询
    @PostMapping(value = "/log/findByDT")
    public String findLogByDeviceIdAndTime(@RequestParam String industryId, @RequestParam String deviceId,
                                           @RequestParam String start, @RequestParam String end){
        //原因：传进来的时间是 例如 2019-07-06-00:00:00
        //需要的是 2019-07-06 00:00:00
        start = start.substring(0,10) + " " + start.substring(11);
        end = end.substring(0,10) + " " + end.substring(11);

        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByDeviceIdAndTime(start,end,deviceId,industryId+"_opslog");

        JSONArray jsonArray= JSONArray.fromObject(customLoggers);
        return jsonArray.toString();
    }

}
