package com.lkc.service.serviceImpl.IndustryImpl;

import com.lkc.model.CustomLogger;
import com.lkc.service.serviceInterface.Industry.RelayService;
import com.lkc.service.serviceInterface.Industry.SensorService;
import com.lkc.FeignClient.mongoservice.DeviceDao;
import com.lkc.FeignClient.mongoservice.IndustryDao;
import com.lkc.FeignClient.mongoservice.RelayDao;
import com.lkc.FeignClient.mongoservice.SensorDao;
import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.industry.deviceInfo.Device;
import com.lkc.model.industry.deviceInfo.Rule;
import com.lkc.model.industry.industryInfo.AcqUnit;
import com.lkc.model.industry.industryInfo.Industry;
import com.lkc.model.industry.relayInfo.Relay;
import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.service.serviceInterface.Industry.DeviceService;
import com.lkc.tool.CRC16M;
import com.lkc.tool.QuitSort;
import com.lkc.tool.UID;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    DeviceDao deviceDao;

    @Resource
    IndustryDao industryDao;

    @Resource
    DownDataDao downDataDao;

    @Resource
    RelayDao relayDao;

    @Resource
    SensorDao sensorDao;

    @Resource
    SensorService sensorService;
    @Resource
    RelayService relayService;
    /*****************************添加********************************************************/

    @Override
    public String addDevice(String industryId, String AcqUnitId,String deviceId,
                            String deviceName,String deviceRemark,String sendRate) {

        String log = "产业:" + industryId + " 添加了一个设备:" + deviceName + "_" + deviceId;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        Industry industry = industryDao.findByIndustryId(industryId);
        String Name = industry.getIndustryName() + '/';
        String unitName = "";
        for(AcqUnit acqUnit : industry.getAcqUnitList())
        {
            if(acqUnit.getUnitId().equals(AcqUnitId))
            {
                unitName = acqUnit.getUnitName();
                break;
            }
        }
        String res = downDataDao.downCtl("sendrate","setSendrate",sendRate,deviceId," ");
        if(!res.equals("success")){
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return res;
        }
        Name+=unitName;
        Device device = new Device();
        Device device1;
        device.setIndustryId(industryId);
        device.setUnitId(AcqUnitId);
        device.setDeviceId(deviceId);
        device.setDeviceName(deviceName);
        device.setDeviceRemark(deviceRemark);
        device.setSendRate(sendRate);
        device.setLinkState("已入网");
        device1 = deviceDao.addDevice(device);
        if(device1 == null) {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "addDevice_fail";
        }
        else {
            downDataDao.addOpsLog(industryId,customLogger);
            customLogger.setResult("成功");
            return Name;
        }
    }



    //content:哪一路,-1为全开全关        toaddr:所要控制的继电器的位置   action:FF(开) 00(关)


    @Override
    public String addrule(Map<String,String> map) {
        String code = null;
        //其余直接传给485code    profession

        //继电器 生成485code
        if(map.get("type").equals("amateur"))
        {
            //全开/全关
            if(map.get("content").equals("-1"))
            {
                String first = Long.toHexString(Long.valueOf(map.get("toaddr"))).toUpperCase();
                code = first;
                code = code + " 0F 00 00 00 08 01 " + map.get("action") + ' ';
                code = code.trim();
            }
            //单开/关
            else
            {
                String first = Long.toHexString(Long.valueOf(map.get("toaddr"))).toUpperCase();
                code = first;
                code = code + " 05 00 "  + '0' + map.get("content") + ' ' + map.get("action") + " 00 ";
                code = code.trim();
                System.out.println(code);
            }
            String replace = code.replace(" ","");
            byte[] sbuf = CRC16M.getSendBuf(replace);
            String ss = CRC16M.getBufHexStr(sbuf);
            code = code + ' ' + ss;
            code = code.trim();
            map.put("code",code);
        }
        String log = "产业:" + map.get("industryId") + " 设备: " +  map.get("deviceId") +
                " 增加了一个rule:当 " + map.get("addr") + ',' +map.get("typeName") +
                map.get("logic") +map.get("value") + " 时,执行 " + map.get("code");
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(map.get("deviceId"));
        customLogger.setType("0");
        customLogger.setMessage(log);
        Rule rule = new Rule();
        rule.setRuleId(UID.getUid());
        rule.setAddr(map.get("addr"));
        rule.setTypeName(map.get("typeName"));
        rule.setCode(map.get("code"));
        rule.setLogic(map.get("logic"));
        rule.setValue(map.get("value"));
        rule.setSwitchState("0");
        rule.setRuleId(UID.getUid());
        String res = downDataDao.addRule(map.get("deviceId"),rule);
        if(!res.equals("success"))
        {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(map.get("industryId"),customLogger);
            return "fail";
        }
        //发送到Mongo 添加rule
        long result = deviceDao.addRule(map.get("deviceId"),rule);
        if(result != 0)
        {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(map.get("industryId"),customLogger);
            Map ruleMap = new HashMap();
            Device device = deviceDao.findByDeviceId(map.get("deviceId"));
            String sensorName = sensorDao.find(map.get("deviceId"),map.get("addr")).get(0).getSensorName();
            String belong = device.getDeviceName() + '/' + sensorName;
            ruleMap.put("belongName", belong);
            ruleMap.put("ruleId",rule.getRuleId());
            ruleMap.put("deviceId", map.get("deviceId"));
            ruleMap.put("sensorAddr", rule.getAddr());
            ruleMap.put("ruleState", rule.getSwitchState().equals("0") ? false : true);
            ruleMap.put("ruleMessage", "当 " + rule.getTypeName() + " " + rule.getLogic() + " " + rule.getValue() +
                    " 时,执行485指令: " + rule.getCode());
            JSONArray jsonArray = JSONArray.fromObject(ruleMap);
            String jstr = jsonArray.toString();
            return jstr;
        }
        else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(map.get("industryId"),customLogger);
            return "fail";
        }
    }
