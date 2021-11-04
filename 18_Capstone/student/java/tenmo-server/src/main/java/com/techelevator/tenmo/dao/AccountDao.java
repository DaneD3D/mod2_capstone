package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferInfo;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao  {

    BigDecimal getCurrentBalance(Integer userId);

    boolean isBalanceEnough(BigDecimal amount, Integer userId);

    void withdrawMoney(BigDecimal amount, Integer userId);

    void depositMoney(BigDecimal amount, Integer userId);

    Transfer transferMoney(Transfer transfer);

    Transfer getTransferById(Integer transferId);

    Integer getAccountID(Integer userId);

    List<TransferInfo> getUserTransfers(Integer userId);

    TransferInfo getTransferInfoByID(Integer transferId);
}
