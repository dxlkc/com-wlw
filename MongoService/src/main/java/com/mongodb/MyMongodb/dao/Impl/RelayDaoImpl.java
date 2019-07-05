package com.mongodb.MyMongodb.dao.Impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MyMongodb.customAnnontation.NotNull;
import com.mongodb.MyMongodb.dao.RelayDao;
import com.mongodb.MyMongodb.model.relayInfo.Machine;
import com.mongodb.MyMongodb.model.relayInfo.Relay;
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
public class RelayDaoImpl implements RelayDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    //查找 ok
    public Relay find(String deviceId, String relayAddr) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        return mongoTemplate.findOne(query, Relay.class);
    }

    //查找板子下的所有继电器
    public List<Relay> findAll(String deviceId) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        return mongoTemplate.find(query, Relay.class);
    }

    /***********添加********************/

    //添加 relay ok
    public Relay addRelay(Relay relay) {
        return mongoTemplate.save(relay);
    }

    //添加 machine ok
    public long addMachine(@NotNull String industryId, @NotNull String deviceId, @NotNull String relayAddr,
                           Machine machine) {
        Query query = new Query(Criteria.where("industryId").is(industryId)
                .and("deviceId").is(deviceId).and("relayAddr").is(relayAddr));
        Update update = new Update();
        update.addToSet("machineList", machine);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Relay.class);
        return result.getModifiedCount();
    }

    /***********删除********************/

    //删除industry 下的所有 relay
    public long deleteAllByIndustryId(String industryId) {
        Query query = new Query(Criteria.where("industryId").is(industryId));
        DeleteResult result = mongoTemplate.remove(query, Relay.class);
        return result.getDeletedCount();
    }

    //删除acqUnit 下的所有 relay
    public long deleteAllByIndustryIdAndUnitId(String industryId, String unitId){
        Query query = new Query(Criteria.where("industryId").is(industryId).and("unitId").is(unitId));
        DeleteResult result = mongoTemplate.remove(query, Relay.class);
        return result.getDeletedCount();
    }

    //删除device 下的所有 relay
    public long deleteAllByDeviceId(String deviceId) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId));
        DeleteResult result = mongoTemplate.remove(query, Relay.class);
        return result.getDeletedCount();
    }

    //删除relay
    public long deleteByRelayAddr(String industryId, String deviceId, String relayAddr) {
        Query query = new Query(Criteria.where("industryId").is(industryId)
                .and("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        DeleteResult result = mongoTemplate.remove(query, Relay.class);
        return result.getDeletedCount();
    }

    //删除 machine ok
    public long deleteByMachineName(String industryId, String deviceId, String relayAddr, String machineName) {
        Query query = new Query(Criteria.where("industryId").is(industryId)
                .and("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        Update update = new Update();
        update.pull("machineList", new BasicDBObject("machineName", machineName));
        UpdateResult result = mongoTemplate.updateFirst(query, update, Relay.class);
        return result.getModifiedCount();
    }

    /***********更新********************/

    //更新 relay 名字 ok
    public long updateRelayInfo(@NotNull String industryId, @NotNull String deviceId,
                                @NotNull String relayAddr, String newName) {
        Query query = new Query(Criteria.where("industryId").is(industryId)
                .and("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        Update update = new Update();
        update.set("relayName", newName);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Relay.class);
        return result.getModifiedCount();
    }

    //更新 relay 的 8 路状态 ok
    public long updatePinsState(@NotNull String deviceId, @NotNull String relayAddr,
                                String newPinsState) {
        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        Update update = new Update();
        update.set("pinsState", newPinsState);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Relay.class);
        return result.getModifiedCount();
    }

    //更新 machine 信息 ok
    public long updateMachineInfo(@NotNull String deviceId, @NotNull String relayAddr,
                                  @NotNull String machinePosition, String newName, String newRemark, String newPostion) {
        Relay relay = find(deviceId, relayAddr);
        int machineIndex = relay.getMachineIndexByPosition(machinePosition);

        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        Update update = new Update();

        update.set("machineList." + machineIndex + "machineName", newName);
        update.set("machineList." + machineIndex + "machineRemark", newRemark);
        update.set("machineList." + machineIndex + "machinePostion", newPostion);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Relay.class);
        return result.getModifiedCount();
    }

    //更新 machine 状态 ok
    public long updateMachineState(@NotNull String deviceId, @NotNull String relayAddr,
                                   @NotNull String machinePosition, String state) {
        Relay relay = find(deviceId, relayAddr);
        int machineIndex = relay.getMachineIndexByPosition(machinePosition);

        Query query = new Query(Criteria.where("deviceId").is(deviceId)
                .and("relayAddr").is(relayAddr));
        Update update = new Update();
        update.set("machineList." + machineIndex + "machineState", state);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Relay.class);
        return result.getModifiedCount();
    }

}
