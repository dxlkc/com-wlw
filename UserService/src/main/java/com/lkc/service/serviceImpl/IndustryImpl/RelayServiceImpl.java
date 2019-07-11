package com.lkc.service.serviceImpl.IndustryImpl;

import com.lkc.FeignClient.mqttservice.DownDataDao;
import com.lkc.model.CustomLogger;
import com.lkc.model.industry.relayInfo.Machine;
import com.lkc.model.industry.relayInfo.Relay;
import com.lkc.tool.CRC16M;
import com.lkc.FeignClient.mongoservice.RelayDao;
import com.lkc.service.serviceInterface.Industry.RelayService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelayServiceImpl implements RelayService {
    @Resource
    RelayDao relayDao;
    @Resource
    DownDataDao downDataDao;

    /***********************************查找******************************/
    @Override
    public String findRelay(String deviceId) {
        List<Relay> relays = relayDao.findAll(deviceId);
        List<Map> relayList = new ArrayList<>();

        for (Relay relay : relays) {
            Map<String, String> rmap = new HashMap<>();
            rmap.put("label", relay.getRelayName() + '(' + relay.getRelayAddr() + ')');
            rmap.put("value", relay.getRelayAddr());
            relayList.add(rmap);
        }
        JSONArray jsonArray = JSONArray.fromObject(relayList);
        return jsonArray.toString();
    }

    public List<Relay> findAll(String deviceId) {
        return relayDao.findAll(deviceId);
    }

    public Relay find(String industryId, String deviceId) {
        return relayDao.find(industryId, deviceId);
    }

    //relay详细信息
    @Override
    public String findRelaydata(String deviceId, String addr) {
        Relay relay = relayDao.find(deviceId, addr);
        if (relay == null) {
            return "find fail";
        }
        Map<Object, Object> relayMap = new HashMap<>();
        int state = Integer.valueOf(relay.getPinsState());
        relayMap.put("relayName", relay.getRelayName());
        relayMap.put("relayAddr", relay.getRelayAddr());
        relayMap.put("deviceId", deviceId);
        List<Map<String, String>> message = new ArrayList<>();
        List<Machine> machines = relay.getMachineList();
        for (int i = 0; i < 8; i++) {
            int flag = 0;
            int z = (int) state % 10;
            state = state / 10;
            Map<String, String> mach = new HashMap<>();
            mach.put("machinePosition", String.valueOf(i));
            for (Machine machine : machines) {
                if (Integer.valueOf(machine.getMachinePosition()) == i) {
                    flag = 1;
                    mach.put("machineName", machine.getMachineName());
                    mach.put("machineState", machine.getMachineState().equals("0") ? "off" : "on");
                    break;
                }
            }
            if (flag == 0) {
                mach.put("machineName", "无设备");
                mach.put("machineState", String.valueOf(z).equals("0") ? "off" : "on");
            }
            message.add(mach);
        }
        relayMap.put("relayMessage", message);
        JSONArray jsonArray = JSONArray.fromObject(relayMap);
        return jsonArray.toString();
    }

    @Override
    public String returnbefore(String deviceId, String relayAddr) {
        Relay relay = relayDao.find(deviceId, relayAddr);
        if (relay == null) {
            return "relay fail";
        }
        int state = Integer.valueOf(relay.getPinsState());
        Map<Object, Object> reb = new HashMap<>();
        reb.put("relayAddr", relayAddr);
        reb.put("relayName", relay.getRelayName());
        reb.put("deviceId", deviceId);
        List<Machine> machines = relay.getMachineList();
        if (machines == null || machines.size() == 0) {
            reb.put("machineList", " ");
        }
        List<Map> machineList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int flag = 0;
            state = state / 10;
            Map<Object, Object> m = new HashMap<>();
            m.put("seen", false);
            m.put("machinePosition", String.valueOf(i));
            for (Machine machine : machines) {
                if (machine.getMachinePosition().equals(String.valueOf(i))) {
                    flag = 1;
                    m.put("machineName", machine.getMachineName());
                }
            }
            if (flag == 0) {
                m.put("machineName", "null");
            }
            machineList.add(m);
        }
        reb.put("machineList", machineList);
        JSONArray jsonArray = JSONArray.fromObject(reb);
        return jsonArray.toString();
    }


    /***********************************添加******************************/


    @Override
    public String updateOradd(List<Map<String, String>> mapList) {
        if (mapList == null || mapList.size() == 0) {
            return "fail";
        }

        String res;
        for (Map<String, String> map : mapList) {
            if (!map.get("machineName").equals("null")) {
                Machine machine = relayDao.findMachine(map.get("deviceId"), map.get("relayAddr"), map.get("machinePosition"));
                if (machine != null) {
                    if (map.get("machineName").isEmpty()) {
                        res = this.updateMachineInfo(map.get("industryId"), map.get("deviceId"),
                                map.get("relayAddr"), map.get("machinePosition"), " ");
                        if (res.equals("fail")) {
                            return res;
                        }
                    }
                    else {
                        res = this.updateMachineInfo(map.get("industryId"), map.get("deviceId"),
                                map.get("relayAddr"), map.get("machinePosition"), map.get("machineName"));
                        if (res.equals("fail")) {
                            return res;
                        }
                    }

                } else {
                    res = this.addMachine(map.get("industryId"), map.get("deviceId"),
                            map.get("relayAddr"), map.get("machineName"), map.get("machinePosition"));
                    if (res.equals("fail")) {
                        return res;
                    }
                }
            }
        }
        return "success";
    }

    public String addRelay(Relay relay) {
        String log = "产业:" + relay.getIndustryId() + " 在设备:" + relay.getDeviceId() + " 下添加一个继电器:" +
                relay.getRelayName() + " addr:" + relay.getRelayAddr();
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(relay.getDeviceId());
        customLogger.setType("0");
        customLogger.setMessage(log);

        String code = Long.toHexString(Long.valueOf(relay.getRelayAddr())).toUpperCase();
        code = code + " 01 00 00 00 08 ";
        String replace = code.replace(" ", "");
        byte[] sbuf = CRC16M.getSendBuf(replace);
        String ss = CRC16M.getBufHexStr(sbuf);
        code = code + ss;
        code = code.trim();

        String res1 = downDataDao.addRelaConfirm(relay.getDeviceId(), relay.getRelayAddr(), code);
        if (!res1.equals("success")) {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(relay.getIndustryId(), customLogger);
            return res1;
        }
        if (relayDao.addRelay(relay) != null) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(relay.getIndustryId(), customLogger);
            return "success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(relay.getIndustryId(), customLogger);
            return "fail";
        }
    }


    public String addMachine(String industryId, String deviceId, String relayAddr,
                             String machineName, String machinePostion) {
        String log = "产业:" + industryId + " 在设备:" + deviceId + " 继电器:" +
                relayAddr + " 下添加一个强点设备, addr:" + machineName;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        Machine machine = new Machine();
        machine.setMachineName(machineName);
        machine.setMachinePosition(machinePostion);
        machine.setMachineState("0");

        long result = relayDao.addMachine(industryId, deviceId, relayAddr, machine);
        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);
            return "success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "fail";
        }
    }

    /***********************************删除******************************/

    public String deleteMachine(String industryId, String deviceId, String relayAddr,
                                String machinePosition) {

        String log = "产业:" + industryId + " 在设备:" + deviceId + " 继电器:" +
                relayAddr + " 下删除一个强点设备:" + machinePosition;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        long result = relayDao.deleteByMachineName(industryId, deviceId, relayAddr, machinePosition);

        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);
            return "success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "machine fail";
        }
    }

    public String deleteRelay(String industryId, String deviceId, String relayAddr) {
        Relay relay = relayDao.find(deviceId, relayAddr);
        String log = "产业:" + industryId + " 在设备:" + deviceId + " 下删除一个传感器:" +
                relay.getRelayName() + " addr: " + relayAddr;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);

        String down_result = downDataDao.downCtl("sensor", "Delete", relayAddr, deviceId, " ");
        if (!down_result.equals("success")) {
            return down_result;
        }

        long result = relayDao.deleteByRelayAddr(industryId, deviceId, relayAddr);

        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);
            return "success";
        } else {
            System.out.println("delete fail    " + result);
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "fail";
        }
    }

    /***********************************更改******************************/

    public String updateMachineInfo(String industryId, String deviceId, String relayAddr,
                                    String postion, String newName) {
        String log = "产业:" + industryId + " 在设备:" + deviceId + " 下的传感器:" +
                relayAddr + " 更改继电器信息";
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        System.out.println(newName);
        long result = relayDao.updateMachineInfo(deviceId, relayAddr, postion, newName);
        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);
            return "update machine success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "update machine fail";
        }
    }

    //state:On1,Off1
    @Override
    public String updateMachineState(String industryId, String deviceId, String relayAddr, String postion,
                                     Boolean state) {
        String log = "产业:" + industryId + "在设备" + deviceId + " 下的传感器:" +
                relayAddr + " 更改强电设备状态为: " + state;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);
        String state1 = "";

        if (state) {
            state1 = "On1";
        } else {
            state1 = "Off1";
        }

        String res1 = downDataDao.downCtl("relay", state1, postion, deviceId, relayAddr);

        if (res1.equals("success")) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);

        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "fail";
        }
        Machine m = relayDao.findMachine(deviceId, relayAddr, postion);
        if (m != null) {
            long result = relayDao.updateMachineState(deviceId, relayAddr, postion, state1.equals("On1") ? "1" : "0");
            System.out.println(result);
            if (result != 0) {
                customLogger.setResult("成功");
                downDataDao.addOpsLog(industryId, customLogger);
            } else {
                customLogger.setResult("失败");
                downDataDao.addOpsLog(industryId, customLogger);
                return "fail";
            }
        }
        return "success";
    }

    public String updateRelayInfo(String industryId, String deviceId, String relayAddr,
                                  String newName) {
        String log = "产业:" + industryId + " 在设备:" + deviceId + " 把继电器:" +
                relayAddr + " 的继电器名修改为:" + newName;
        CustomLogger customLogger = new CustomLogger();
        customLogger.setDeviceId(deviceId);
        customLogger.setType("0");
        customLogger.setMessage(log);

        long result = relayDao.updateRelayInfo(industryId, deviceId, relayAddr, newName);
        if (result != 0) {
            customLogger.setResult("成功");
            downDataDao.addOpsLog(industryId, customLogger);
            return "success";
        } else {
            customLogger.setResult("失败");
            downDataDao.addOpsLog(industryId, customLogger);
            return "fail";
        }
    }
}
