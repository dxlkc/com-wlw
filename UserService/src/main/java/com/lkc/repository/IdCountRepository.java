package com.lkc.repository;

import com.lkc.model.IdInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IdCountRepository extends JpaRepository<IdInfo, String> {
    IdInfo findByCountId(String countId);

    @Transactional
    @Modifying
    @Query("update IdInfo set industryId = :industryId where countId = :countId")
    int updateindustryid(String industryId, String countId);


    @Transactional
    @Modifying
    @Query("update IdInfo set acqunitId = :acqunitId where countId = :countId")
    int updateunitid(String acqunitId, String countId);

}
