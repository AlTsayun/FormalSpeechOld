package com.formalspeech.client.fx.windows.editWindow;

import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.formEssentials.Form;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditWindowController implements Initializable {

    public static String getFXMLFileName(){
        return "editWindow.fxml";
    }

    private final Form formToFill;

    private final EditWindowListener listener;

    private List<Component> components = new ArrayList<>();

    @FXML
    private Label lbTitle;

    @FXML
    private VBox vbEdit;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSave;

    @FXML
    void onBtnCancelClick(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBtnSaveClick(ActionEvent event) {
        try {
            for (Component c :
                    components) {
                c.getValueAsString();
            }
            Form filledForm = new Form(lbTitle.getText(), components);
            listener.sendChanges(filledForm);
            ((Stage) btnSave.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            btnSave.setStyle("-fx-focus-color: red");
        }

    }


    public EditWindowController(EditWindowConstructorParam param) {
        formToFill = param.form;
        listener = param.listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ArrayList<Pane> loadedPanes = new ArrayList<>();
            Map<String, String> identifierToValue = formToFill.getComponentIdentifierToValue();
            ComponentsHandler handler = new ComponentsHandler();
            for (String identifier :
                    formToFill.getComponentsIdentifiers()) {
                Component component = handler.getNewInstance(identifier, identifierToValue.get(identifier));
                loadedPanes.add(component.getLoadedPaneForFilling());
                components.add(component);
            }
            vbEdit.getChildren().setAll(loadedPanes);
            lbTitle.setText(formToFill.getName());
        } catch (IOException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Error", "Cannot load components of the form!");
            ((Stage) vbEdit.getScene().getWindow()).close();
        }
    }
}
