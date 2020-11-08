package com.ming.spring.boot.web.service.impl;

import com.ming.common.model.User;
import com.ming.spring.boot.web.dao.UserDao;
import com.ming.spring.boot.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(int id) {
        return userDao.getUserById(id);
    }
}
