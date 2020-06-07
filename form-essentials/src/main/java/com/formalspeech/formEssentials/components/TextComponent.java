package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;

@ComponentAnnotation(label = "Text", identifier = "textComponent")
public class TextComponent extends ComponentWithInfo<TextComponentController, String>{


    public TextComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = TextComponentController.class;
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
