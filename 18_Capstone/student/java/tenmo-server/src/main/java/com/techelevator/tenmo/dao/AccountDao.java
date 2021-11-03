package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao  {

    BigDecimal getCurrentBalance(Long userId);

    boolean isBalanceEnough(BigDecimal amount, Long userId);

    void withdrawMoney(BigDecimal amount, Long userId);

    void depositMoney(BigDecimal amount, Long userId);

    Transfer transferMoney(Transfer transfer);
}
