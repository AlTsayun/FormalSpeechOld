package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerAnnotation(fxmlFileName = "optionComponent.fxml")
public class OptionComponentController extends Controller<String> {

    static final String SEPARATOR = ";";

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfValue1;

    @FXML
    private TextField tfValue2;

    @FXML
    private RadioButton rb1;

    @FXML
    private ToggleGroup toggleGroup;

    @FXML
    private RadioButton rb2;

    public OptionComponentController(String value) {
        super(value);

    }

    @Override
    public void beforeEditing() {
        tfDescription.setEditable(true);
        tfValue1.setEditable(true);
        tfValue2.setEditable(true);
    }

    @Override
    public void beforeFilling() {
        tfDescription.setEditable(false);
        tfValue1.setEditable(false);
        tfValue2.setEditable(false);
    }

    @Override
    protected void saveValue() {
        StringBuilder stringBuilder = new StringBuilder();
        if (toggleGroup.getSelectedToggle() == rb1){
            stringBuilder.append("1;");
        } else {
            stringBuilder.append("2;");
        }
        stringBuilder.append(tfDescription.getText() + ";");
        stringBuilder.append(tfValue1.getText() + ";");
        stringBuilder.append(tfValue2.getText());
        value = stringBuilder.toString();
    }

    @Override
    public void showIncorrectEntered() {

    }

    @Override
    public void removeIncorrectEntered() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] strings = value.split(SEPARATOR, 4);
        if (strings.length == 4) {
            tfDescription.setText(strings[1]);
            tfValue1.setText(strings[2]);
            tfValue2.setText(strings[3]);
            if (Integer.parseInt(strings[0]) == 1){
                toggleGroup.selectToggle(rb1);
            } else {
                toggleGroup.selectToggle(rb2);
            }
        } else{
            toggleGroup.selectToggle(rb1);
        }
    }
}