//
//    @Override
//    public String addByDeviceId(String deviceId) {
//        Device device1;
//
//        device1 = deviceDao.addByDeviceId(deviceId);
//        if(device1 == null)
//        {
//            return "addDeviceId fail";
//        }
//        else {
//            Industry industry = industryDao.findByIndustryId(device1.getIndustryId());
//            String belong = industry.getIndustryName();
//            List<AcqUnit> units = industry.getAcqUnitList();
//            for(AcqUnit acqUnit : units)
//            {
//                if(acqUnit.getUnitId().equals(device1.getUnitId()))
//                {
//                    belong = belong + '/' + acqUnit.getUnitName();
//                    break;
//                }
//            }
//            return belong;
//        }
//    }

    /*************************************************查找********************************/

    @Override
    public String findDevice(String deviceId) {
        if(deviceDao.findByDeviceId(deviceId) == null)
            return null;
        else
            return "success";
    }


    @Override
    public List<Device> findAll(String industryId, String unitId) {
        return deviceDao.findAll(industryId,unitId);
    }


    @Override
    public String findRule(String deviceId) {
        List<Rule> ruleList = deviceDao.findAllRule(deviceId);
        if(ruleList == null)
        {
            return "fail";
        }
        JSONArray jsonArray = JSONArray.fromObject(ruleList);
        String jsonstr = jsonArray.toString();
        return jsonstr;
    }

    @Override
    public String findataALl(String industryId, String unitId) {

        List<Map> mapList = new ArrayList<Map>();
        String name;
        name = industryDao.findByIndustryId(industryId).getIndustryName() + '/';
        List<AcqUnit> acqUnits = industryDao.findByIndustryId(industryId).getAcqUnitList();
        for(int i = 0;i<acqUnits.size();i++)
        {
            if(acqUnits.get(i).getUnitId().equals(unitId))
                name+=acqUnits.get(i).getUnitName();
        }
        System.out.println("name" + name);
        List<Device> devices = deviceDao.findAll(industryId,unitId);
        for(int i = 0;i<devices.size();i++)
        {
            System.out.println("aaaaa");
            int j;
            Map map = new HashMap();
            map.put("deviceId",devices.get(i).getDeviceId());
            map.put("deviceName",devices.get(i).getDeviceName());
            map.put("sendRate",devices.get(i).getSendRate());
            map.put("deviceRemark",devices.get(i).getDeviceRemark());
            map.put("deviceBelong",name);
            map.put("linkState",devices.get(i).getLinkState());
            List<Map> sensorchildren = new ArrayList<>();
            List<Map> relaychildren = new ArrayList<>();
            List<Relay> relays = relayDao.findAll(devices.get(i).getDeviceId());
            List<SensorInfo> sensors = sensorDao.findAll(devices.get(i).getDeviceId());
//            for(SensorInfo sensorInfo : sensors)
//                System.out.println(sensorInfo.getSensorName());
            QuitSort.quickSort(sensors,0,sensors.size());
            String sname = null;
            for(j=0;j<sensors.size();j++)
            {
                System.out.println("sensor" + sensors.get(j).getSensorName());
                if(!sensors.get(j).getSensorName().equals(sname))
                {
                    Map sensormap = new HashMap();
                    sensormap.put("addr",sensors.get(j).getSensorAddr());
                    sensormap.put("name",sensors.get(j).getSensorName());
                    sname = sensors.get(j).getSensorName();
                    sensorchildren.add(sensormap);
                }
            }
            for(j = 0;j<relays.size();j++)
            {
                Map relaymap = new HashMap();
                relaymap.put("name",relays.get(j).getRelayName());
                relaymap.put("addr",relays.get(j).getRelayAddr());
                relaychildren.add(relaymap);
            }
            map.put("sensorChildren",sensorchildren);
            map.put("relayChildren",relaychildren);
            mapList.add(map);
        }
        JSONArray jsonArray = JSONArray.fromObject(mapList);
        String jsonstr = jsonArray.toString();
        return jsonstr;
    }

    /*************************************************删除********************************/

    @Override
    public String deleteDevice(String industryId,String deviceId) {
        String res;
        Device device = deviceDao.findByDeviceId(deviceId);
        String log = "产业:" + industryId + " 删除了一个设备 " + device.getDeviceName() + "_" + deviceId;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        List<SensorInfo> sensors = sensorDao.findAll(device.getDeviceId());
        if(!(sensors == null || sensors.size() == 0)){
            QuitSort.quickSort(sensors,0,sensors.size());
            String name = sensors.get(0).getSensorAddr();
            int i;
            for(i = 0;i<sensors.size();i++){
                if(!name.equals(sensors.get(i).getSensorAddr())){
                    System.out.println("del sensor " + sensors.get(i-1).getSensorAddr());
                    res = sensorService.deletesensor(device.getDeviceId(),sensors.get(i-1).getSensorAddr());
                    System.out.println(res);
                    System.out.println(res);
                    if(!res.equals("success")) {
                        customLogger.setResult("失败");
                        downDataDao.addOpsLog(industryId,customLogger);
                        return res;
                    }
                }
            }
            System.out.println("del sensor " + sensors.get(i-1).getSensorAddr());
            res = sensorService.deletesensor(device.getDeviceId(),sensors.get(i-1).getSensorAddr());
            System.out.println(res);
            if(!res.equals("success")){
                customLogger.setResult("失败");
                downDataDao.addOpsLog(industryId,customLogger);
                return res;
            }

        }
        List<Relay> relays = relayDao.findAll(device.getDeviceId());
        if(!(relays == null || relays.size() == 0)){
            for(Relay relay : relays){
                System.out.println("del relay " + relay.getRelayAddr());
                res = relayService.deleteRelay(industryId,device.getDeviceId(),relay.getRelayAddr());
                System.out.println(res);
                if(!res.equals("success")){
                    customLogger.setResult("失败");
                    downDataDao.addOpsLog(industryId,customLogger);
                    return res;
                }

            }
        }
        downDataDao.cancelsub(deviceId);
        long res1 = deviceDao.deleteByDeviceId(industryId,deviceId);
        System.out.println(res1);
        if(res1 == 0)
        {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId,customLogger);
            return "delete device success";
        }
        else{
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "delete device fail";
        }
    }

    /****************************************/
    @Override
    public String deleterule(String industryId,String deviceId,String id) {
        System.out.println(id);
        String log = "产业:" + industryId + " 设备:" + deviceId + " 删除了一条rule";
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        String res = downDataDao.deleteRule(deviceId,id);
        if(!res.equals("success")){
            System.out.println(res);
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "fail";
        }
        long result = deviceDao.deleteRule(deviceId,id);
        if(result != 0)
        {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId,customLogger);
            return "success";
        }
        else
        {

            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "fail";
        }
    }

    @Override
    public String deleteAllrule(String industryId,String deviceId) {
        String log = "产业:" + industryId + " 删除了一个设备:" +  deviceId + " 下的所有rule";
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        long result = deviceDao.deleteAllrule(deviceId);
        if(result != 0)
        {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId,customLogger);
            return "success";
        }
        else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "fail";
        }
    }

    /*************************************************更新********************************/

    @Override
    public String updateDeviceInfo(String industryId,String deviceId, String newName, String newRemark,String newSendrate) {
        Device device = deviceDao.findByDeviceId(deviceId);
        String log = "产业:" + industryId + " 更新设备:" + device.getDeviceName() + "_" + deviceId+
                        " 信息修改为:" + " name:" + newName + " remark:" + newRemark + " sendrate:" + newSendrate;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);

        String res = downDataDao.downCtl("sendrate","setSendrate",newSendrate,deviceId," ");
        if(!res.equals("success")){
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return res;
        }

        Long result = deviceDao.updateDeviceInfo(deviceId,newName,newRemark) ;
        if(result != 0)
        {
            customLogger.setResult("成功");
        }
        else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "update deviceinfo fail";
        }
        result = deviceDao.updateDeviceSendRate(deviceId,newSendrate);
        if(result != 0)
        {
            downDataDao.addOpsLog(industryId,customLogger);
            return "update deviceinfo success";
        }
        else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "update deviceinfo fail";
        }
    }
    /********************操作rule**********************/
    @Override
    public String operaterule(String industryId,String deviceId,String id,Boolean operation) {
        String log = "产业:" + industryId + " 设备:"+ deviceId+
                " 更改rule:" + id + " 的状态为:" + operation;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        String res = downDataDao.updateState(deviceId,id,operation ? "1":"0");
        if(!res.equals("success"))
        {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "fail";
        }
        long res1 = deviceDao.updateRuleSwitch(deviceId,id,operation ? "1":"0");
        if(res1 == 0)
        {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId,customLogger);
            return "fail";
        }
        else {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId,customLogger);
            return "success";
        }

    }

    /*******************测试是否入网******************/
    @Override
    public String heart(String deviceId) {
        System.out.println("heart" + deviceId);
        String heart = downDataDao.heart(deviceId);
        System.out.println(heart);
        if(heart.equals("接收数据超时"))
        {
            return "未入网";
         }
        else
            return "已入网";

    }

    /***************查询日志**********************/
    @Override
    public String findLog(String industryId, String deviceId, String starttime, String endtime) {
        return downDataDao.findLogByDeviceIdAndTime(industryId,deviceId,starttime,endtime);
    }
}
