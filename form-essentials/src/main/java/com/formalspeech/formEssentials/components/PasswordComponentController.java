package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "passwordComponent.fxml")
public class PasswordComponentController extends Controller<String> {

    @FXML
    private PasswordField pfValue;

    public PasswordComponentController(String value) {
        super(value);
    }

    @Override
    public void showIncorrectEntered() {
        pfValue.setStyle("-fx-border-color: red");
    }

    public void beforeEditing() {

    }

    public void beforeFilling() {
//        tfValue.setText(value);
    }

    @Override
    protected void saveValue() {
        value = pfValue.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pfValue.setText(value);
    }
}
