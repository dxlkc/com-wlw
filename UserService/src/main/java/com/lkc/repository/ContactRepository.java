package com.lkc.repository;

import com.lkc.model.userInfo.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ContactRepository extends JpaRepository<UserContact, String> {

    UserContact findByName(String name);

    @Transactional
    @Modifying
    @Query(value = "insert into contact_info(name,email,email_ctl) values(:name,:email,:email_ctl)", nativeQuery = true)
    int save(String name, String email, String email_ctl);

    //改email
    @Transactional
    @Modifying
    @Query("update UserContact set email= :email where name= :name")
    int changeEmail(String name, String email);

    //改email
    @Transactional
    @Modifying
    @Query("update UserContact set email_ctl= :emailctl where name= :name")
    int changeEmailctl(String name, String emailctl);
}
