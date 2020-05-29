package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

@ControllerAnnotation(fxmlFileName = "emailComponent.fxml")
public class EmailComponentController extends Controller<String>{

    @FXML
    private TextField tfValue;

    public EmailComponentController(String value) {
        super(value);
    }

    @Override
    public void beforeEditing() {

    }

    @Override
    public void beforeFilling() {

    }

    @Override
    protected void saveValue() {
        value = tfValue.getText();
    }

    @Override
    public void showIncorrectEntered() {
        tfValue.setStyle("-fx-border-color: red");
    }

    @Override
    public void removeIncorrectEntered() {
        tfValue.setStyle("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfValue.setText(value);
    }
}
