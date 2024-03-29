package com.lkc.service.serviceImpl.IndustryImpl;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.CustomLogger;
import com.lkc.model.industry.sensorInfo.Sensor;
import com.lkc.model.industry.sensorInfo.SensorDefault;
import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.model.industry.sensorInfo.Type;
import com.lkc.repository.SensorRepository;
import com.lkc.FeignClient.mongoservice.SensorDao;
import com.lkc.service.serviceInterface.Industry.SensorService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SensorServiceImpl implements SensorService {
    @Resource
    SensorDao sensorDao;
    @Resource
    DownDataDao downDataDao;
    @Resource
    SensorRepository sensorRepository;

    /**********************************8查找**********************************/

    @Override
    public String returnbefore(String deviceId, String sensorAddr) {
        Map<Object, Object> reb = new HashMap<>();
        List<SensorInfo> sensorInfos = sensorDao.find(deviceId, sensorAddr);
        if (sensorInfos == null || sensorInfos.size() == 0) {
            return "sensorlist fail";
        }

        reb.put("deviceId", deviceId);
        reb.put("sensorName", sensorInfos.get(0).getSensorName());
        reb.put("sensorAddr", sensorAddr);
        List<Map> typemap = new ArrayList<>();
        for (SensorInfo sensorInfo : sensorInfos) {
            Map<Object, Object> type = new HashMap<>();
            type.put("typeName", sensorInfo.getType());
            type.put("max", sensorInfo.getMax());
            type.put("min", sensorInfo.getMin());
            typemap.add(type);
        }
        reb.put("typeList", typemap);
        JSONArray jsonArray = JSONArray.fromObject(reb);
        return jsonArray.toString();
    }


    @Override
    public String sensordetail(String deviceId, String sensorAddr) {
        Map<Object, Object> detail = new HashMap<>();
        detail.put("sensorAddr", sensorAddr);
        List<SensorInfo> sensorInfos = sensorDao.find(deviceId, sensorAddr);

        if (sensorInfos == null || sensorInfos.size() == 0) {
            return "sensordetail fail";
        }

        detail.put("deviceId", deviceId);
        detail.put("sensorName", sensorInfos.get(0).getSensorName());
        detail.put("code", sensorInfos.get(0).getCode());
        detail.put("returnLength", sensorInfos.get(0).getReturnLength() == null ? "null" :
                sensorInfos.get(0).getReturnLength());
        List<Map<Object, Object>> typeList = new ArrayList<>();
        for (SensorInfo sensorInfo : sensorInfos) {
            Map<Object, Object> type = new HashMap<>();
            type.put("typeName", sensorInfo.getType() == null ? "null" : sensorInfo.getType());
            type.put("max", sensorInfo.getMax() == null ? "null" : sensorInfo.getMax());
            type.put("min", sensorInfo.getMin() == null ? "null" : sensorInfo.getMin());
            typeList.add(type);
        }
        detail.put("typeList", typeList);
        JSONArray jsonArray = JSONArray.fromObject(detail);
        return jsonArray.toString();
    }

    @Override
    public String findsensor(String deviceId, String sensorAddr) {
        if (Integer.valueOf(sensorAddr) >= 0 && Integer.valueOf(sensorAddr) <= 9 && sensorAddr.length() == 1) {
            sensorAddr = "0" + sensorAddr;
        }
        List<SensorInfo> sensorList = sensorDao.find(deviceId, sensorAddr);
        Map<Object, Object> sensorMap = new HashMap<>();
        sensorMap.put("name", sensorList.get(0).getSensorName());
        List<Map> typelist = new ArrayList<>();
        for (SensorInfo sensor : sensorList) {
            Map<Object, Object> typemap = new HashMap<>();
            typemap.put("typeName", sensor.getType());
            typemap.put("max", sensor.getMax());
            typemap.put("min", sensor.getMin());
            typelist.add(typemap);
        }
        sensorMap.put("type", typelist);
        JSONArray jsonArray = JSONArray.fromObject(typelist);
        return jsonArray.toString();
    }

    @Override
    public List<SensorInfo> findAll(String deviceId) {
        return sensorDao.findAll(deviceId);

    }

    /*******************************添加***************************/
//thisy
    @Override
    public String addsensor(Sensor sensor) {

        String log = "产业:" + sensor.getIndustryId() + " 在设备:" + sensor.getDeviceId() + " 下添加一个传感器:" +
                sensor.getSensorName() + " addr: " + sensor.getAddr();
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(sensor.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);

        String res1 = downDataDao.addConfirm(sensor, sensor.getDeviceId());
        if (!res1.equals("success")) {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
            return res1;
        }

        List<Type> types = sensor.getTypes();
        if (types == null || types.size() == 0) {
            return "types null";
        }

        for (Type type : types) {
            if (type.getTypeName() == null || type.getByteCount() == null || type.getByteStart() == null ||
                    type.getDataDefine() == null || type.getDicimal() == null) {
                return "type empty";
            }
            SensorInfo res;

            res = sensorDao.confirmAddr(sensor.getDeviceId(), sensor.getAddr(),
                    type.getTypeName());
            if (res != null) {
                customLogger.setResult("失败");
                downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
                return "addr have";
            }

            SensorInfo sensorInfo = new SensorInfo();
            sensorInfo.setNegative(type.getNegative());
            sensorInfo.setReturnLength(sensor.getReturnLength());
            sensorInfo.setCode(sensor.getCode485());
            sensorInfo.setSensorAddr(sensor.getAddr());
            sensorInfo.setDeviceId(sensor.getDeviceId());
            sensorInfo.setSensorName(sensor.getSensorName());
            sensorInfo.setType(type.getTypeName());
            sensorInfo.setDicimal(type.getDicimal());
            sensorInfo.setDataType(type.getDataDefine());
            sensorInfo.setByteCount(type.getByteCount());
            sensorInfo.setByteStart(type.getByteStart());
            sensorInfo.setIndustryId(sensor.getIndustryId());
            sensorInfo.setMin("0");
            sensorInfo.setMax("0");

            res = sensorDao.add(sensorInfo);
            if (res == null) {
                customLogger.setResult("失败");
                downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
                return "addsensor fail";
            }
        }
        customLogger.setResult("成功");
        downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
        return "success";
    }

    @Override
    public String addDefault(Sensor sensor) {
        String log = "产业:" + sensor.getIndustryId() + " 在设备:" + sensor.getDeviceId() + " 下添加一个传感器:" +
                sensor.getSensorName() + " addr: " + sensor.getAddr();
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(sensor.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);

        int flag = 0;
        String addr = sensor.getAddr();

        List<SensorDefault> sensorDefaults = sensorRepository.findAllBySensorAddr(addr);
        System.out.println("add1 " + sensorDefaults.size());
        if (sensorDefaults == null || sensorDefaults.size() == 0) {
            return "fail";
        }

        for (SensorDefault sensorDefault : sensorDefaults) {
            SensorInfo res = null;

            res = sensorDao.confirmAddr(sensor.getDeviceId(), sensor.getAddr(),
                    sensorDefault.getType());
            if (res != null) {
                customLogger.setResult("失败");
                downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
                return "addr have";
            }
            if (flag == 0) {
                String res1 = downDataDao.downCtl("sensor", "Adddefault", sensor.getAddr(),
                        sensor.getDeviceId(), " ");
                if (!res1.equals("success")) {
                    return res1;
                }
                flag = 1;
            }
            SensorInfo sensorInfo = new SensorInfo();
            sensorInfo.setDeviceId(sensor.getDeviceId());
            sensorInfo.setSensorName(sensor.getSensorName());
            sensorInfo.setSensorAddr(sensor.getAddr());
            sensorInfo.setIndustryId(sensor.getIndustryId());
            sensorInfo.setCode(sensorDefault.getCode());
            sensorInfo.setType(sensorDefault.getType());
            sensorInfo.setByteCount(sensorDefault.getByteCount());
            sensorInfo.setByteStart(sensorDefault.getByteStart());
            sensorInfo.setDataType(sensorDefault.getDataType());
            sensorInfo.setDicimal(sensorDefault.getDicimal());
            sensorInfo.setReturnLength(sensorDefault.getReturnLength());
            sensorInfo.setMin("0");
            sensorInfo.setMax("0");
            sensorInfo.setNegative(sensorDefault.getNegative());
            if (sensorDao.add(sensorInfo) == null) {
                customLogger.setResult("失败");
                downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
                return "addsensor fail";
            }

        }
        customLogger.setResult("成功");
        downDataDao.addOpsLog(sensor.getIndustryId(), customLogger);
        return "success";
    }

    /********************************删除**************************/

    @Override
    public String deletesensor(String deviceId, String sensorAddr) {
        List<SensorInfo> sensorInfos = sensorDao.find(deviceId, sensorAddr);
        if (sensorInfos == null || sensorInfos.size() == 0) {
            return "delete sensor fail1";
        }
        SensorInfo sensorInfo = sensorInfos.get(0);
        String log = "产业:" + sensorInfo.getIndustryId() + " 在设备:" + sensorInfo.getDeviceId() + " 下删除一个传感器:" +
                sensorInfo.getSensorName() + " addr: " + sensorInfo.getSensorAddr();
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(sensorInfo.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);

        //发送到板子
        String res = downDataDao.downCtl("sensor", "Delete", sensorAddr, deviceId, " ");
        if (!res.equals("success")) {
            System.out.println(res);
            customLogger.setResult("失败");
            downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
            return "delete sensor fail3";
        }
        long result = sensorDao.delete(deviceId, sensorAddr);
        if (result == 0) {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
            return "delete sensor fail2";
        } else {
            return "success";
        }
    }

    /*******************************修改****************************/
    @Override
    public String updateThreshold(String deviceId, String sensorAddr, String type, String max, String min) {

        SensorInfo sensorInfo = sensorDao.find(deviceId, sensorAddr).get(0);
        String log = "产业:" + sensorInfo.getIndustryId() + " 在设备:" + sensorInfo.getDeviceId() + " 下更新了传感器:" +
                sensorInfo.getSensorName() + " type:" + sensorInfo.getType() + " 的阈值,max:" + max + " min:" + min;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(sensorInfo.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);
        if (Integer.valueOf(min) > Integer.valueOf(max)) {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
            return "阈值填写错误";
        }
        long result = sensorDao.updateThreshold(deviceId, sensorAddr, type, max, min);
        System.out.println("111111111111    " + result + "name " + type);
        customLogger.setResult("成功");
        downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
        return "success";
    }

    @Override
    public String updateInfo(String deviceId, String sensorAddr, String name) {
        SensorInfo sensorInfo = sensorDao.find(deviceId, sensorAddr).get(0);
        String log = "产业:" + sensorInfo.getIndustryId() + " 在设备:" + sensorInfo.getDeviceId() + " 下更新了传感器:" +
                sensorInfo.getSensorName() + " 更名为:" + name;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(sensorInfo.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);
        long result = sensorDao.updateName(deviceId, sensorAddr, name);
        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
            return "update name_success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(sensorInfo.getIndustryId(), customLogger);
            return "update name_fail";
        }
    }
}
