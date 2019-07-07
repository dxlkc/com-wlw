package com.lkc.mqttUp;

import com.lkc.FeignClient.UserFeignClient;
import com.lkc.InfluxdbDao.InfluxdbDao;
import com.lkc.FeignClient.MongoFeignClient;
import com.lkc.tool.MyThreadPoolExecutor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//永远开启  用于接收上行环境数据
public class UpCallback implements MqttCallback {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    //连接断开
    @Override
    public void connectionLost(Throwable throwable) {
        logger.warn("mqtt proxy : 连接断开，可以重连...");
        while (true) {
            try {
                Thread.sleep(4000);
                logger.warn("mqtt proxy : 正在尝试重连...");
                UpMqtt.getInstance().connect();
            } catch (MqttException e) {
                logger.warn("mqtt proxy : 重连失败...");
                //打印日志
                continue;
            } catch (InterruptedException e) {
                continue;
            }
            return;
        }
    }

    //发送信息成功时 回调
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("deliveryComplete-----" + iMqttDeliveryToken.isComplete());
    }

    //接收信息成功时 回调
    //是个单线程操作 所以最好把 数据处理扔到线程里
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        logger.info("上行--收到环境数据");

        //进行数据解析
        MyThreadPoolExecutor.getInstance().getMyThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //预警处理专用 Map
                Map<String, String> threshold = new HashMap<>();
                //纬度 度
                double latitude_degree = -1;
                //纬度 分
                double latitude_minute = -1;
                //经度 度
                double longitude_degree = -1;
                //经度 分
                double longitude_minute = -1;
                //详细位置
                String RMC_detail = "";
                //继电器八路开关状态
                String relayState;

                //base64解密
                Base64.Decoder decoder = Base64.getDecoder();
                String json = new String(mqttMessage.getPayload());
                String jsonstr = new String(decoder.decode(json));
                //转换成jsonObject
                JSONObject jsonObject = JSONObject.fromObject(jsonstr);

                //第一层获取固定值 MAC, Time, sendRate
                String MAC = jsonObject.getString("MAC");
                String sendRate = jsonObject.getString("sendRate");
                String Time = jsonObject.getString("Time");

                JSONArray Sensors = null;
                //第一层获取 不定长对象数组 Data
                if (null != jsonObject.getJSONArray("Sensors")) {
                    Sensors = jsonObject.getJSONArray("Sensors");
                }

                if (Sensors.size() > 0) {

                    for (int i = 0; i < Sensors.size(); ++i) {
                        JSONObject object = Sensors.getJSONObject(i);
                        String addr = object.getString("addr");
                        String code = object.getString("code_485");

                        Map<String, String> Data = object.getJSONObject("Data");

                        for (Map.Entry<String, String> entry : Data.entrySet()) {
                            String type = entry.getKey();
                            String value = entry.getValue();

                            switch (type) {
                                case "latitude-degree":
                                    latitude_degree = Double.valueOf(value);
                                    break;
                                case "latitude-direction":
                                    break;
                                case "latitude-minute":
                                    latitude_minute = Double.valueOf(value);
                                    break;
                                case "longitude-degree":
                                    longitude_degree = Double.valueOf(value);
                                    break;
                                case "longitude-direction":
                                    break;
                                case "longitude-minute":
                                    longitude_minute = Double.valueOf(value);
                                    break;
                                case "RMC_detail":
                                    RMC_detail = value;
                                    break;
                                default:
                                    Map<String, String> tags = new HashMap<>();
                                    Map<String, Object> field = new HashMap<>();

                                    //传感器没有 relayState
                                    if (type.equals("relayState")) {
                                        field.put("value", Integer.toBinaryString(Integer.valueOf(value)));
                                        //状态存入 influxdb
                                        SaveData.saveData.insertToInfluxdb(MAC + "_" + code.substring(0, 2).toUpperCase() + "_" + type, tags, field);
                                        //修改mongodb relay和machine的状态
                                        SaveData.saveData.updateRelayState(MAC, code.substring(0, 2), value);
                                        break;
                                    } else {
                                        field.put("value", value);
                                        //存入 预警处理专用的 Map中
                                        threshold.put(type,value);
                                    }

                                    //环境数据存influxdb
                                    SaveData.saveData.insertToInfluxdb(MAC + "_" + code.substring(0, 2) + "_" + type, tags, field);
                                    // 调用mongodb 存传感器最新值
                                    SaveData.saveData.updateSensorValue(MAC, addr, type, value, Time);
                            } //end switch
                        } //end second for
                    } //end first for

                    //调用mongodb 修改经纬度 和 sendrate
                    if (-1 != latitude_degree && -1 != latitude_minute && -1 != longitude_degree && -1 != longitude_minute) {
                        latitude_degree += latitude_minute / 60.0;
                        longitude_degree += longitude_minute / 60.0;
                        // 调用mongodb 存经纬度 和 sendRate
                        SaveData.saveData.updataLocation(MAC, String.valueOf(longitude_degree), String.valueOf(latitude_degree), RMC_detail);
                        SaveData.saveData.updateSendRate(MAC, sendRate);
                    } else {
                        // 调用mongodb 存经 sendRate
                        SaveData.saveData.updateSendRate(MAC, sendRate);
                    }
                    // 调用user service 进行 预警处理
                    SaveData.saveData.thresholdHandler(MAC, threshold, Time);
                } //end if
            } //end run
        });
    }

    //这个类用于保存数据
    @Component
    public static class SaveData {
        @Resource
        private InfluxdbDao influxdbDao;
        //注入FeignClient
        @Resource
        private MongoFeignClient mongoFeignClient;
        @Resource
        private UserFeignClient userFeignClient;

        private static SaveData saveData;

        @PostConstruct
        public void init() {
            saveData = this;
            saveData.influxdbDao = this.influxdbDao;
            saveData.mongoFeignClient = this.mongoFeignClient;
            saveData.userFeignClient = this.userFeignClient;
        }

        public void insertToInfluxdb(String measurement, Map<String, String> tags, Map<String, Object> fields) {
            saveData.influxdbDao.insert(measurement, tags, fields);
        }

        public void updataLocation(String deviceId, String longitude, String latitude, String locationDetail) {
            saveData.mongoFeignClient.updateDeviceLocation(deviceId, longitude, latitude, locationDetail);
        }

        public void updateSendRate(String deviceId, String sendRate) {
            saveData.mongoFeignClient.updateDeviceSendRate(deviceId, sendRate);
        }

        public void updateSensorValue(String deviceId, String sensorAddr, String type, String value, String time) {
            saveData.mongoFeignClient.updateSensorValue(deviceId, sensorAddr, type, value, time);
        }

        public void updateRelayState(String deviceId, String relayAddr, String state){
            saveData.mongoFeignClient.updatePinsState(deviceId, relayAddr, state);
        }

        public void thresholdHandler(String deviceId, Map<String, String> map, String time){
            saveData.userFeignClient.thresholdHandler(deviceId, map, time);
        }
    }
}
