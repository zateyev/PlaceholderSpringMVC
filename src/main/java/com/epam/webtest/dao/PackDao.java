package com.epam.webtest.dao;

import com.epam.webtest.domain.Pack;
import com.epam.webtest.domain.User;

import java.util.List;

public interface PackDao {
    Pack findById(Long id);
    Pack findByName(String name);
    List<Pack> findByUsername(String username);
    void update(Pack pack);
    Pack save(Pack pack);
    Pack insert(Pack pack);
    boolean remove(Pack pack);
    void removeById(Long id);
}