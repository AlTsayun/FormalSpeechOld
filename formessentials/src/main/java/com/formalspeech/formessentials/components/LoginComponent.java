package com.formalspeech.formessentials.components;

import com.formalspeech.formessentials.annotations.ComponentAnnotation;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

@ComponentAnnotation(fxmlFileName = "loginComponent.fxml", label = "Text field for login")
public class LoginComponent implements Component{

    @FXML
    private TextField tfValue;

    private Pane loadedPane;

    @Override
    public Object getValue() throws IOException {
        return tfValue.getText();
    }

    @Override
    public void setValue(Object value) {
        tfValue.setText((String) value);
    }

    @Override
    public boolean checkValue() {
        return true;
    }

    @Override
    public void setLoadedPane(Pane pane) {
        this.loadedPane = pane;
    }

    @Override
    public Pane getLoadedPane() {
        return loadedPane;
    }


}
