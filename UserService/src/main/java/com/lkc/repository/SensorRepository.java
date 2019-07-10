package com.lkc.repository;

import com.lkc.model.industry.sensorInfo.SensorDefault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<SensorDefault, String> {

    List<SensorDefault> findAllBySensorAddr(String SensorAddr);

    List<SensorDefault> findSensorDefaultsBySensorAddr(String SensorAddr);
}
