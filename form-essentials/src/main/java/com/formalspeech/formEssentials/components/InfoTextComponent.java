package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import javafx.scene.layout.Pane;

import java.io.IOException;

@ComponentAnnotation(label = "Info text", identifier = "infoTextComponent")
public class InfoTextComponent extends ComponentWithInfo<InfoTextComponentController, String>{


    public InfoTextComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = InfoTextComponentController.class;
    }


    @Override
    public String convertStringAsValue(String str) {
        return str;
    }

    @Override
    public String convertValueAsString(String value) {
        return value;
    }

    @Override
    protected boolean checkValueForEditing(String value) {
        return true;
    }
    @Override
    protected boolean checkValueForFilling(String value) {
        return true;
    }
}
