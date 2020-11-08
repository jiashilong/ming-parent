package com.ming.spring.boot.web.dao.impl;

import com.ming.common.model.User;
import com.ming.spring.boot.web.dao.UserDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    private static Map<Integer, User> USER_MAP = new HashMap<>();

    static {
        USER_MAP.put(1, new User(1, "ming01", 1));
        USER_MAP.put(2, new User(2, "ming02", 2));
        USER_MAP.put(3, new User(3, "ming03", 3));
    }

    @Override
    public User getUserById(int id) {
        return USER_MAP.getOrDefault(id,new User());
    }
}
