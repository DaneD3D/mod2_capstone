package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao  {

    BigDecimal getCurrentBalance(Integer userId);

    boolean isBalanceEnough(BigDecimal amount, Integer userId);

    void withdrawMoney(BigDecimal amount, Integer userId);

    void depositMoney(BigDecimal amount, Integer userId);

    Transfer transferMoney(Transfer transfer);

    Transfer getTransferById(Integer transferId);
}
