package com.lkc.service.Impl;

import com.lkc.FeignClient.MongoFeignClient;
import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.model.Industry.sensorInfo.SensorInfo;
import com.lkc.model.ReturnUser.HistoryData;
import com.lkc.model.ReturnUser.ReturnData;
import com.lkc.service.HistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Resource
    private InfluxdbDao influxdbDao;
    @Resource
    private MongoFeignClient mongoFeignClient;

    public List<ReturnData> findHistory(String start, String end, String deviceId) {
        List<ReturnData> res = new ArrayList<>();
        List<SensorInfo> sensorInfos = mongoFeignClient.findAll(deviceId);

        for (SensorInfo sensorInfo : sensorInfos) {
            ReturnData returnData = new ReturnData();
            returnData.setDeviceId(deviceId);
            returnData.setType(sensorInfo.getType());

            String measurement = deviceId + "_" + sensorInfo.getSensorAddr() + "_" + sensorInfo.getType();
            ArrayList<HistoryData> list =
                    influxdbDao.findByTime(start, end, measurement);

            if (0 == list.size()) {
                continue;
            }

            SensorInfo sensorInfo1 = mongoFeignClient.findByAddrAndType(deviceId, sensorInfo.getSensorAddr(), sensorInfo.getType());
            if (null != sensorInfo1) {
                if (!"".equals(sensorInfo1.getMax()) || !"".equals(sensorInfo1.getMin())) {
                    returnData.setMax(Float.valueOf(sensorInfo1.getMax()));
                    returnData.setMin(Float.valueOf(sensorInfo1.getMin()));

                    returnData.setData(list);
                    res.add(returnData);
                }
            }
        }

        return res;
    }

}
