package com.formalspeech.server.fx.windows.mainWindow.usersListPane;

import com.formalspeech.databaseService.User;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class UsersListPaneController implements Initializable {
    private final Tab parentTab;

    @FXML
    private ListView<User> lvAllUsers;

    @FXML
    private ListView<User> lvActiveUsers;

    public UsersListPaneController(TabPaneConstructorParam param) {
            parentTab = param.parentTab;
    }


    @FXML
    void onAddUserClick(ActionEvent event) {

    }

    @FXML
    void onRefreshClick(ActionEvent event) {

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
            });
            MenuItem openInfo = new MenuItem();
            openInfo.textProperty().bind(Bindings.format("Open profile"));
            openInfo.setOnAction(event -> {
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
            remove.textProperty().bind(Bindings.format("Disconnect"));
            remove.setOnAction(event -> {
            });
            MenuItem openInfo = new MenuItem();
            openInfo.textProperty().bind(Bindings.format("Open profile"));
            openInfo.setOnAction(event -> {
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

        onRefreshClick(new ActionEvent());
    }
}
