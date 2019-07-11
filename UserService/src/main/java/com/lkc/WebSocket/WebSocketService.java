package com.lkc.WebSocket;

import com.lkc.model.industry.relayInfo.Machine;
import com.lkc.model.industry.relayInfo.Relay;
import com.lkc.model.industry.sensorInfo.SensorInfo;
import com.lkc.tool.QuitSort;
import com.lkc.FeignClient.mongoservice.RelayDao;
import com.lkc.FeignClient.mongoservice.SensorDao;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

//和客户端连接
@ServerEndpoint("/webSocket")
//在外部tomcat上需要注释掉
@Component
//第一次连接时执行一次,之后转到sendThread进行操作
public class WebSocketService {
    //日志记录器
    private Logger logger = LoggerFactory.getLogger(getClass());

    //存放连接进来的客户端
    private static CopyOnWriteArrayList<WebSocketService> webSocketSet = new CopyOnWriteArrayList<WebSocketService>();

    //记录连接进来的数量
    private static int onlineCount = 0;

    //通过session给用户发送 实时数据
    private Session session;

    //创建 一个线程安全的hashmap <session,deviceID>
    private static ConcurrentHashMap<Session, String> map = new ConcurrentHashMap<>();

    //注入sensorDao（注意static和WebSocketService.sensorDao）
    private static SensorDao sensorDao;


    private static RelayDao relayDao;

    @Autowired
    public void setRelayDao(RelayDao relayDao) {
        WebSocketService.relayDao = relayDao;
    }

    @Autowired
    public void setSensorDao(SensorDao sensorDao) {
        WebSocketService.sensorDao = sensorDao;
    }


    public ConcurrentHashMap<Session, String> getMap() {
        return map;
    }

    @OnOpen
    public void OnOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        logger.info("websocket:" + " sessionid = " + session.getId() + "连接成功！ 人数为：" + getOnlineCount());
    }

    @OnError
    public void OnError(Session session, Throwable error) {
        webSocketSet.remove(this);
        logger.warn("websocket:" + "sessionid = " + session.getId() + "连接错误！");
        logger.error(error.toString());
    }

    @OnClose
    public void OnClose(Session session) {
        map.remove(session);
        while (!webSocketSet.remove(this)) {
        }
        subOnlineCount();

        logger.info("websocket: " + "sessionid = " + session.getId() + "连接关闭，剩余人数为：" + getOnlineCount());
    }

    //操作数据
    @OnMessage
    public synchronized void OnMessage(String msg, Session session) {
        map.put(session, msg);
        //获取map里的session username deviceID
        String deviceID = msg;
        //按deviceID查询实时数据
        Map<Object, Object> data = new HashMap<>();  //总map
        List<SensorInfo> sensorList = WebSocketService.sensorDao.findAll(deviceID);  //获取sensor列表
        List<Relay> relayList = WebSocketService.relayDao.findAll(deviceID);  //获取relay列表
        QuitSort.quickSort(sensorList, 0, sensorList.size());   //sensor列表排序(使同一个sensor的不同type在一起)

        List<Map<Object, Object>> sensors = new ArrayList<>();    //存所有sensor数据
        Map<Object, Object> sensormap = new HashMap<>();   //存每个sensor的数据
        String value = null;   //存每个sensor的value

        if (sensorList == null || sensorList.size() == 0) {
            session.getAsyncRemote().sendText("deviceId 下没有sensor");
            logger.info("websocket:" + "sessionid = " + session.getId() + " 发送正常");
        } else {

            String name = sensorList.get(0).getSensorName();
            //把同一sensor的不同type在合并
            sensormap.put("name", "Time");
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(currentTime);
            sensormap.put("message", dateString);
            Map<Object, Object> mapC = new HashMap<>();
            mapC.putAll(sensormap);
            sensors.add(mapC);

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
                if (sensor.getValue() == null || sensor.getValue().isEmpty()) {
                    continue;
                }

                if (name.equals(sensor.getSensorName())) {
                    sensormap.put("name", sensor.getSensorName());
                    if (value == null) {
                        value = sensor.getType() + ":" + sensor.getValue();
                    } else {
                        value = value + "\n" + sensor.getType() + ":" + sensor.getValue();
                    }
                } else if (value == null) {
                    name = sensor.getSensorName();
                    sensormap.put("name", sensor.getSensorName());
                    value = sensor.getType() + ":" + sensor.getValue();
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

            List<Map<Object, Object>> cope = new ArrayList<>();
            cope.addAll(sensors);
            data.put("sensor", cope);
            System.out.println(sensors);

        }

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
        //relay存入data
        data.put("relay", relays);

        //转换成json格式
        JSONObject json = JSONObject.fromObject(data);
        String jsonstr = json.toString();
        //查询 成功并发送
        try {
            session.getAsyncRemote().sendText(jsonstr);
            logger.info("websocket:" + "sessionid = " + session.getId() + " 发送正常");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("websocket:" + "sessionid = " + session.getId() + " 发送异常！");
        }
    }

    //增加人数
    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    //减少人数
    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }

    //查看连接人数
    public static synchronized int getOnlineCount() {
        return WebSocketService.onlineCount;
    }
}
