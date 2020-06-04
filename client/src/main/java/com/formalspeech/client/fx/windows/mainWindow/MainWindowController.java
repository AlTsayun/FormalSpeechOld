package com.formalspeech.client.fx.windows.mainWindow;

import com.formalspeech.formEssentials.serialization.StringSerializer;
import com.formalspeech.formEssentials.serialization.XmlSerializer;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import com.formalspeech.client.fx.windows.editWindow.EditWindowConstructorParam;
import com.formalspeech.client.fx.windows.editWindow.EditWindowController;
import com.formalspeech.networkService.conntection.Connection;
import com.formalspeech.networkService.conntection.ConnectionListener;
import com.formalspeech.networkService.conntection.SocketConnection;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
public class MainWindowController implements Initializable {
    public static final int PORT = 4205;
    private Connection connection;
    private StringSerializer<Form> serializer;
    private ConnectionListener listener = new ConnectionListener() {

        @Override
        public void onReceive(Connection connection, String data) {
            log.info("Received form");
            Form form = serializer.readValueFromString(data);
            lvMain.getItems().add(form);
        }

        @Override
        public void onConnected(Connection connection) {
//            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "Connected successfully!");
        }

        @Override
        public void onDisconnected(Connection connection) {
//            AlertWrapper.showAlert(Alert.AlertType.WARNING, "Disconnected", "The connection is being discontinued!");
        }
    };

    public MainWindowController() {
        serializer = new XmlSerializer<>(Form.class);
    }

    @FXML
    private ListView<Form> lvMain;


    @FXML
    private Button btnEdit;

    @FXML
    void onFillFormClicked(ActionEvent event) {
        Form item;
        if ((item = lvMain.getSelectionModel().getSelectedItem()) == null){
            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "No item selected", "No item selected");
        } else {
            fillForm(item);
        }
    }


    boolean establishConnection(){
        String serverAddress;
        boolean isThrown = true;
        while (isThrown) {
            if (null == (serverAddress = showTextInputDialog("Enter server address", null, "Enter server address:"))){
                break;
            }
            try {
                connection = new SocketConnection(new Socket(serverAddress, PORT), listener);
                isThrown = false;
            } catch (IOException e) {
                isThrown = true;
            }
        }
        return !isThrown;
    }

    public void configureWindowClosing(){
        btnEdit.getScene().getWindow().setOnCloseRequest(e ->{
            if(connection != null){
                closeConnection();
            }
        });
    }

    private void closeConnection(){
        connection.close();
        log.info("Closing connection");
    }


    @FXML
    void onDisconnectClicked(ActionEvent event) {
        connection.close();
    }

    @FXML
    void onSendFormClicked(ActionEvent event) {
        Form item;
        if ((item = lvMain.getSelectionModel().getSelectedItem()) == null){
            AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "No item selected", "No item selected");
        } else {
            sendForm(item);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        if (!establishConnection()){
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot continue working without connection!");
            Platform.exit();
        }
        lvMain.setCellFactory(lv ->{
            ListCell<Form> cell = new ListCell<Form>(){
                @Override
                @SneakyThrows
                public void updateItem(Form item, boolean empty){
                    super.updateItem(item, empty);
                    if(item == null){
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };

            cell.setOnMouseClicked((mouseEvent)->{
                if (mouseEvent.getClickCount() == 2){
                    Form item = cell.getItem();
                    fillForm(item);
                }
            });


            MenuItem editItem = new MenuItem();
            editItem.textProperty().bind(Bindings.format("Fill"));
            editItem.setOnAction(event -> {
                Form item = cell.getItem();
                fillForm(item);
            });

            MenuItem sendItem = new MenuItem();
            sendItem.textProperty().bind(Bindings.format("Send to server"));
            sendItem.setOnAction(event -> {
                Form item = cell.getItem();
                sendForm(item);
                lvMain.getItems().remove(item);
            });

            ContextMenu componentContextMenu = new ContextMenu();

            componentContextMenu.getItems().setAll(editItem, sendItem);


            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (!isNowEmpty) {
                    cell.setContextMenu(componentContextMenu);
                }
            });

            cell.setPrefHeight(40);

            return cell;


        });
    }

    private void showModalWindow(Parent loadedObject, String title) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene((Parent) loadedObject));
        stage.setResizable(false);
        stage.showAndWait();
    }

    private void fillForm(Form form) {

        try {
            log.info("Editing: " + form.toString());
            final Form[] filledForm = new Form[1];
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(
                    EditWindowController.getFXMLFileName(),
                    EditWindowController.class, new EditWindowConstructorParam(
                            form,
                            form1 -> filledForm[0] = form1
                    )
            );

            showModalWindow((Parent) loaderResponse.loadedObject, EditWindowController.getFXMLFileName());
            int selectedIndex = lvMain.getSelectionModel().getSelectedIndex();
            ObservableList<Form> items = lvMain.getItems();
            items.set(selectedIndex, filledForm[0]);
            lvMain.setItems(items);
        } catch (IOException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Cannot edit form", "Cannot edit form");
        }

    }

    private void sendForm(Form form){
        String data = serializer.writeAsString(form);
        connection.send(data);
        AlertWrapper.showAlert(Alert.AlertType.INFORMATION, "Success", "The form is sent successfully!");
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
