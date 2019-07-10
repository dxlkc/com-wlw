package com.lkc.repository;

import com.lkc.model.userInfo.UserIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;

public interface IndustryRepository extends JpaRepository<UserIndustry, String> {

    UserIndustry deleteByName(String name);

    UserIndustry deleteByNameAndIndustryId(String name, String industryId);

    UserIndustry findByIndustryIdAndName(String industryID,String name);

    @Transactional
    @Modifying
    @Query("delete from UserIndustry where industryId = :industryId")
    int deleteByIndustryId(String industryId);

    @Transactional
    @Modifying
    @Query("select industryId from UserIndustry where name = :name")
    ArrayList<String> findByName(String name);

    @Transactional
    @Modifying
    @Query("select name from UserIndustry where industryId = :industryId")
    ArrayList<String> findByIndustryId(String industryId);

}
