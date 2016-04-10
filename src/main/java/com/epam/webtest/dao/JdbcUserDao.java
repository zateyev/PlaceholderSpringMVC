package com.epam.webtest.dao;


import com.epam.webtest.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT email, password, enabled FROM users WHERE email = " + email;
        return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if (resultSet.next()) {
                    User user = new User();
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setEnabled(resultSet.getShort("enabled"));
                    return user;
                }
                return null;
            }
        });
    }

    @Override
    public void update(User user) {
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO users (email, password, enabled) VALUES (DEFAULT, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.isEnabled());
    }

    @Override
    public boolean remove(User user) {
        return false;
    }
}
