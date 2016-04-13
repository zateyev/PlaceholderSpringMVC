package com.epam.webtest.dao;


import com.epam.webtest.domain.Pack;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcPackDao implements PackDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcPackDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Pack findById(Long id) {
        String sql = "SELECT name, user_email, location FROM pack WHERE id=" + id;
        return jdbcTemplate.query(sql, new ResultSetExtractor<Pack>() {
            @Override
            public Pack extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    Pack pack = new Pack();
                    pack.setId(id);
                    pack.setName(resultSet.getString("name"));
                    pack.setLocation(resultSet.getString("location"));
                    return pack;
                }
                return null;
            }
        });
    }

    @Override
    public Pack findByName(String name) {
        return null;
    }

    @Override
    public List<Pack> findByUsername(String username) {
        String sql = "SELECT id, name, location FROM pack WHERE user_email= \'" + username + "\'";
        return jdbcTemplate.query(sql, new RowMapper<Pack>() {
            @Override
            public Pack mapRow(ResultSet resultSet, int i) throws SQLException {
                Pack pack = new Pack();
                pack.setId(resultSet.getLong("id"));
                pack.setName(resultSet.getString("name"));
                pack.setLocation(resultSet.getString("location"));
                pack.setUsername(username);
                return pack;
            }
        });
    }

    @Override
    public void update(Pack pack) {
        String sql = "UPDATE pack SET name=?, user_email=?, location=? WHERE id=?";
        jdbcTemplate.update(sql, pack.getName(), pack.getUsername(), pack.getLocation(), pack.getId());
    }

    @Override
    public Pack save(Pack pack) {
        return null;
    }

    @Override
    public Pack insert(Pack pack) {
        String sql = "INSERT INTO pack (id, name, user_email, location) VALUES (DEFAULT, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, pack.getName());
                ps.setString(2, pack.getUsername());
                ps.setString(3, pack.getLocation());
                return ps;
            }
        }, holder);
        Long newPackId = holder.getKey().longValue();
        pack.setId(newPackId);
        return pack;
    }

    @Override
    public boolean remove(Pack pack) {
        return false;
    }

    @Override
    public void removeById(Long id) {
        String sql = "DELETE FROM pack WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
