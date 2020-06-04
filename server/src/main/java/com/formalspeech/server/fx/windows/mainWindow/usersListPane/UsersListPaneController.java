package com.formalspeech.server.fx.windows.mainWindow.usersListPane;

import com.formalspeech.databaseService.User;
import com.formalspeech.databaseService.Users;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.server.fx.windows.mainWindow.MainWindowController;
import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsersListPaneController implements Initializable {
    private final Tab parentTab;
    private final MainWindowController mainController;

    public static String getFXMLFileName(){
        return "usersListPane.fxml";
    }

    @FXML
    private ListView<User> lvAllUsers;

    @FXML
    private ListView<User> lvActiveUsers;

    public UsersListPaneController(TabPaneConstructorParam param) {
        this.mainController = param.mainController;
        this.parentTab = param.parentTab;
    }


    @FXML
    void onAddUserClick(ActionEvent event) {
        mainController.onNewUserClicked(new ActionEvent());
    }


    @FXML
    public void onFindByEmailClick(ActionEvent event) {
        String email = showTextInputDialog("Email", null, "Enter email");
        if (email != null) {
            try {
                Users users = Users.getInstance();
                lvAllUsers.getItems().setAll(users.getUsersByEmail(email));
                lvActiveUsers.getItems().setAll(users.getActiveUsersByEmail(email));
            } catch (ClassNotFoundException | SQLException e) {
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Unable to connect to databaseQ");
            }
        }
    }

    @FXML
    void onRefreshClick(ActionEvent event) {
        try {
            Users users = Users.getInstance();
            lvAllUsers.getItems().setAll(users.getAllUsers());
            lvActiveUsers.getItems().setAll(users.getActiveUsers());
        } catch (ClassNotFoundException | SQLException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Unable to connect to database");
        }
    }

    private void setCellFactoryForLvAllUsers(){
        lvAllUsers.setCellFactory(lv ->{
            ListCell<User> cell = new ListCell<>(){
                @Override
                public void updateItem(User item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getLogin());
                    }
                }
            };


            MenuItem remove = new MenuItem();
            remove.textProperty().bind(Bindings.format("Delete"));
            remove.setOnAction(event -> {
                mainController.deleteUser(cell.getItem().getLogin());
            });
            MenuItem openInfo = new MenuItem();
            openInfo.textProperty().bind(Bindings.format("Open profile"));
            openInfo.setOnAction(event -> {
                mainController.showUserInfo(cell.getItem().getLogin());
            });

            ContextMenu componentContextMenu = new ContextMenu();
            componentContextMenu.getItems().setAll(remove,openInfo);



            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }


    private void setCellFactoryForLvActiveUsers(){
        lvActiveUsers.setCellFactory(lv ->{
            ListCell<User> cell = new ListCell<>(){
                @Override
                public void updateItem(User item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getLogin());
                    }
                }
            };


            MenuItem remove = new MenuItem();
            remove.textProperty().bind(Bindings.format("Disconnect"));
            remove.setOnAction(event -> {
            });
            MenuItem openInfo = new MenuItem();
            openInfo.textProperty().bind(Bindings.format("Open profile"));
            openInfo.setOnAction(event -> {
                mainController.showUserInfo(cell.getItem().getLogin());
            });

            ContextMenu componentContextMenu = new ContextMenu();
            componentContextMenu.getItems().setAll(remove,openInfo);



            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellFactoryForLvAllUsers();
        setCellFactoryForLvActiveUsers();
        onRefreshClick(new ActionEvent());
    }

    private String showTextInputDialog(String title, String headerText, String label){
        TextInputDialog dialog = new TextInputDialog("");

        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(label);

        Optional<String> result = dialog.showAndWait();

        String answer = null;

        if(result.isPresent()){
            answer = result.get();
        }
        return answer;
    }
}
