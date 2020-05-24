package com.formalspeech.client.fx.windows.editWindow;

import com.formalspeech.formEssentials.FormHandler;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.fxmlEssentials.AlertWrapper;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
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

    private Component[] loadedComponents;

    @FXML
    private Label lbTitle;

    @FXML
    private VBox editList;

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
            boolean isFilledCorrect = true;
            for (Component c :
                    loadedComponents) {
                isFilledCorrect &= c.checkValue();
            }
            if (isFilledCorrect){
                formToFill.setFilledComponents(loadedComponents);
                listener.sendChanges(formToFill);
                ((Stage) btnSave.getScene().getWindow()).close();
            } else {
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
            loadedComponents = FormHandler.loadComponents(formToFill).toArray(new Component[0]);
            ArrayList<Pane> loadedPanes = new ArrayList<>();
            Arrays.stream(loadedComponents).forEach((Component) ->{
                loadedPanes.add(Component.getLoadedPane());
            });
            editList.getChildren().setAll(loadedPanes);
            lbTitle.setText(formToFill.getName());
        } catch (IOException e) {
            AlertWrapper.showAlert(Alert.AlertType.ERROR, "Cannot load components of the form", "Cannot load components of the form");
            ((Stage) editList.getScene().getWindow()).close();
        }
    }
}
