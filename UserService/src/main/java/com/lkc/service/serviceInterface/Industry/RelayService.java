package com.lkc.service.serviceInterface.Industry;

import com.lkc.model.industry.relayInfo.Relay;

import java.util.List;
import java.util.Map;

public interface RelayService {
    //通过device查找其下所有relay
    List<Relay> findAll(String deviceId);

    //relay详情显示
    String findRelaydata(String deviceId, String addr);

    String findRelay(String deviceId);

    //添加或更新machine
    String updateOradd(List<Map<String, String>> mapList);

    //relay更改数据前返回数据
    String returnbefore(String deviceId, String relayAddr);

    //添加relay
    String addRelay(Relay relay);

    //添加machine
    String addMachine(String industryId, String deviceId, String relayAddr,
                      String machineName, String machinePostion);

    //更新relay
    String updateRelayInfo(String industryId, String deviceId, String relayAddr, String newName);


    //更新machine
    String updateMachineInfo(String industryId, String deviceId, String relayAddr, String postion,
                             String newName);

    //更新machinestate
    String updateMachineState(String industryId, String deviceId, String relayAddr, String postion, Boolean state);

    //删除relay
    String deleteRelay(String industryId, String deviceId, String relayAddr);

    //删除machine
    String deleteMachine(String industryId, String deviceId, String relayAddr, String machinePosition);
}
