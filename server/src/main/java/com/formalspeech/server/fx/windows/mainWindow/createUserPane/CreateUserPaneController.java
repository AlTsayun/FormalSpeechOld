package com.formalspeech.server.fx.windows.mainWindow.createUserPane;

import com.formalspeech.server.fx.windows.mainWindow.TabPaneConstructorParam;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class CreateUserPaneController {

    @FXML
    private VBox vbUserInfo;
    private final Tab parentTab;

    public CreateUserPaneController(TabPaneConstructorParam param) {
        parentTab = param.parentTab;
    }
    @FXML
    void onCancelClicked(ActionEvent event) {

    }

    @FXML
    void onCreateClicked(ActionEvent event) {

    }

}
