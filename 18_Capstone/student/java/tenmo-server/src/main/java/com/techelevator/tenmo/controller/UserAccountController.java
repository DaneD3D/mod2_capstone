package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.security.PublicKey;

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
    public BigDecimal getCurrentBalance(@PathVariable long id, Principal principal) {
        System.out.println(principal.getName());
        return accountDao.getCurrentBalance(id);
    }
}




