package com.lkc.service.serviceInterface.Industry;

import com.lkc.model.industry.deviceInfo.Device;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    //查找
    String findDevice(String deviceId);

    //通过某产业Id查找其下device
    List<Device> findAll(String industryId, String unitId);

    String findRule(String deviceId);

    //设备列表显示数据构造
    String findataALl(String industryId, String unitId);

    //用户添加设备
    String addDevice(String industryId, String AcqUnitId, String deviceId,
                     String deviceName, String deviceRemark, String sendRate);

    //设备连到服务器
   // String addByDeviceId(String deviceId);

    //删除
    String deleteDevice(String industryId, String deviceId);

    //更新
    String updateDeviceInfo(String industryId, String deviceId,
                            String newName, String newRemark, String SendRate);

    //增加一条rule
    String addrule(Map<String, String> map);

    //删除一条rule
    String deleterule(String industryId, String deviceId, String id);

    //删除某device下的所有rule
    String deleteAllrule(String industryId, String deviceId);

    //操作rule
    String operaterule(String industryId, String deviceId, String id, Boolean operation);


    String heart(String deviceId);

    //查询日志
    String findLog(String industryId, String deviceId, String starttime, String endtime);

}
