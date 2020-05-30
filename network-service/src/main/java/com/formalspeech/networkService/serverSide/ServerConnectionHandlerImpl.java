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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

public class ServerConnectionHandlerImpl implements ServerConnectionHandler {
    static final int PORT = 4200;
    private final String LOG_IN_FORM_FILE_NAME = "logInForm.xml";
    private Map<String, List<String>> loginToForms;
    private BiMap<Connection, String> connectionToLogin;

    public ServerConnectionHandlerImpl() {
        loginToForms = new HashMap<>();
        connectionToLogin = HashBiMap.create();
        Thread thread = new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
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
                                    Users users = new Users();

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
                            String login = connectionToLogin.get(connection);
                            if (login != null) {
                                loginToForms.remove(login);
                                connectionToLogin.remove(connection);
                            }
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
        try {
            String data = new String(Files.readAllBytes(Path.of(getClass().getResource(LOG_IN_FORM_FILE_NAME).toURI())));
            connection.send(data);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disable() {
        Set<Connection> connections = connectionToLogin.keySet();
        for (Connection c :
                connections) {
            c.close();
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
