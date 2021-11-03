package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.PublicKey;

@RestController
public class UserAccountController {

    private UserDao userDao;
    private AccountDao accountDao;

    public UserAccountController() {

    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getCurrentBalance() {
        accountDao.getCurrentBalance()
    }
}


