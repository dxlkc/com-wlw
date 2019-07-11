package com.lkc.service.Impl;

import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.model.Log.CustomLogger;
import com.lkc.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService {
    @Resource
    private InfluxdbDao influxdbDao;

    public void addOpsLog(String industryId, CustomLogger customLogger) {
        Map<String, String> tags = new HashMap<>();
        tags.put("type", customLogger.getType());
        tags.put("deviceId", customLogger.getDeviceId());

        Map<String, Object> fields = new HashMap<>();
        fields.put("message", customLogger.getMessage());
        fields.put("result", customLogger.getResult());

        influxdbDao.insert(industryId + "_opslog", tags, fields);
    }

}
