package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserInfo;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<UserInfo> findForTransfer(Principal principal);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
