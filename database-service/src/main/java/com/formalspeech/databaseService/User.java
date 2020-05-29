package com.formalspeech.databaseService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String email;
    private String password;
    private boolean isActive;
    private Date lastActivityDate;
    private int accessLevel;
    private Date registrationDate;
}
