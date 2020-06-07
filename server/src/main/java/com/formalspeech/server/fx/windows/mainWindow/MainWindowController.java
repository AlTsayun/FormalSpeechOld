package com.formalspeech.server.fx.windows.mainWindow;

import com.formalspeech.databaseService.User;
import com.formalspeech.databaseService.Users;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import com.formalspeech.networkService.serverSide.ServerConnectionHandler;
import com.formalspeech.networkService.serverSide.ServerConnectionHandlerImpl;
import com.formalspeech.server.fx.windows.mainWindow.createFormPane.CreateFormPaneController;
import com.formalspeech.server.fx.windows.mainWindow.createUserPane.CreateUserPaneController;
import com.formalspeech.server.fx.windows.mainWindow.userInfoPane.UserInfoPaneConstructorParam;
import com.formalspeech.server.fx.windows.mainWindow.userInfoPane.UserInfoPaneController;
import com.formalspeech.server.fx.windows.mainWindow.usersListPane.UsersListPaneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class MainWindowController implements Initializable {

    private ServerConnectionHandler connectionHandler;

    @FXML
    private ToggleGroup tgMainMenuNetworkServiceActivation;

    @FXML
    private TabPane tpMainWindow;


    @FXML
    private RadioMenuItem rmiNetworkServiceDisabled;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tpMainWindow.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        onNewFormClicked(new ActionEvent());
    }

    @FXML
    public void onAboutClicked(ActionEvent event) {

    }

    @FXML
    public void onDeleteFormClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file with form");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Form files", "*" + Form.FORM_FILE_EXTENSION));
        File file = fileChooser.showOpenDialog(tpMainWindow.getScene().getWindow());

        if (file != null) {
            if (AlertWrapper.showConfirmationDialog("Deleting file", "Are you sure, you want to delete the file?")) {
                if(file.delete())
                {
                    AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "File deleted successfully");
                }
                else
                {
                    AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the file");
                }
            }
        }
    }

    @FXML
    public void onEditFormClicked(ActionEvent event) {
        CreateFormPaneController controller = (CreateFormPaneController) showTab("Editing form", CreateFormPaneController.getFXMLFileName(), CreateFormPaneController.class);
        if (controller != null) {
            controller.onLoadClicked(new ActionEvent());
        }
    }

    @FXML
    public void onNetworkServiceEnableClicked(ActionEvent event) {
        connectionHandler = new ServerConnectionHandlerImpl();
        if (connectionHandler.enable()){
            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "Network service is successfully enabled!");
        } else {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Could not properly start network service!");
            rmiNetworkServiceDisabled.setSelected(true);
        }
    }

    @FXML
    public void onNetworkServiceDisableClicked(ActionEvent event) {
        connectionHandler.disable();
        connectionHandler = null;
    }

    @FXML
    public void onNewFormClicked(ActionEvent event) {
        showTab("New form", CreateFormPaneController.getFXMLFileName(), CreateFormPaneController.class);
    }

    private Object showTab(String title, String fxmlFileName, Class controllerClass) {
        try {
            Tab tab = new Tab(title);
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(fxmlFileName, controllerClass, new TabPaneConstructorParam(tab, connectionHandler, this));
            tab.setContent((Node) loaderResponse.loadedObject);
            tpMainWindow.getTabs().add(tab);
            tpMainWindow.getSelectionModel().select(tab);
            return loaderResponse.controller;
        } catch (IOException e){
            log.info("Error while loading pane!");
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void onSendFormClicked(ActionEvent event) {
        String login = getLoginDialog();
        if (login != null){
            sendFormToUser(login);
        }
    }

    private String getLoginDialog() {
        return showTextInputDialog("Enter login", null, "Login:");
    }


    @FXML
    public void onFindUsersByEmailClicked(ActionEvent event) {
        UsersListPaneController controller = (UsersListPaneController) showTab("Users", UsersListPaneController.getFXMLFileName(), UsersListPaneController.class);
        controller.onFindByEmailClick(new ActionEvent());
    }

    @FXML
    public void onFindUserByLoginClicked(ActionEvent event) {
        String login = getLoginDialog();
        if (login != null){
            showUserInfo(login);
        }
    }

    @FXML
    public void onDeleteUserClicked(ActionEvent event) {
        String login = getLoginDialog();
        if (login != null){
            deleteUser(login);
        }
    }

    public void deleteUser(String login) {
        if (AlertWrapper.showConfirmationDialog("Deleting user", "Are you sure, you want to delete this user?")) {
            try {
                Users users = Users.getInstance();
                if (users.deleteUser(login)){
                    AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "The user " + login +" is deleted successfully!");
                } else {
                    AlertWrapper.showAlert(Alert.AlertType.ERROR, "ERROR", "The user " + login +" cannot be deleted!");
                }

            } catch (ClassNotFoundException | SQLException e) {
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Error while connection to database!");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onNewUserClicked(ActionEvent event) {
        showTab("New user", CreateUserPaneController.getFXMLFileName(), CreateUserPaneController.class);
    }


    @FXML
    public void onShowAllUsersClicked(ActionEvent event) {
        showTab("Users", UsersListPaneController.getFXMLFileName(), UsersListPaneController.class);
    }

    @FXML
    public void onExitClicked(ActionEvent event) {
        if (AlertWrapper.showConfirmationDialog("Exit", "All unsaved data will be lost. Continue?")){
            System.exit(0);
        }
    }

    private void showErrorWhileLoadingApp(){
        AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error!", "Cannot properly load the app");
    }

    public void showUserInfo(String login){
        try {
            Users users = Users.getInstance();
            User user = users.getUserByLogin(login);

            if (user != null) {
                Tab tab = new Tab(user.getLogin());
                FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(UserInfoPaneController.getFXMLFileName(), UserInfoPaneController.class, new UserInfoPaneConstructorParam(tab, connectionHandler, this, user));
                tab.setContent((Node) loaderResponse.loadedObject);
                tpMainWindow.getTabs().add(tab);
                tpMainWindow.getSelectionModel().select(tab);
            } else{
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot find such user");
            }
        } catch (IOException e){
            log.info("Error while loading pane!");
            e.printStackTrace();
        } catch (SQLException | ClassNotFoundException throwables) {
            log.info("Error connection to database!");
        }
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

    public boolean sendFormToUser(String login){
        if (connectionHandler == null) {
            if (!AlertWrapper.showConfirmationDialog("Network service disabled", "Network service isn't enabled. Enable it?")) {
                return false;
            } else {
                onNetworkServiceEnableClicked(new ActionEvent());
            }
        }
        try {
            Users users = Users.getInstance();
            User user = users.getUserByLogin(login);
            if (user != null){
                if (user.isActive()){
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open file with form");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Form files", "*" + Form.FORM_FILE_EXTENSION));
                    File selectedFile = fileChooser.showOpenDialog(tpMainWindow.getScene().getWindow());
                    if (selectedFile != null) {
                        try {
                            String data = new String(Files.readAllBytes(selectedFile.toPath()));
                            connectionHandler.sendFormToUser(login, data);
                            return true;
                        } catch (IOException e) {
                            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot properly open file with form " + selectedFile.getPath());
                        }
                    }
                } else {
                    AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "User is not active at the moment!");
                }
            } else {
                AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot find such user!");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot establish connection to database!");
        }
        return false;
    }


}
