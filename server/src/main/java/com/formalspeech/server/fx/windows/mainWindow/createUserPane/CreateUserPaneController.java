package com.formalspeech.server.fx.windows.mainWindow.createUserPane;

import com.formalspeech.databaseService.User;
import com.formalspeech.databaseService.Users;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.IdentifierAndValue;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.formEssentials.serialization.StringSerializer;
import com.formalspeech.formEssentials.serialization.XmlSerializer;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Slf4j
public class CreateUserPaneController implements Initializable {

    @FXML
    private VBox vbComponents;

    private final Tab parentTab;

    private ArrayList<Component> components;


    public CreateUserPaneController(TabPaneConstructorParam param) {
        parentTab = param.parentTab;
    }
    public static String getFXMLFileName(){
        return "createUserPane.fxml";
    }
    @FXML
    void onCancelClicked(ActionEvent event) {
        parentTab.getTabPane().getTabs().remove(parentTab);
    }


    @FXML
    public void onCreateClicked(ActionEvent event) {
        try {
            if (((String) components.get(2).convertStringAsValue(components.get(2).getValueAsString())).equals(components.get(4).convertStringAsValue(components.get(4).getValueAsString()))) {
                try {
                    String login = (String) components.get(0).convertStringAsValue(components.get(0).getValueAsString());
                    String email = (String) components.get(1).convertStringAsValue(components.get(1).getValueAsString());
                    String password = (String) components.get(2).convertStringAsValue(components.get(2).getValueAsString());
                    boolean isActive = false;
                    Date lastActivityDate = null;
                    int accessLevel = (Integer) components.get(6).convertStringAsValue(components.get(6).getValueAsString());
                    Date registrationDate = null;

                    Users users = new Users();
                    if (users.add(new User(
                            login,
                            email,
                            password,
                            isActive,
                            lastActivityDate,
                            accessLevel,
                            registrationDate
                    ))){
                        AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "User "+ login + " successfully created");
                    } else {
                        AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "An user with such login already exists");
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            } else{
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Passwords are not equal");
            }
        } catch (IOException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "IncorrectData entered");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        components = new ArrayList<>();
        try {
            StringSerializer<Form> serializer= new XmlSerializer<>(Form.class);
            String data = new String(Files.readAllBytes(Path.of(CreateUserPaneController.class.getResource("createUserForm.xml").toURI())));
            Form loadedForm = serializer.readValueFromString(data);

            ComponentsHandler componentsHandler = new ComponentsHandler();
            ArrayList<IdentifierAndValue> componentsIdentifiersAndValues = loadedForm.getComponentsIdentifiersAndValues();
            for (IdentifierAndValue identifierAndValue :
                    componentsIdentifiersAndValues) {
                this.components.add(componentsHandler.getNewInstance(identifierAndValue.identifier, identifierAndValue.value));
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot load file!");
        }

        List<Pane> panes = new ArrayList<>();
        for (Component item :
                components) {
            try {
                panes.add(item.getLoadedPaneForFilling());
//                panes.add(item.getLoadedPaneForEditing());

            } catch (IOException e) {
                log.info("Cannot load component!");
                e.printStackTrace();
            }
        }
        vbComponents.getChildren().setAll(panes);
    }
}
