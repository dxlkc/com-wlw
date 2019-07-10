package com.lkc.InfluxdbDao;

import com.lkc.model.Log.CustomLogger;

import java.util.ArrayList;
import java.util.Map;

public interface InfluxdbDao {

    void insert(String measurement, Map<String, String> tags, Map<String, Object> fields);

    ArrayList findByTime(String starttime, String endtime, String measurement);

    String deleteMeasurement(String measurement);

    /*******************************************************/

    ArrayList<CustomLogger> findLogByTime(String starttime, String endtime, String measurement);

    ArrayList<CustomLogger> findAllLog(String measurement);

    ArrayList<CustomLogger> findLogByDeviceId(String DeviceId, String measurement);

    ArrayList<CustomLogger> findLogByDeviceIdAndTime(String starttime, String endtime, String DeviceId, String measurement);
}
