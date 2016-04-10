package com.epam.webtest.dao;

import com.epam.webtest.domain.Document;
import com.epam.webtest.domain.Pack;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        List<Document> documents = jdbcTemplate.query(sql, new RowMapper<Document>() {
            @Override
            public Document mapRow(ResultSet resultSet, int i) throws SQLException {
                Document document = new Document();
                document.setId(resultSet.getLong("id"));
                document.setPack(pack);
                return document;
            }
        });
        return documents;
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