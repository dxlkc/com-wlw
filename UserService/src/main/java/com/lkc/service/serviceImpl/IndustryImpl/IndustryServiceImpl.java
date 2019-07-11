package com.lkc.service.serviceImpl.IndustryImpl;

import com.lkc.model.IdCount;
import com.lkc.model.industry.deviceInfo.Device;
import com.lkc.model.industry.industryInfo.AcqUnit;
import com.lkc.model.industry.industryInfo.Industry;
import com.lkc.model.userInfo.UserIndustry;
import com.lkc.repository.IdCountRepository;
import com.lkc.repository.IndustryRepository;
import com.lkc.service.serviceInterface.Industry.DeviceService;
import com.lkc.FeignClient.mongoservice.DeviceDao;
import com.lkc.FeignClient.mongoservice.IndustryDao;
import com.lkc.service.serviceInterface.Industry.IndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndustryServiceImpl implements IndustryService {
    @Resource
    private IndustryDao industryDao;
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private IndustryRepository industryRepository;
    @Resource
    private IdCountRepository idCountRepository;

    /***********************************查找**************************************************/
    public Industry findIndustry(String industryId) {
        return industryDao.findByIndustryId(industryId);
    }

    /**********************************添加***************************************************/

    public String addIndustry(String username, String industryName,
                              String industryRemark, String industryTime) {
        Industry industry = new Industry();
        UserIndustry userIndustry = new UserIndustry();
        IdCount idCount = IdCount.getIntance();
        //设置industryid
        Integer Id = idCount.industryCount();
        industry.setIndustryId(Id.toString());
        industry.setIndustryName(industryName);
        industry.setIndustryRemark(industryRemark);
        industry.setIndustryTime(industryTime);
        industry.setIndustryUnitNum("0");
        userIndustry.setName(username);
        userIndustry.setIndustryId(Id.toString());
        userIndustry.setPermission("1");

        industryDao.addIndustry(industry);
        idCountRepository.updateindustryid(Id.toString(), "1");
        industryRepository.save(userIndustry);

        return Id.toString();
    }


    public String addAcqUnit(String industryId, String UnitName, String UnitRemark) {
        Industry industry = this.findIndustry(industryId);
        String num = industry.getIndustryUnitNum();
        num = String.valueOf(Integer.valueOf(num) + 1);

        industryDao.updateAcqNum(industryId, num);
        AcqUnit acqUnit = new AcqUnit();
        IdCount idCount = IdCount.getIntance();
        Integer id = idCount.acqUnitCount();
        acqUnit.setUnitId(id.toString());
        acqUnit.setUnitName(UnitName);
        acqUnit.setUnitRemark(UnitRemark);

        industryDao.addAcqUnit(industryId, acqUnit);
        idCountRepository.updateunitid(id.toString(), "1");
        return id.toString();
    }


    /*********************************************删除**********************************************/

    public String deleteAcqUnit(String industryId, String unitId) {
        long res = 0;
        Industry industry = this.findIndustry(industryId);
        String num = industry.getIndustryUnitNum();
        num = String.valueOf(Integer.valueOf(num) - 1);
        industryDao.updateAcqNum(industryId, num);

        List<Device> devices = deviceDao.findAll(industryId, unitId);
        if (!(devices == null || devices.size() == 0)) {
            for (Device device : devices) {
                String res1 = deviceService.deleteDevice(industryId, device.getDeviceId());
                if (!res1.equals("delete device success"))
                    return res1;
            }
        }
        res = industryDao.deleteByAcqUnitId(industryId, unitId);

        if (res != 0) {
            return "success";
        }
        return "delete acqunit fail";
    }


    public String deleteIndustry(String name, String id) {
        UserIndustry userIndustry = industryRepository.findByIndustryIdAndName(id, name);

        if (userIndustry.getPermission().equals("0")) {
            return "industry permission error";
        }
        Industry industry = industryDao.findByIndustryId(id);
        List<AcqUnit> acqUnits = industry.getAcqUnitList();
        if (!(acqUnits == null || acqUnits.size() == 0)) {
            for (AcqUnit acqUnit : acqUnits) {
                String res = this.deleteAcqUnit(id, acqUnit.getUnitId());
                if (!res.equals("success")) {
                    return res;
                }
            }
        }
        if (industryRepository.deleteByIndustryId(id) == 0) {
            return "fail";
        }
        long result = industryDao.deleteByIndustryId(id);
        if (result != 0) {
            return "success";
        }
        return "delete industry fail";
    }

    /*****************************************更新************************************************/

    public String updateAcqUnit(String industryId, String unitId, String newName, String newRemark) {
        long result = industryDao.updateAcqunitInfo(industryId, unitId, newName, newRemark);
        if (result != 0) {
            return "update unit success";
        }
        return "update unit fail";
    }

    public String updateIndustry(String industryId, String newName, String newRemark) {
        long result = industryDao.updateIndustryInfo(industryId, newName, newRemark);
        if (result != 0) {
            return "update industry success";
        }
        return "update industry fail";
    }
}
