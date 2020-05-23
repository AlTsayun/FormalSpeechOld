package com.formalspeech.server.fx.windows.mainWindow.createFormPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateFormPaneController implements Initializable {

    private final Tab parentTab;

    public CreateFormPaneController(CreateFormPaneConstructorParam param) {
        parentTab = param.parentTab;
    }

    public static String getFXMLFileName(){
        return "createFormPane.fxml";
    }


    @FXML
    private ListView<?> lvAvailableComponents;

    @FXML
    private ListView<?> lvSelectedComponents;

    @FXML
    private TextField tfFormFileName;

    @FXML
    private VBox vbPreview;

    @FXML
    void onCancelClicked(ActionEvent event) {

    }

    @FXML
    void onMoveFromSelectedClicked(ActionEvent event) {

    }

    @FXML
    void onMoveToSelectedClicked(ActionEvent event) {

    }

    @FXML
    void onSaveClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

