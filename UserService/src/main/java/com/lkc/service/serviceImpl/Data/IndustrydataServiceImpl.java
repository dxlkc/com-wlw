package com.lkc.service.serviceImpl.Data;


import com.lkc.model.industry.deviceInfo.Device;
import com.lkc.model.industry.deviceInfo.Rule;
import com.lkc.model.industry.industryInfo.AcqUnit;
import com.lkc.model.industry.industryInfo.Industry;
import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.model.returndata.Industrydata;
import com.lkc.service.serviceInterface.Data.IndustrydataService;
import com.lkc.tool.Gps;
import com.lkc.tool.Maptrans;
import com.lkc.tool.QuitSort;
import com.lkc.FeignClient.mongoservice.DeviceDao;
import com.lkc.FeignClient.mongoservice.IndustryDao;
import com.lkc.FeignClient.mongoservice.SensorDao;
import com.lkc.model.returndata.Industrygps;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class IndustrydataServiceImpl implements IndustrydataService {
    @Resource
    private IndustryDao industryDao;
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private SensorDao sensorDao;

    //重构结构体 用于地图显示
    public Industrydata findall(String id) {
        Industrydata industrydata = new Industrydata();
        Industry industry = industryDao.findByIndustryId(id);
        industrydata.setIndustryName(industry.getIndustryName());
        industrydata.setIndustryRemark(industry.getIndustryRemark());
        industrydata.setIndustryTime(industry.getIndustryTime());
        industrydata.setIndustryUnitNum(industry.getIndustryUnitNum());

        //indust下的acqunit list
        List<AcqUnit> acqUnits = industry.getAcqUnitList();
        int devicenum = 0;
        List<Industrygps> industrygps = new ArrayList<>();

        for (int i = 0; i < acqUnits.size(); i++) {
            String unitid = acqUnits.get(i).getUnitId();
            List<Device> devices = deviceDao.findAll(id, unitid);
            devicenum += devices.size();

            for (int j = 0; j < devices.size(); j++) {
                Industrygps industrygps1 = new Industrygps();
                //Gps解码
                if (!(devices.get(j).getLongitude().equals("") && devices.get(j).getLatitude().equals(""))) {
                    Maptrans maptrans = Maptrans.getIntance();
                    Gps gps1, gps2;
                    Gps gps = Gps.getIntance();
                    gps.setWgLon(Double.valueOf(devices.get(j).getLatitude()));
                    gps.setWgLat(Double.valueOf(devices.get(j).getLongitude()));
                    gps1 = maptrans.gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());

                    if (gps1 == null) {
                        continue;
                    }

                    gps2 = maptrans.gcj02_To_Bd09(gps1.getWgLat(), gps1.getWgLon());
                    industrygps1.setLatitude(String.valueOf(gps2.getWgLat()));
                    industrygps1.setLongitude(String.valueOf(gps2.getWgLon()));
                }
                //存入到变量
                industrygps1.setUnitId(unitid);
                industrygps1.setUnitName(acqUnits.get(i).getUnitName());
                industrygps1.setDeviceId(devices.get(j).getDeviceId());
                industrygps1.setDeviceName(devices.get(j).getDeviceName());
                industrygps1.setDeviceRemark(devices.get(j).getDeviceRemark());
                industrygps1.setSendRate(devices.get(j).getSendRate());
                //加入list
                industrygps.add(industrygps1);
            }
        }
        industrydata.setIndustryDeviceNum(String.valueOf(devicenum));
        industrydata.setIndustryGps(industrygps);

        return industrydata;
    }

    @Override
    public String findbyValue(String industryId) {
        List<Map<Object, Object>> mapList = new ArrayList<>();
        Industry industry = industryDao.findByIndustryId(industryId);
        if (industry == null) {
            return "fail";
        }
        List<AcqUnit> acqUnits = industry.getAcqUnitList();
        for (AcqUnit acqUnit : acqUnits) {
            Map<Object, Object> map = new HashMap<>();
            String name = acqUnit.getUnitName() + '_' + acqUnit.getUnitId();
            map.put("label", name);

            List<Device> devices = deviceDao.findAll(industryId, acqUnit.getUnitId());
            List<Map<String, String>> mapList1 = new ArrayList<>();

            for (Device device : devices) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("value", device.getDeviceId());
                map1.put("label", device.getDeviceName());

                mapList1.add(map1);
            }
            map.put("children", mapList1);
            mapList.add(map);
        }
        JSONArray jsonArray = JSONArray.fromObject(mapList);
        return jsonArray.toString();
    }

    @Override
    public String findbyrule(String industryId) {
        Map<Object, Object> Data = new HashMap<>();
        List<Map<Object, Object>> options = new ArrayList<>();
        List<Map<Object, Object>> ruleData = new ArrayList<>();

        Industry industry = industryDao.findByIndustryId(industryId);
        List<AcqUnit> units = industry.getAcqUnitList();
        for (AcqUnit acqUnit : units) {
            Map<Object, Object> unitMap = new HashMap<>();
            unitMap.put("value", acqUnit.getUnitId());
            unitMap.put("label", acqUnit.getUnitName() + '(' + acqUnit.getUnitId() + ')');
            List<Map> unitChildren = new ArrayList<>();
            List<Device> devices = deviceDao.findAll(industryId, acqUnit.getUnitId());
            for (Device device : devices) {
                //构造rulelist
                List<Rule> ruleList = deviceDao.findAllRule(device.getDeviceId());
                if (!(ruleList == null || ruleList.size() == 0)) {
                    QuitSort.quickSortByrule(ruleList, 0, ruleList.size());
                    for (Rule rule : ruleList) {
                        Map<Object, Object> ruleMap = new HashMap<>();
                        ruleMap.put("ruleId", rule.getRuleId());
                        ruleMap.put("deviceId", device.getDeviceId());
                        ruleMap.put("sensorAddr", rule.getAddr());
                        ruleMap.put("ruleState", !rule.getSwitchState().equals("0"));
                        List<SensorInfo> sensorInfo = sensorDao.find(device.getDeviceId(), rule.getAddr());
                        String sensorName = "";
                        if (sensorInfo.size() == 0 || sensorInfo == null) {
                            sensorName = "";
                        } else {
                            sensorName = sensorInfo.get(0).getSensorName();
                        }
                        String belong = device.getDeviceName() + '/' + sensorName;
                        ruleMap.put("belongName", belong);
                        String logic = "<";
                        if (rule.getLogic().equals("moreThan")) {
                            logic = ">";
                        } else if (rule.getLogic().equals("notLessThan")) {
                            logic = ">=";
                        } else if (rule.getLogic().equals("notMoreThan")) {
                            logic = "<=";
                        }
                        ruleMap.put("ruleMessage", "当 " + rule.getTypeName() + " " + logic + " " + rule.getValue() +
                                " 时,执行485指令: " + rule.getCode());
                        ruleData.add(ruleMap);
                    }
                }

                //构造unitChildren
                Map<Object, Object> devMap = new HashMap<>();
                devMap.put("value", device.getDeviceId());
                devMap.put("label", device.getDeviceName() + '(' + device.getDeviceId() + ')');
                List<Map> devChildren = new ArrayList<>();
                List<SensorInfo> sensors = sensorDao.findAll(device.getDeviceId());
                Map<Object, Object> senMap = new HashMap<>();
                List<Map<Object, Object>> senChildren = new ArrayList<>();

                if (sensors.size() == 0 || sensors == null) {
                    devMap.put("children", null);
                    unitChildren.add(devMap);
                    continue;
                }
                QuitSort.quickSort(sensors, 0, sensors.size());
                String tempName = sensors.get(0).getSensorName();
                senMap.put("value", sensors.get(0).getSensorAddr());
                senMap.put("label", sensors.get(0).getSensorName() + '(' + sensors.get(0).getSensorAddr() + ')');
                for (SensorInfo sensor : sensors) {
                    if (sensor.getSensorName().equals(tempName)) {
                        Map<Object, Object> typeMap = new HashMap<>();
                        typeMap.put("value", sensor.getType());
                        typeMap.put("label", sensor.getType());
                        senChildren.add(typeMap);
                    } else {
                        List<Map<Object, Object>> listB = new ArrayList<>();
                        listB.addAll(senChildren);
                        senMap.put("children", listB);
                        Map<Object, Object> mapB = new HashMap<>();
                        mapB.putAll(senMap);
                        devChildren.add(mapB);
                        senChildren.clear();
                        senMap.clear();
                        senMap.put("value", sensor.getSensorAddr());
                        senMap.put("label", sensor.getSensorName() + '(' + sensor.getSensorAddr() + ')');
                        tempName = sensor.getSensorName();
                        Map<Object, Object> typeMap = new HashMap<>();
                        typeMap.put("value", sensor.getType());
                        typeMap.put("label", sensor.getType());
                        senChildren.add(typeMap);
                    }
                }
                List<Map<Object, Object>> listB = new ArrayList<>();
                listB.addAll(senChildren);
                senMap.put("children", listB);
                Map<Object, Object> mapB = new HashMap<>();
                mapB.putAll(senMap);
                devChildren.add(mapB);

                devMap.put("children", devChildren);
                unitChildren.add(devMap);
            }
            unitMap.put("children", unitChildren);
            options.add(unitMap);
        }
        Data.put("options", options);
        Data.put("ruleData", ruleData);
        JSONArray jsonArray = JSONArray.fromObject(Data);
        return jsonArray.toString();
    }
}
