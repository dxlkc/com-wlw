package com.lkc.InfluxdbDao.Impl;

import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.config.InfluxdbConfig;
import com.lkc.model.Log.CustomLogger;
import com.lkc.model.ReturnUser.HistoryData;
import com.lkc.tool.TimeChange;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class InfluxdbDaoImpl implements InfluxdbDao {
    @Resource
    private InfluxDB influxdb;
    @Resource
    private InfluxdbConfig influxdbConfig;

    //添加信息
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);
        influxdb.write(influxdbConfig.getDbName(), null, builder.build());
    }

    //查找历史"信息"  返回一个List ok
    public ArrayList findByTime(String starttime, String endtime, String measurement) {
        //将值全部存入list
        ArrayList<HistoryData> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" where " + "time>=" + "'" + starttime + "'" + " and " + "time<=" + "'" + endtime + "'" + " tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String values = (value.get(1) == null) ? null : value.get(1).toString();

                    HistoryData historyData = new HistoryData();
                    historyData.setTime(TimeChange.dbtimeTonNormal(time));  //influxdb  time不可能为null
                    historyData.setValue(values);
                    res.add(historyData);
                }
            }
        }
        return res;
    }

    //查询"最新信息" ok
    public ArrayList findNewest(String measurement) {
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" order by time desc limit 1 tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String values = (value.get(1) == null) ? null : value.get(1).toString();
                    ArrayList<String> list = new ArrayList<>();
                    list.add(TimeChange.dbtimeTonNormal(time)); //influxdb  time不可能为null
                    list.add(values);
                    res.add(list);
                }
            }
        }
        return res;
    }

    //删除表 ok
    public String deleteMeasurement(String measurement) {
        QueryResult result = influxdb.query(new Query("drop measurement " + measurement, influxdbConfig.getDbName()));
        return result.getError();
    }

    /*********************************************************************************/

    //查找"历史操作"  一段时间内的
    public ArrayList<CustomLogger> findLogByTime(String starttime, String endtime, String measurement) {
        //将值全部存入list
        ArrayList<CustomLogger> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" where " + "time>=" + "'" + starttime + "'" + " and " + "time<=" + "'" + endtime + "'" + " tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String type = (value.get(1) == null) ? null : value.get(1).toString();
                    String deviceId = (value.get(2) == null) ? null : value.get(2).toString();
                    String message = (value.get(3) == null) ? null : value.get(3).toString();
                    String result = (value.get(4) == null) ? null : value.get(4).toString();

                    CustomLogger customLogger = new CustomLogger();
                    customLogger.setTime(TimeChange.dbtimeTonNormal(time)); //influxdb  time不可能为null
                    customLogger.setType(type);
                    customLogger.setDeviceId(deviceId);
                    customLogger.setMessage(message);
                    customLogger.setResult(result);

                    res.add(customLogger);
                }
            }
        }
        return res;
    }

    //查找"历史操作"  所有
    public ArrayList<CustomLogger> findAllLog(String measurement) {
        //将值全部存入list
        ArrayList<CustomLogger> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String type = (value.get(1) == null) ? null : value.get(1).toString();
                    String deviceId = (value.get(2) == null) ? null : value.get(2).toString();
                    String message = (value.get(3) == null) ? null : value.get(3).toString();
                    String result = (value.get(4) == null) ? null : value.get(4).toString();

                    CustomLogger customLogger = new CustomLogger();
                    customLogger.setTime(TimeChange.dbtimeTonNormal(time));  //influxdb  time不可能为null
                    customLogger.setType(type);
                    customLogger.setDeviceId(deviceId);
                    customLogger.setMessage(message);
                    customLogger.setResult(result);

                    res.add(customLogger);
                }
            }
        }
        return res;
    }

    //查找"历史操作" 某个设备的所有操作
    public ArrayList<CustomLogger> findLogByDeviceId(String DeviceId, String measurement) {
        //将值全部存入list
        ArrayList<CustomLogger> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" where deviceId=" + "'" + DeviceId + "'" + " tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String type = (value.get(1) == null) ? null : value.get(1).toString();
                    String deviceId = (value.get(2) == null) ? null : value.get(2).toString();
                    String message = (value.get(3) == null) ? null : value.get(3).toString();
                    String result = (value.get(4) == null) ? null : value.get(4).toString();

                    CustomLogger customLogger = new CustomLogger();
                    customLogger.setTime(TimeChange.dbtimeTonNormal(time)); //influxdb  time不可能为null
                    customLogger.setType(type);
                    customLogger.setDeviceId(deviceId);
                    customLogger.setMessage(message);
                    customLogger.setResult(result);

                    res.add(customLogger);
                }
            }
        }
        return res;
    }

    //查找"历史操作" 某个设备的一段时间内的操作
    public ArrayList<CustomLogger> findLogByDeviceIdAndTime(String starttime, String endtime, String DeviceId, String measurement) {
        //将值全部存入list
        ArrayList<CustomLogger> res = new ArrayList<>();
        Query query = new Query(
                "select * from \"" + measurement + "\" where " + "deviceId=" + "'" + DeviceId + "'" + " and " + "time>=" + "'" + starttime + "'" + " and " + "time<=" + "'" + endtime + "'" + " tz('Asia/Shanghai')", influxdbConfig.getDbName());

        //以下需要封装（传一个Query 返回一个list）
        QueryResult queryResult = influxdb.query(query);
        QueryResult.Result oneResult = queryResult.getResults().get(0); //返回单条sql语句的返回值

        if (oneResult.getSeries() != null) {
            List<List<Object>> valueList = oneResult.getSeries().stream().map(QueryResult.Series::getValues).collect(Collectors.toList()).get(0);

            if (valueList != null && valueList.size() > 0) {
                for (List<Object> value : valueList) {
                    //取字段值
                    String time = (value.get(0) == null) ? null : value.get(0).toString();
                    String type = (value.get(1) == null) ? null : value.get(1).toString();
                    String deviceId = (value.get(2) == null) ? null : value.get(2).toString();
                    String message = (value.get(3) == null) ? null : value.get(3).toString();
                    String result = (value.get(4) == null) ? null : value.get(4).toString();

                    CustomLogger customLogger = new CustomLogger();
                    customLogger.setTime(TimeChange.dbtimeTonNormal(time)); //influxdb  time不可能为null
                    customLogger.setType(type);
                    customLogger.setDeviceId(deviceId);
                    customLogger.setMessage(message);
                    customLogger.setResult(result);

                    res.add(customLogger);
                }
            }
        }
        return res;
    }
}
