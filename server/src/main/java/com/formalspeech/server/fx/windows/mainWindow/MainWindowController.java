package com.formalspeech.server.fx.windows.mainWindow;

import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import com.formalspeech.server.fx.windows.mainWindow.createFormPane.CreateFormPaneConstructorParam;
import com.formalspeech.server.fx.windows.mainWindow.createFormPane.CreateFormPaneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class MainWindowController implements Initializable {


    @FXML
    private ToggleGroup tgMainMenuNetworkServiceActivation;

    @FXML
    private TabPane tpMainWindow;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onNewFormClicked(new ActionEvent());
    }

    @FXML
    void onAboutClicked(ActionEvent event) {

    }

    @FXML
    void onDeleteFormClicked(ActionEvent event) {

    }

    @FXML
    void onEditFormClicked(ActionEvent event) {

    }

    @FXML
    void onNetworkServiceDisableClicked(ActionEvent event) {

    }

    @FXML
    void onNetworkServiceEnableClicked(ActionEvent event) {

    }

    @FXML
    void onNewFormClicked(ActionEvent event) {
        try {
            Tab tab = new Tab("New form");
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(CreateFormPaneController.getFXMLFileName(), CreateFormPaneController.class, new CreateFormPaneConstructorParam(tab));
            tab.setContent((Node) loaderResponse.loadedObject);
            tpMainWindow.getTabs().add(tab);
        } catch (IOException e){
            log.info("Error while loading createFormPane!");
            e.printStackTrace();
        }
    }

    @FXML
    void onSendFormClicked(ActionEvent event) {

    }


    @FXML
    void onShowActiveUsersClicked(ActionEvent event) {

    }

    @FXML
    void onFindUserByEmailClicked(ActionEvent event) {

    }

    @FXML
    void onFindUserByLoginClicked(ActionEvent event) {

    }

    @FXML
    void onDeleteUserClicked(ActionEvent event) {

    }

    @FXML
    void onNewUserClicked(ActionEvent event) {

    }
    @FXML
    void onExitClicked(ActionEvent event) {
        if (AlertWrapper.showConfirmationDialog("Exit", "All unsaved data will be lost. Continue?")){
            System.exit(0);
        }
    }

    private void showErrorWhileLoadingApp(){
        AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error!", "Cannot properly load the app");
    }



}
