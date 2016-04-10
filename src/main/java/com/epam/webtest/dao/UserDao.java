package com.epam.webtest.dao;

import com.epam.webtest.domain.User;

public interface UserDao {
    User findByEmail(String email);
    void update(User user);
    void insert(User user);
    boolean remove(User user);
}
