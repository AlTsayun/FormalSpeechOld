package com.formalspeech.networkService.serverSide;

import com.formalspeech.databaseService.User;
import com.formalspeech.databaseService.Users;
import com.formalspeech.networkService.conntection.Connection;
import com.formalspeech.networkService.conntection.ConnectionListener;
import com.formalspeech.networkService.conntection.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.*;

public class ServerConnectionHandlerImpl implements ServerConnectionHandler {
    private Map<String, List<String>> loginToForms;

    private Map<Connection, String> connectionToLogin;
    public ServerConnectionHandlerImpl() {
        loginToForms = new HashMap<>();
        Thread thread = new Thread(()->{

            try {
                ServerSocket serverSocket = new ServerSocket();
                while (true){
                    Connection connection = new SocketConnection(serverSocket.accept(), new ConnectionListener() {

                        @Override
                        public void onReceive(Connection connection, String data) {
                            Set<Connection> activeConnections = connectionToLogin.keySet();
                            if(activeConnections.contains(connection)){
                                String login = connectionToLogin.get(connection);
                                List<String> serializedForms = loginToForms.get(login);

                            } else {
                                try {
                                    Users users = new Users();

                                    //todo: parse data to form

                                    User user = users.getUser("", "");
                                    if(user == null){
                                        sendFormForLogIn(connection);
                                    } else{
                                        connectionToLogin.put(connection, user.getLogin());
                                        loginToForms.put(user.getLogin(), new ArrayList<>());
                                    }
                                } catch (ClassNotFoundException | SQLException e) {
                                    e.printStackTrace();
                                    connection.close();
                                }
                            }
                        }

                        @Override
                        public void onConnected(Connection connection) {
                            sendFormForLogIn(connection);
                        }

                        @Override
                        public void onDisconnected(Connection connection) {

                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                //disable
            }
        });
        thread.start();
    }

    private void sendFormForLogIn(Connection connection){
        //todo: send form for log in
        connection.send("");
    }

    @Override
    public void disable() {
    }

    @Override
    public void sendFormToUser(String login, String serializedForm) {
    }
}
