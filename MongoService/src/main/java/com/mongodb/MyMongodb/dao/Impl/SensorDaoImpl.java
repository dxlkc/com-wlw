package com.mongodb.MyMongodb.dao.Impl;

import com.mongodb.MyMongodb.customAnnontation.NotNull;
import com.mongodb.MyMongodb.dao.SensorDao;
import com.mongodb.MyMongodb.model.sensorInfo.SensorInfo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorDaoImpl implements SensorDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    //查找 通过 ok
    public List<SensorInfo> find(String deviceId, String sensorAddr) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId).and("sensorAddr").is(sensorAddr));
        return mongoTemplate.find(query, SensorInfo.class);
    }

    //查找板子下的所有传感器
    public List<SensorInfo> findAll(String deviceId) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        return mongoTemplate.find(query, SensorInfo.class);
    }

    //查找某个产业下的所有传感器
    public List<SensorInfo> findAllByIndustryId(String industryId){
        Query query = new Query(Criteria.where("industryId").is(industryId));
        return mongoTemplate.find(query, SensorInfo.class);
    }

    //查找传感器是否存在 通过 addr站号
    public SensorInfo findByAddr(String deviceId, String sensorAddr, String type){
        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("sensorAddr").is(sensorAddr)
                .and("type").is(type));
        return mongoTemplate.findOne(query, SensorInfo.class);
    }

    /***************添加********************/

    //添加 ok
    public SensorInfo add(SensorInfo sensorInfo) {
        return mongoTemplate.save(sensorInfo);
    }

    /***************删除************************/

    //删除industry下的所有 sensor
    public long deleteAllByIndustryId(String industryId){
        Query query = new Query(Criteria.where("industryId").is(industryId));
        DeleteResult result = mongoTemplate.remove(query, SensorInfo.class);
        return result.getDeletedCount();
    }

    //删除acqUnit 下的所有 sensor
    public long deleteAllByIndustryIdAndUnitId(String industryId, String unitId){
        Query query = new Query(Criteria.where("industryId").is(industryId).and("unitId").is(unitId));
        DeleteResult result = mongoTemplate.remove(query, SensorInfo.class);
        return result.getDeletedCount();
    }

    //删除device 下的所有 sensor
    public long deleteAllByDeviceId(String deviceId){
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        DeleteResult result = mongoTemplate.remove(query, SensorInfo.class);
        return result.getDeletedCount();
    }

    //删除 ok
    public long delete(@NotNull String deviceId, @NotNull String sensorAddr) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId).and("sensorAddr").is(sensorAddr));
        DeleteResult result = mongoTemplate.remove(query, SensorInfo.class);
        return result.getDeletedCount();
    }

    /*****************更新*********************/

    //更新传感器名字
    public long updateName(@NotNull String deviceId, @NotNull String sensorAddr,
                           String name) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId).and("sensorAddr").is(sensorAddr));
        Update update = new Update();
        update.set("sensorName", name);
        UpdateResult result = mongoTemplate.updateMulti(query, update, SensorInfo.class);
        return result.getModifiedCount();
    }

    //更新阈值 ok
    public long updateThreshold(@NotNull String deviceId, @NotNull String sensorAddr, @NotNull String type,
                                String max, String min) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("sensorAddr").is(sensorAddr)
                .and("type").is(type));
        Update update = new Update();
        update.set("max", max);
        update.set("min", min);
        UpdateResult result = mongoTemplate.updateMulti(query, update, SensorInfo.class);
        return result.getModifiedCount();
    }

    //更新最新值 ok
    public long updateValue(@NotNull String deviceId, @NotNull String sensorAddr,
                            @NotNull String type, String value, String time) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("sensorAddr").is(sensorAddr)
                .and("type").is(type));
        Update update = new Update();
        update.set("value", value);
        update.set("time", time);
        UpdateResult result = mongoTemplate.updateMulti(query, update, SensorInfo.class);
        return result.getModifiedCount();
    }
}
