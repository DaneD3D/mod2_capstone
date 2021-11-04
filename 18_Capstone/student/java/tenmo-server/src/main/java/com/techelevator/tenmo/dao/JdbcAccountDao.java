package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferFailedException;
import com.techelevator.tenmo.model.TransferInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getCurrentBalance(Integer userId) {
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
    public void withdrawMoney(BigDecimal amount, Integer userId) {
        if (isBalanceEnough(amount, userId)) {
            String sql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql, amount, userId);
        } else throw new TransferFailedException();
    }

    @Override
    public void depositMoney(BigDecimal amount, Integer userId) {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, amount, userId);
    }

    @Override
    public Integer getAccountID(Integer userId){
        Integer accountId = -1;
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";

        try {
            accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        }catch (DataAccessException e) {
            System.out.println("Data cannot be accessed");
        }catch (NullPointerException e) {
            System.out.println("Balance is Null");
        }
        return accountId;
    }

    @Override
    public Transfer transferMoney(Transfer transfer) {
        withdrawMoney(transfer.getAmount(), transfer.getFrom());
        depositMoney(transfer.getAmount(), transfer.getTo());
        String sql = "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2,2,?,?,?) RETURNING transfer_id;";

        Integer fromId = getAccountID(transfer.getFrom());
        Integer toId = getAccountID(transfer.getTo());
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class,
                fromId, toId, transfer.getAmount());

        return getTransferById(transferId);
    }

    @Override
    public List<TransferInfo> getUserTransfers(Integer userId) {
        List<TransferInfo> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, " +
                "(SELECT username FROM users u INNER JOIN accounts a ON a.user_id = u.user_id WHERE account_id = account_from) AS userNameFrom, " +
                "(SELECT username FROM users u INNER JOIN accounts a ON a.user_id = u.user_id WHERE account_id = account_to) AS userNameTo, amount " +
                "FROM transfers t " +
                "INNER JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id " +
                "INNER JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            TransferInfo transferInfo = mapRowToTransferInfo(results);
            transfers.add(transferInfo);
        }
        return transfers;
    }

    @Override
    public TransferInfo getTransferInfoByID(Integer transferId) {
        TransferInfo transferInfo = new TransferInfo();
        String sql = "SELECT transfer_id, transfer_type_desc, transfer_status_desc, " +
                "(SELECT username FROM users u INNER JOIN accounts a ON a.user_id = u.user_id WHERE account_id = account_from) AS userNameFrom, " +
                "(SELECT username FROM users u INNER JOIN accounts a ON a.user_id = u.user_id WHERE account_id = account_to) AS userNameTo, amount " +
                "FROM transfers t " +
                "INNER JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id " +
                "INNER JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id " +
                "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            transferInfo = mapRowToTransferInfo(results);
        }
        return transferInfo;
    }

    @Override
    public Transfer getTransferById(Integer transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } return null;
    }

    @Override
    public boolean isBalanceEnough(BigDecimal amount, Integer userId) {
        BigDecimal currentBalance = getCurrentBalance(userId);
        return currentBalance.compareTo(amount) >= 0;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setId(results.getInt("transfer_id"));
        transfer.setFrom(results.getInt("account_from"));
        transfer.setTo(results.getInt("account_to"));
        transfer.setStatus(results.getInt("transfer_status_id"));
        transfer.setAmount(results.getBigDecimal("amount"));
        transfer.setType(results.getInt("transfer_type_id"));
        return transfer;
    }

    private TransferInfo mapRowToTransferInfo(SqlRowSet results) {
        TransferInfo transferInfo = new TransferInfo();
        transferInfo.setId(results.getInt("transfer_id"));
        transferInfo.setFrom(results.getString("userNameFrom"));
        transferInfo.setTo(results.getString("userNameTo"));
        transferInfo.setStatus(results.getString("transfer_status_desc"));
        transferInfo.setAmount(results.getBigDecimal("amount"));
        transferInfo.setType(results.getString("transfer_type_desc"));
        return transferInfo;
    }


}
