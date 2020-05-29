package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "loginComponent.fxml")
public class LoginComponentController extends Controller<String> {

    @FXML
    private TextField tfValue;

    public LoginComponentController(String value) {
        super(value);
    }

    @Override
    public void showIncorrectEntered() {
        tfValue.setStyle("-fx-border-color: red");
    }

    @Override
    public void removeIncorrectEntered() {
        tfValue.setStyle("");
    }

    public void beforeEditing() {

    }

    public void beforeFilling() {
//        tfValue.setText(value);
    }

    @Override
    protected void saveValue() {
        value = tfValue.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfValue.setText(value);
    }
}
