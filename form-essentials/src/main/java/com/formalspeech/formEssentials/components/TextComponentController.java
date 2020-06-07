package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "textComponent.fxml")
public class TextComponentController extends Controller<String> {

    @FXML
    private TextArea taText;

    public TextComponentController(String value) {
        super(value);
    }

    @Override
    public void showIncorrectEntered() { }
    @Override
    public void removeIncorrectEntered() {
//        taInfoText.setStyle("");
    }

    public void beforeEditing() {
        taText.setEditable(true);
    }

    public void beforeFilling() {
        taText.setEditable(true);
    }

    @Override
    protected void saveValue() {
        value = taText.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taText.setText(value);
    }
}
