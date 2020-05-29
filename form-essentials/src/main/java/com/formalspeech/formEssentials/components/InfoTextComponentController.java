package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "infoTextComponent.fxml")
public class InfoTextComponentController extends Controller<String> {

    @FXML
    private TextArea taInfoText;

    public InfoTextComponentController(String value) {
        super(value);
    }

    @Override
    public void showIncorrectEntered() { }
    @Override
    public void removeIncorrectEntered() {
//        taInfoText.setStyle("");
    }

    public void beforeEditing() {
        taInfoText.setEditable(true);
    }

    public void beforeFilling() {
        taInfoText.setEditable(false);
    }

    @Override
    protected void saveValue() {
        value = taInfoText.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taInfoText.setText(value);
    }
}
