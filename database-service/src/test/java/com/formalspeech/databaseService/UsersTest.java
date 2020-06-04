package com.formalspeech.databaseService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class UsersTest {

    Users users;

    @BeforeEach
    public void init() throws SQLException, ClassNotFoundException {
        users = Users.getInstance();

    }
    @Test
    void givenDataBase_whenSelectingAllUsers_thenCorrect() throws SQLException{
        log.info(users.getAllUsers().toString());
    }

    @Test
    void givenCorrectLoginAndPassword_whenGetUser_thenCorrect() throws SQLException{
        log.info(users.getUser("admin", "admin").toString());

    }

    @Test
    void givenCorrectLogin_whenMakeUserActiveAndInactive_thenLastActivityDateChanged() throws SQLException {
        users.setUserActive("admin", true);
        users.setUserActive("admin", false);
    }

    @Test
    void givenCorrectUser_whenAdd_thenAddedCorrectly() throws SQLException {
       users.add(new User("login", "email@mail.com", "password", false, null, 1, null));
       log.info(users.getAllUsers().toString());
    }

    @Test
    void givenExistingUser_whenAdd_thenNotAddedCorrectly() throws SQLException {
        users.add(new User("admin", "email@mail.com", "password", false, null, 1, null));
        log.info(users.getAllUsers().toString());
    }
}