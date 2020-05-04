package com.ljy.graduate.dao;

import com.ljy.graduate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: liuzhiyuan
 * Date: 2020/3/23
 * Description:
 */
@Repository("userDao")
public interface UserDao extends JpaRepository<User,Integer> {

     User findByEmail(String email);
}
