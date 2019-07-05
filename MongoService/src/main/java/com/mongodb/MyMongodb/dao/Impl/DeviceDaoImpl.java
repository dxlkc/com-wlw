package com.mongodb.MyMongodb.dao.Impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MyMongodb.customAnnontation.NotNull;
import com.mongodb.MyMongodb.dao.DeviceDao;
import com.mongodb.MyMongodb.model.deviceInfo.Device;
import com.mongodb.MyMongodb.model.deviceInfo.Rule;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.rmi.server.UID;
import java.util.List;

@Component
public class DeviceDaoImpl implements DeviceDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    // 查找所有板子
    public List<Device> findAllDevice(){
        return mongoTemplate.findAll(Device.class);
    }

    // deviceId 查找板子
    public Device findByDeviceId(String deviceId) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        return mongoTemplate.findOne(query, Device.class);
    }

    // industryId 和 unitId 查找所有板子
    public List<Device> findAll(String industryId, String unitId) {
        Query query = new Query(Criteria.where("industryId").is(industryId).and("unitId").is(unitId));
        return mongoTemplate.find(query, Device.class);
    }

    /*********************************************更新信息部分*************************************************************/

    //更新板子连接状态
    public long updateLinkState(String deviceId, String linkState){
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("linkState", linkState);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    //更新板子名字和备注
    public long updateDeviceInfo(@NotNull String deviceId, String newName, String newRemark) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("deviceName", newName);
        update.set("deviceRemark", newRemark);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    //更新板子经纬度信息   板子端调用
    public long updateLocationDetail(@NotNull String deviceId, String longitude, String latitude, String locationDetail) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("longitude", longitude);
        update.set("latitude", latitude);
        update.set("locationDetail", locationDetail);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    //更新板子发送速率
    public long updateDeviceSendRate(@NotNull String deviceId, String sendRate) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("sendRate", sendRate);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    //添加（更新）板子的产业和采集点信息  （只有EUI存在时）
    public long updateDeviceOwner(@NotNull String industryId, @NotNull String deviceId,
                                  @NotNull String unitId) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.set("industryId", industryId);
        update.set("unitId", unitId);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    /*********************************************删除信息部分*************************************************************/

    //删除industry下的所有 device
    public long deleteAllByIndustryId(String industryId){
        Query query = new Query(Criteria.where("industryId").is(industryId));
        DeleteResult result = mongoTemplate.remove(query, Device.class);
        return result.getDeletedCount();
    }

    //删除acqUnit 下的所有 device
    public long deleteAllByIndustryIdAndUnitId(String industryId, String unitId){
        Query query = new Query(Criteria.where("industryId").is(industryId).and("unitId").is(unitId));
        DeleteResult result = mongoTemplate.remove(query, Device.class);
        return result.getDeletedCount();
    }

    //删除某个acqUnit下的 Device ok
    public long deleteByDeviceId(String industryId, String deviceId) {
        Query query = new Query(Criteria.where("industryId").is(industryId).and("deviceId").is(deviceId));
        DeleteResult result = mongoTemplate.remove(query, Device.class);
        return result.getDeletedCount();
    }

    /*********************************************添加信息部分*************************************************************/

    //添加 device ok
    public Device addDevice(Device device) {
        return mongoTemplate.save(device);
    }

    //只添加 deviceId (用户没在网页添加，但板子已经发来信息)
    public Device addByDeviceId(String deviceId) {
        Device device = new Device();
        device.setDeviceId(deviceId);
        return mongoTemplate.save(device);
    }

    /*******************************************************************************************/
    /************************************             ******************************************/
    /************************************用户自定义规则******************************************/
    /************************************             ******************************************/
    /*******************************************************************************************/

    //添加一条规则
    public long addRule(String deviceId, Rule rule){
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.addToSet("rules", rule);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Device.class);
        return result.getModifiedCount();
    }

    //删除一条规则
    public long deleteRule(String deviceId, String ruleId){
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.pull("rules", new BasicDBObject("id", ruleId));
        UpdateResult result = mongoTemplate.updateFirst(query, update, Device.class);
        return result.getModifiedCount();
    }

    //删除所有规则
    public long deleteAllRule(String deviceId){
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        Update update = new Update();
        update.unset("rules");
        UpdateResult result = mongoTemplate.updateFirst(query, update, Device.class);
        return result.getModifiedCount();
    }
}
