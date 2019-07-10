package com.lkc.WebSocket;


import com.lkc.FeignClient.mongoservice.RelayDao;
import com.lkc.FeignClient.mongoservice.SensorDao;
import com.lkc.model.industry.relayInfo.Machine;
import com.lkc.model.industry.relayInfo.Relay;
import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.tool.QuitSort;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SendThread {
    @Resource
    private SensorDao sensorDao;
    @Resource
    private WebSocketService webSocketService;
    @Resource
    private RelayDao relayDao;

    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    //发送实时数据
    public void send() {

        ConcurrentHashMap<Session, String> Sessionmap = webSocketService.getMap();

        for (Map.Entry<Session, String> entry : Sessionmap.entrySet()) {


            System.out.println("sendThread   !111111111");
            //获取map里的session username deviceID
            Session session = entry.getKey();
            String deviceID = entry.getValue();
            //按deviceID查询实时数据
            Map<Object, Object> data = new HashMap<>();  //总map
            List<SensorInfo> sensorList = sensorDao.findAll(deviceID);  //获取sensor列表
            List<Relay> relayList = relayDao.findAll(deviceID);  //获取relay列表
            QuitSort.quickSort(sensorList, 0, sensorList.size());   //sensor列表排序(使同一个sensor的不同type在一起)

            List<Map> sensors = new ArrayList<>();    //存所有sensor数据
            Map<Object, Object> sensormap = new HashMap<>();   //存每个sensor的数据
            String value = null;   //存每个sensor的value

            if (sensorList == null || sensorList.size() == 0) {
                session.getAsyncRemote().sendText("deviceId 下没有sensor");
                logger.info("websocket:" + "sessionid = " + session.getId() + " 发送正常");
            } else {
                String name = sensorList.get(0).getSensorName();
                sensormap.put("name","Time");
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = formatter.format(currentTime);
                sensormap.put("message",dateString);
                Map<Object, Object> mapC = new HashMap<>();
                mapC.putAll(sensormap);
                sensors.add(mapC);
                //把同一sensor的不同type在合并
                for (SensorInfo sensor : sensorList) {
                    switch (sensor.getType()) {
                        case "latitude-degree":
                        case "latitude-direction":
                        case "latitude-minute":
                        case "longitude-degree":
                        case "longitude-direction":
                        case "longitude-minute":
                        case "RMC_detail":
                            continue;
                    }
                    if (sensor.getValue() == null || sensor.getValue().isEmpty())
                        continue;

                    if (name.equals(sensor.getSensorName())) {
                        sensormap.put("name", sensor.getSensorName());
                        if (value == null)
                            value = sensor.getType() + ":" + sensor.getValue();
                        else
                            value = value + "\n" + sensor.getType() + ":" + sensor.getValue();
                    } else if (value == null) {
                        name = sensor.getSensorName();
                        sensormap.put("name",sensor.getSensorName());
                        value = sensor.getType() + ":" + sensor.getValue();
                        continue;
                    } else {
                        sensormap.put("message", value);
                        Map<Object, Object> mapB = new HashMap<>();
                        mapB.putAll(sensormap);
                        sensors.add(mapB);
                        sensormap.put("name", sensor.getSensorName());
                        value = sensor.getType() + ":" + sensor.getValue();
                        name = sensor.getSensorName();
                    }
                }
                //sensor存入data
                sensormap.put("message", value);
                sensors.add(sensormap);


                List<Map> cope = new ArrayList<>();
                cope.addAll(sensors);
                data.put("sensor", cope);
                System.out.println(sensors);

                //relay
                List<Map<Object, Object>> relays = new ArrayList<>();
                for (Relay relay : relayList) {
                    int state = Integer.valueOf(relay.getPinsState());
                    Map<Object, Object> relayMap = new HashMap<>();
                    relayMap.put("addr", relay.getRelayAddr());
                    relayMap.put("name", relay.getRelayName());
                    List<Machine> machines = relay.getMachineList();
                    List<Map> maclist = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        int flag = 0;
                        int z = (int) state % 10;
                        state = state / 10;
                        Map<Object, Object> mac = new HashMap<>();
                        mac.put("name", "无设备");
                        mac.put("position", String.valueOf(i));
                        for (Machine machine : machines) {
                            if (machine.getMachinePosition().equals(String.valueOf(i))) {
                                mac.put("name", machine.getMachineName());
                            }

                        }
                        mac.put("message", !String.valueOf(z).equals("0"));
                        maclist.add(mac);
                    }

                    relayMap.put("valueList", maclist);
                    relays.add(relayMap);
                }
                System.out.println(relays);
                //relay存入data
                data.put("relay", relays);
                //转换成json格式
                JSONObject json = JSONObject.fromObject(data);
                String jsonstr = json.toString();

                System.out.println(jsonstr);
                //查询 成功并发送
                try {
                    session.getAsyncRemote().sendText(jsonstr);
                    //session.getBasicRemote().sendText(str);
                    logger.info("websocket:" + "sessionid = " + session.getId() + " 发送正常");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("websocket:" + "sessionid = " + session.getId() + " 发送异常！");
                }
            }
        }
    }
}
