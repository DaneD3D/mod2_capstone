package com.techelevator.tenmo.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getCurrentBalance(Long userId) {
        BigDecimal currentBalance = BigDecimal.ZERO;
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        try {
            currentBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        }catch (DataAccessException e) {
            System.out.println("Data cannot be accessed");
        }catch (NullPointerException e) {
            System.out.println("Balance is Null");
        }
        return currentBalance;
    }
}
