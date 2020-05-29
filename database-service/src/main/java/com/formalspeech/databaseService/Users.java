package com.formalspeech.databaseService;

import lombok.SneakyThrows;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Users implements Closeable {
    final static String url = "jdbc:mysql://localhost:3306/formalspeech";
    final static String username = "root";
    final static String password = "";
    final Connection connection;

    public Users() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, username, password);
    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM `users`");
        return collectUsers(resultSet);
    }

    private List<User> collectUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()){
            User user = new User();
            user.setLogin(resultSet.getString("Login"));
            user.setEmail(resultSet.getString("Email"));
            user.setPassword(resultSet.getString("Password"));
            user.setActive(resultSet.getBoolean("IsActive"));
            user.setLastActivityDate(resultSet.getDate("LastActivityDate"));
            user.setAccessLevel(resultSet.getInt("AccessLevel"));
            user.setRegistrationDate(resultSet.getDate("RegistrationDate"));
            users.add(user);
        }
        return users;
    }

    public List<User> getActiveUsers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM `users` WHERE `users`.`IsActive` = 1");
        return collectUsers(resultSet);
    }

    public boolean add(User user) throws SQLException{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM `users`");
        if(!resultSet.next()){
            return false;
        } else {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` (`Login`, `Email`, `Password`, `IsActive`, `LastActivityDate`, `AccessLevel`, `RegistrationDate`) VALUES (?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setBoolean(4, user.isActive());
            preparedStatement.setDate(5, user.getLastActivityDate());
            preparedStatement.setInt(6, user.getAccessLevel());
            preparedStatement.setDate(7, user.getRegistrationDate() == null ? Date.valueOf(LocalDate.now()) : user.getRegistrationDate());

            boolean autoCommit = connection.getAutoCommit();
            try {
                connection.setAutoCommit(false);
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                return false;
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        }

    }

    public boolean setUserActive(String Login, boolean isActive) throws  SQLException{

        PreparedStatement preparedStatement;
        if (isActive) {
            preparedStatement = connection.prepareStatement("UPDATE `users` SET `IsActive` = 1 WHERE `users`.`Login` = ?; ");
        } else {
            preparedStatement = connection.prepareStatement("UPDATE `users` SET `IsActive` = 0, `LastActivityDate` = CURRENT_DATE() WHERE `users`.`Login` = ?; ");
        }
        preparedStatement.setString(1, Login);

        return commitChange(preparedStatement);
    }

    public boolean deleteUser(String login) throws SQLException{
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("DELETE FROM `users` WHERE `users`.`Login` = ? ");
        preparedStatement.setString(1, login);

        return commitChange(preparedStatement);
    }

    private boolean commitChange(PreparedStatement preparedStatement) throws SQLException {
        boolean autoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            if(preparedStatement.executeUpdate() == 1){
                connection.commit();
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    public User getUser(String login, String password)throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `users`.`Login` = ? AND `users`.`Password` = ?");
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> users = collectUsers(resultSet);
        if(users.size() == 1){
            return users.get(0);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public void close() throws IOException {
        connection.close();
    }
}

