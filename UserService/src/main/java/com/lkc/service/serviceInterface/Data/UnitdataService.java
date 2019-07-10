package com.lkc.service.serviceInterface.Data;

import com.lkc.model.returndata.Unitdata;

import java.util.List;

public interface UnitdataService {

    List<Unitdata> findunit(String id);
}