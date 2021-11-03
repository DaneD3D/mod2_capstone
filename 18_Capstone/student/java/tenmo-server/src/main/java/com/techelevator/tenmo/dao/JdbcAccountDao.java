package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferFailedException;
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

    @Override
    public void withdrawMoney(BigDecimal amount, Long userId) {
        if (isBalanceEnough(amount, userId)) {
            String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, amount, userId);
        } else throw new TransferFailedException();
    }

    @Override
    public void depositMoney(BigDecimal amount, Long userId) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public Transfer transferMoney(Transfer transfer) {
        withdrawMoney(transfer.getAmount(), transfer.getFrom());
        depositMoney(transfer.getAmount(), transfer.getTo());
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2,2,?,?,?) RETURNING transfer_id;";

        Transfer succesfulTransfer = jdbcTemplate.queryForObject(sql, Transfer.class,
                transfer.getFrom(), transfer.getTo(), transfer.getAmount());

        return succesfulTransfer;
    }

    @Override
    public boolean isBalanceEnough(BigDecimal amount, Long userId) {
        BigDecimal currentBalance = getCurrentBalance(userId);
        return currentBalance.compareTo(amount) >= 0;
    }



}
