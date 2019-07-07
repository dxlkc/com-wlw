package com.lkc.service;

import com.lkc.model.ReturnUser.ReturnData;

import java.util.List;

public interface HistoryService {

    List<ReturnData> findHistory(String start, String end, String deviceId);
}
