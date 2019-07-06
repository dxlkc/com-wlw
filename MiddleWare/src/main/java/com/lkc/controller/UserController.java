package com.lkc.controller;

import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.model.Log.CustomLogger;
import com.lkc.model.ReturnUser.ReturnData;
import com.lkc.mqttUp.UpMqtt;
import com.lkc.service.LogService;
import com.lkc.tool.TimeChange;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Resource
    private InfluxdbDao influxdbDao;
    @Resource
    private LogService logService;

    //查询历史数据
    @PostMapping(value = "/history")
    public String findHistory(@RequestParam String start, @RequestParam String end,
                              @RequestParam String deviceId, @RequestParam String types) {
        //键 为 温湿度等类型
        //值 为 类型对应站号
        List<ReturnData> res = new ArrayList<>();

        Map<String, String> type = JSONObject.fromObject(types);

        for (Map.Entry<String, String> entry : type.entrySet()) {
            ReturnData returnData = new ReturnData();
            returnData.setDeviceId(deviceId);
            returnData.setType(entry.getKey());

            String measurement = deviceId+"_"+entry.getValue()+"_"+entry.getKey();
            ArrayList<ArrayList<String>> list =
                    influxdbDao.findByTime(TimeChange.secondToTime(start), TimeChange.secondToTime(end), measurement);

            returnData.setData(list);
            res.add(returnData);
        }

        JSONArray jsonArray = JSONArray.fromObject(res);
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

        JSONObject jsonObject = JSONObject.fromObject(customLoggers);
        return jsonObject.toString();
    }

    //查询操作日志   按时间段查询
    @PostMapping(value = "/log/findByTime")
    public String findLogByTime(@RequestParam String industryId, @RequestParam String starttime,
                                @RequestParam String endtime){
        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByTime(starttime,endtime,industryId+"_opslog");

        JSONObject jsonObject = JSONObject.fromObject(customLoggers);
        return jsonObject.toString();
    }

    //查询操作日志  按deviceId查询
    @PostMapping(value = "/log/findByDeviceId")
    public String findLogByDeviceId(@RequestParam String industryId, @RequestParam String deviceId){
        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByDeviceId(deviceId,industryId+"_opslog");

        JSONObject jsonObject = JSONObject.fromObject(customLoggers);
        return jsonObject.toString();
    }

    //查询操作日志  按deviceId和时间段 查询
    @PostMapping(value = "/log/findByDT")
    public String findLogByDeviceIdAndTime(@RequestParam String industryId, @RequestParam String deviceId,
                                           @RequestParam String starttime, @RequestParam String endtime){
        ArrayList<CustomLogger> customLoggers = influxdbDao.findLogByDeviceIdAndTime(starttime,endtime,deviceId,industryId+"_opslog");

        JSONObject jsonObject = JSONObject.fromObject(customLoggers);
        return jsonObject.toString();
    }

}
