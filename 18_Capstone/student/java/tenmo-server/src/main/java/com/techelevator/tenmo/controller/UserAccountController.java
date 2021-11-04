package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferInfo;
import com.techelevator.tenmo.model.UserInfo;
import org.apache.coyote.RequestInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.security.PublicKey;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class UserAccountController {

    private UserDao userDao;
    private AccountDao accountDao;

    public UserAccountController(UserDao userDao, AccountDao accountDao) {
            this.userDao = userDao;
            this.accountDao = accountDao;
        }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getCurrentBalance(@PathVariable Integer id, Principal principal) {
        System.out.println(principal.getName());
        return accountDao.getCurrentBalance(id);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer transfer(@RequestBody Transfer transfer) {
        return accountDao.transferMoney(transfer);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserInfo> findForTransfer(){
        return userDao.findForTransfer();
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public TransferInfo getTransferInfoByID(@PathVariable Integer id) {
        return accountDao.getTransferInfoByID(id);
    }

    @RequestMapping(path = "/userTransfers/{id}", method = RequestMethod.GET)
    public List<TransferInfo> getUserTransfers(@PathVariable Integer id) {
        return getUserTransfers(id);
    }
}




