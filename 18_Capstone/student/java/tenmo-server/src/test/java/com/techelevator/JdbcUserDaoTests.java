package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Authority;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(2001L, "TestUser1", "password", "tenmo_owner");
    private static final User USER_2 = new User(2002L, "TestUser2", "password", "tenmo_owner");

    private JdbcUserDao sut;
    private JdbcTemplate template = new JdbcTemplate();

    @Before
    public void setup() {
        sut = new JdbcUserDao(template);
    }

    @Test
    public void getUserNameAfterCreation() {
        boolean b = sut.create(USER_1.getUsername(), USER_1.getPassword());
        Assert.assertEquals(1001, sut.findByUsername("TestUser1"));
    }
}
