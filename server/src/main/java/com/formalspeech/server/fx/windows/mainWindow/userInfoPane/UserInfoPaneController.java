package com.formalspeech.server.fx.windows.mainWindow.userInfoPane;

import com.formalspeech.databaseService.User;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.IdentifierAndValue;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.formEssentials.serialization.StringSerializer;
import com.formalspeech.formEssentials.serialization.XmlSerializer;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.networkService.serverSide.ServerConnectionHandler;
import com.formalspeech.server.fx.windows.mainWindow.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
public class UserInfoPaneController implements Initializable {

    private final Tab parentTab;
    private final MainWindowController mainController;
    private final User user;
    private final ServerConnectionHandler connectionHandler;

    public static String getFXMLFileName(){
        return "userInfoPane.fxml";
    }


    public UserInfoPaneController(UserInfoPaneConstructorParam param) {
        this.parentTab = param.parentTab;
        this.user = param.user;
        this.mainController = param.mainController;
        this.connectionHandler = param.connectionHandler;
    }

    @FXML
    private TextArea taInfo;

    @FXML
    private ListView<Form> lvForms;

    @FXML
    private VBox vbFormPreview;

    @FXML
    void onRefreshClicked(ActionEvent event) {
        if(connectionHandler != null){
            StringSerializer<Form> serializer = new XmlSerializer<>(Form.class);
            List<String> serializedForms = connectionHandler.getSerializedFormsForUser(user.getLogin());
            List<Form> forms = new ArrayList<>();
            if(null != serializedForms) {
                for (String serializedForm :
                        serializedForms) {
                    try {
                        forms.add(serializer.readValueFromString(serializedForm));
                        log.info("Form successfully added!");
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
                lvForms.getItems().setAll(forms);
            }
        }
    }
    @FXML
    void onSendClick(ActionEvent event) {
        mainController.sendFormToUser(user.getLogin());
    }

    void onShowFormPreview(Form form){

        ArrayList<Pane> panes = new ArrayList<>();
        Map<String, String> componentIdentifierToValue = form.getComponentIdentifierToValue();
        List<String> identifiers = form.getComponentsIdentifiers();
        ComponentsHandler componentsHandler = new ComponentsHandler();
        for (String identifier:
             identifiers) {
            try {
                panes.add(componentsHandler.getNewInstance(identifier, componentIdentifierToValue.get(identifier)).getLoadedPaneForEditing());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        vbFormPreview.getChildren().setAll(panes);
    }

    private void setCellFactoryForLvForms(){
        lvForms.setCellFactory(lv ->{
            ListCell<Form> cell = new ListCell<>(){
                @Override
                public void updateItem(Form item, boolean empty){
                super.updateItem(item, empty);
                if(item == null){
                    setText(null);
                } else {
                    setText(item.getName());
                }
                }
            };


            MenuItem select = new MenuItem();
            select.textProperty().bind(Bindings.format("Select"));
            select.setOnAction(event -> onShowFormPreview(cell.getItem()));

            MenuItem saveToFile = new MenuItem();
            saveToFile.textProperty().bind(Bindings.format("Save to file"));
            saveToFile.setOnAction(event -> onSaveToFile(cell.getItem()));

            ContextMenu componentContextMenu = new ContextMenu();

            componentContextMenu.getItems().setAll(select, saveToFile);



            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });


            cell.setOnMouseClicked((mouseEvent)->{
                if (mouseEvent.getClickCount() == 2){
                    onShowFormPreview(cell.getItem());
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }

    private void onSaveToFile(Form form) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose folder to save form file into");
        File selectedDirectory = directoryChooser.showDialog(taInfo.getScene().getWindow());
        if (selectedDirectory != null) {
            log.info(selectedDirectory.getAbsolutePath());
            String fullFileName = selectedDirectory.getAbsolutePath() + "\\" + form.getName() + Form.FORM_FILE_EXTENSION;

            log.info("saving " + fullFileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFileName))) {

                StringSerializer<Form> serializer = new XmlSerializer<>(Form.class);
                String data = serializer.writeAsString(form);
                writer.write(data);
                AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "File " + fullFileName + " saved correctly!");
            } catch (IOException e) {
                e.printStackTrace();
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Error while writing to file!");
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot properly serialize the form!");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setCellFactoryForLvForms();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Login: " + user.getLogin() + "\n");
        stringBuilder.append("Email: " + user.getEmail() + "\n");
        stringBuilder.append("Registration date: " + user.getRegistrationDate().toString() + "\n");
        stringBuilder.append("Access level: " + user.getAccessLevel() + "\n");
        String lastActivityDate = user.getLastActivityDate() == null ? "never" : user.getLastActivityDate().toString();
        stringBuilder.append("Last active : " + lastActivityDate + "\n");
        stringBuilder.append("Is active now: " + user.isActive() + "\n");
        taInfo.setText(stringBuilder.toString());
        onRefreshClicked(new ActionEvent());
    }
}
