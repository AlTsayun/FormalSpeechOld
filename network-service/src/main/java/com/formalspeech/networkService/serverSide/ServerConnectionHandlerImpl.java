package com.formalspeech.networkService.serverSide;

import com.formalspeech.databaseService.User;
import com.formalspeech.databaseService.Users;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.LoginComponent;
import com.formalspeech.formEssentials.components.PasswordComponent;
import com.formalspeech.formEssentials.serialization.StringSerializer;
import com.formalspeech.formEssentials.serialization.XmlSerializer;
import com.formalspeech.networkService.conntection.Connection;
import com.formalspeech.networkService.conntection.ConnectionListener;
import com.formalspeech.networkService.conntection.SocketConnection;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ServerConnectionHandlerImpl implements ServerConnectionHandler {
    static final int PORT = 4205;
    private final String LOG_IN_FORM_FILE_NAME = "logInForm.xml";
    private Map<String, List<String>> loginToForms;
    private BiMap<Connection, String> connectionToLogin;
    private ServerSocket serverSocket;

    public ServerConnectionHandlerImpl() {
        loginToForms = new ConcurrentHashMap<>();
        connectionToLogin = Maps.synchronizedBiMap(HashBiMap.create());

    }

    private void sendFormForLogIn(Connection connection){
        try {
            String data = new String(Files.readAllBytes(Path.of(getClass().getResource(LOG_IN_FORM_FILE_NAME).toURI())));
            connection.send(data);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {
        try {
            serverSocket.close();
            Set<Connection> connections = connectionToLogin.keySet();
            for (Connection c :
                    connections) {
                c.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Error while closing serverSocket!");
        }

        log.info("ServerConnectionHandler is disabled");
    }

    @Override
    public boolean enable() {
        try {
            serverSocket = new ServerSocket(PORT);
            Thread thread = new Thread(()->{
                try {
                    while (true){
                        new SocketConnection(serverSocket.accept(), new ConnectionListener() {

                            @Override
                            public void onReceive(Connection connection, String data) {
                                Set<Connection> activeConnections = connectionToLogin.keySet();
                                if(activeConnections.contains(connection)){
                                    String login = connectionToLogin.get(connection);
                                    List<String> serializedForms = loginToForms.get(login);
                                        serializedForms.add(data);
                                } else {
                                    try {
                                        Users users = Users.getInstance();

                                        StringSerializer<Form> serializer = new XmlSerializer<>(Form.class);
                                        Form form = serializer.readValueFromString(data);
                                        Map<String, String> identifierToValue = form.getComponentIdentifierToValue();

                                        User user = users.getUser(
                                                identifierToValue.get(LoginComponent.class.getAnnotation(ComponentAnnotation.class).identifier()),
                                                identifierToValue.get(PasswordComponent.class.getAnnotation(ComponentAnnotation.class).identifier())
                                        );
                                        if(user == null){
                                            sendFormForLogIn(connection);
                                        } else{
                                            log.info("User " + user.getLogin() + " is authorized");
                                            connectionToLogin.put(connection, user.getLogin());
                                            loginToForms.put(user.getLogin(), new ArrayList<>());
                                            users.setUserActive(user.getLogin(), true);
                                        }
                                    } catch (ClassNotFoundException | SQLException e) {
                                        e.printStackTrace();
                                        connection.close();
                                    }
                                }
                            }

                            @Override
                            public void onConnected(Connection connection) {
                                log.info("New connection " + connection.toString());
                                sendFormForLogIn(connection);
                            }

                            @Override
                            public void onDisconnected(Connection connection) {
                                try {
                                    String login = connectionToLogin.get(connection);
                                    log.info("client disconnected");
                                    if (login != null) {
                                        log.info("User " + login + " disconnected");
                                        Users users = Users.getInstance();
                                        users.setUserActive(login, false);
                                        loginToForms.remove(login);
                                        connectionToLogin.remove(connection);
                                    }
                                } catch (SQLException | ClassNotFoundException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    disable();
                }
            });
            thread.start();
            log.info("ServerConnectionHandler is enabled");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sendFormToUser(String login, String serializedForm) {
        Connection connection = connectionToLogin.inverse().get(login);
        connection.send(serializedForm);
    }

    @Override
    public List<String> getSerializedFormsForUser(String login) {
        return loginToForms.get(login);
    }


}
