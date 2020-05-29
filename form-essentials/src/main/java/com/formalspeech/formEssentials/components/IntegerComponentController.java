package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
@ControllerAnnotation(fxmlFileName = "integerComponent.fxml")
public class IntegerComponentController extends Controller<Integer>{

    @FXML
    private TextField tfValue;

    public IntegerComponentController(Integer value) {
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
        value = Integer.parseInt(tfValue.getText());
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
        tfValue.setText(value.toString());
        tfValue.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfValue.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
