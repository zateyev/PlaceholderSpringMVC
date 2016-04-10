package com.epam.webtest.dao;

import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Pack;

import java.util.List;

public interface DocumentDao {
    Document findById(Long id);
    Document findByName(String name);
    List<Document> findByPack(Pack pack);
    void insert(Document document);
    void removeByPackId(Long id);
    boolean remove(Document document);
    boolean removeById(Long id);
}
