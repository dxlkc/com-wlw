package com.lkc.service;

import com.lkc.model.Log.CustomLogger;

public interface LogService {

    void addOpsLog(String industryId, CustomLogger customLogger);
}
