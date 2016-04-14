package com.epam.webtest.dao;

import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Pack;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

public class JdbcDocumentDao implements DocumentDao {

    JdbcTemplate jdbcTemplate;

    public JdbcDocumentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Document findById(Long id) {
        return null;
    }

    @Override
    public Document findByName(String name) {
        return null;
    }

    @Override
    public List<Document> findByPack(Pack pack) {
        String sql = "SELECT id, name FROM document WHERE pack_id=" + pack.getId();
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            Document document = null;
            try {
                document = new Document(resultSet.getString("name"), pack);
            } catch (IOException e) {
                e.printStackTrace();
            }
            document.setId(resultSet.getLong("id"));
            return document;
        });
    }

    @Override
    public void insert(Document document) {
        String sql = "INSERT INTO document (id, name, pack_id) VALUES (DEFAULT, ?, ?)";
        jdbcTemplate.update(sql, document.getName(), document.getPack().getId());
    }

    @Override
    public void removeByPackId(Long id) {
        String sql = "DELETE FROM document WHERE pack_id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean remove(Document document) {
        return false;
    }

    @Override
    public boolean removeById(Long id) {
        return false;
    }
}
