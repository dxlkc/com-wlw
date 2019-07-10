package com.lkc.service.serviceInterface.Data;

import com.lkc.model.returndata.Industrydata;

public interface IndustrydataService {

    Industrydata findall(String id);

    String findbyValue(String industryId);

    String findbyrule(String industryId);
}

