package com.caogen.blog.dao;

import com.caogen.blog.entity.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    UserRole getUserByUsername(String userName);

}
