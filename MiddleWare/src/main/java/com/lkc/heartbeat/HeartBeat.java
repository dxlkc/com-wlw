package com.lkc.heartbeat;

import com.lkc.FeignClient.MongoFeignClient;
import com.lkc.model.DownLink.SensorDebug;
import com.lkc.model.Industry.deviceInfo.Device;
import com.lkc.mqttDown.DownHandler;
import com.lkc.tool.MyThreadPoolExecutor;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class HeartBeat {
    @Resource
    private MongoFeignClient mongoFeignClient;

    public void start() {

        MyThreadPoolExecutor.getInstance().getMyThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try{
                    //定时轮询
                    while (true) {
                        Set<String> stringSet = new HashSet<>();
                        List<Device> deviceList = mongoFeignClient.findAllDevice();


                        for (Device device : deviceList) {
                            if (stringSet.contains(device.getDeviceId())){
                                continue;
                            }
                            stringSet.add(device.getDeviceId());
                            //构建唯一 id
                            String uid = UUID.randomUUID().toString();

                            //构建发送的信息结构
                            SensorDebug sensorDebug = new SensorDebug();
                            sensorDebug.setContent("heart");
                            sensorDebug.setOperation("heart");
                            sensorDebug.setTarget("heart");
                            sensorDebug.setTopic(device.getDeviceId() + "_" + uid + "_heart");

                            //转成json字符串
                            JSONObject jsonObject = JSONObject.fromObject(sensorDebug);
                            String message = jsonObject.toString();

                            //发送
                            DownHandler downHandler = new DownHandler();
                            String receive = downHandler.sendDownMesg(device.getDeviceId(), uid, sensorDebug.getTopic(), message);

                            //修改状态
                            if (!"接收数据超时".equals(receive)){
                                mongoFeignClient.updateLinkState(device.getDeviceId(),"已入网");
                            } else {
                                mongoFeignClient.updateLinkState(device.getDeviceId(),"未入网");
                            }
                        }

                        //定时
                        try {
                            Thread.sleep(300000);
                        } catch (Exception e){
                            e.printStackTrace();
                            start();
                        }

                    }
                } catch (Exception e){
                    start();
                }
            }
        });
    }
}
