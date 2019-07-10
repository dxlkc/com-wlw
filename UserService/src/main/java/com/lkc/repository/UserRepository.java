package com.lkc.repository;

import com.lkc.model.userInfo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

    //查找name
    User findByName(String name);

    //修改密码
    @Transactional
    @Modifying
    @Query("update User as c set c.password = :password where c.name = :name")
    int updatePasswordByName(@Param("name") String name, @Param("password") String password);
}