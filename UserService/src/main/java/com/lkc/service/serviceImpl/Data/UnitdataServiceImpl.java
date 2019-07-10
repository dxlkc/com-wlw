package com.lkc.service.serviceImpl.Data;

import com.lkc.model.industry.deviceInfo.Device;
import com.lkc.model.industry.industryInfo.Industry;
import com.lkc.FeignClient.mongoservice.DeviceDao;
import com.lkc.FeignClient.mongoservice.IndustryDao;
import com.lkc.model.returndata.Unitdata;
import com.lkc.model.industry.industryInfo.AcqUnit;
import com.lkc.service.serviceInterface.Data.UnitdataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnitdataServiceImpl implements UnitdataService {
    @Resource
    private IndustryDao industryDao;

    @Resource
    private DeviceDao deviceDao;

    @Override
    public List<Unitdata> findunit(String id) {
        System.out.println(id);
        List<Unitdata> unitdata = new ArrayList<>();
        Industry industry = industryDao.findByIndustryId(id);
        List<AcqUnit> acqUnits = industry.getAcqUnitList();
        for(int i = 0;i<acqUnits.size();i++)
        {
            Unitdata unitdata1 = new Unitdata();
            unitdata1.setUnitId(acqUnits.get(i).getUnitId());
            unitdata1.setUnitName(acqUnits.get(i).getUnitName());
            unitdata1.setUnitRemark(acqUnits.get(i).getUnitRemark());
            List<Device> devices = deviceDao.findAll(id,acqUnits.get(i).getUnitId());
            System.out.println(devices.size());
            String num = String.valueOf(devices.size());
            unitdata1.setUnitDeviceNum(num);
            unitdata.add(unitdata1);
            unitdata1 = null;
        }
        return unitdata;
    }
}
