package com.mongodb.MyMongodb.dao.Impl;

import com.mongodb.BasicDBObject;
import com.mongodb.MyMongodb.customAnnontation.NotNull;
import com.mongodb.MyMongodb.dao.IndustryDao;
import com.mongodb.MyMongodb.model.industryInfo.AcqUnit;
import com.mongodb.MyMongodb.model.industryInfo.Industry;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IndustryDaoImpl implements IndustryDao{
    @Resource
    private MongoTemplate mongoTemplate;

    //通过 industry id 查找项目
    public Industry findByIndustryId(String id) {
        Query query = new Query(Criteria.where("industryId").is(id));
        return mongoTemplate.findOne(query, Industry.class);
    }

    /*********************************************更新信息部分*************************************************************/

    //更新产业信息
    public long updateIndustryInfo(@NotNull String industryId, String industryName, String industryRemark) {
        Query query = Query.query(Criteria.where("industryId").is(industryId));
        Update update = new Update();
        update.set("industryName", industryName);
        update.set("industryRemark", industryRemark);
        UpdateResult result = mongoTemplate.upsert(query, update, Industry.class);
        return  result.getModifiedCount();
    }

    //更新采集点信息
    public long updateAcqunitInfo(@NotNull String industryId, @NotNull String unitId,
                                  String newName, String unitRemark) {
        Industry industry = findByIndustryId(industryId);
        int index = industry.getUnitIndexById(unitId);

        Query query = new Query(Criteria.where("industryId").is(industryId));
        Update update = new Update();

        update.set("acqUnitList." + index + ".unitName", newName);
        update.set("acqUnitList." + index + ".unitRemark", unitRemark);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Industry.class);
        return result.getModifiedCount();
    }

    //更新采集点数量
    public long updateAcqNum(@NotNull String industryId, String industryUnitNum){
        Query query = new Query(Criteria.where("industryId").is(industryId));
        Update update = new Update();
        update.set("industryUnitNum",industryUnitNum);
        UpdateResult result = mongoTemplate.updateMulti(query, update, Industry.class);
        return result.getModifiedCount();
    }

    /*********************************************删除信息部分*************************************************************/

    //通过 industry id 删除整个项目
    public long deleteByIndustryId(String id) {
        Query query = new Query(Criteria.where("industryId").is(id));
        DeleteResult result = mongoTemplate.remove(query, Industry.class);
        return result.getDeletedCount();
    }

    //删除某个industry下的 acqUnit
    public long deleteByAcqUnitId(String industryId, String unitId) {
        Query query = new Query(Criteria.where("industryId").is(industryId));
        Update update = new Update();
        update.pull("acqUnitList",new BasicDBObject("unitId",unitId));
        UpdateResult result = mongoTemplate.updateFirst(query, update, Industry.class);
        return result.getModifiedCount();
    }

    /*********************************************添加信息部分*************************************************************/

    //添加 industry
    public Industry addIndustry(Industry industry) {
        return mongoTemplate.save(industry);
    }

    //添加 acqUnit
    public long addAcqUnit(String industryId, AcqUnit acqUnit) {
        Query query = new Query(Criteria.where("industryId").is(industryId));
        Update update = new Update();
        update.addToSet("acqUnitList", acqUnit);
        UpdateResult result = mongoTemplate.upsert(query, update, Industry.class);
        return result.getModifiedCount();
    }

}
