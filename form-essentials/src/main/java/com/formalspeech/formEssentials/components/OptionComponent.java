package com.formalspeech.formEssentials.components;


import com.formalspeech.formEssentials.annotations.ComponentAnnotation;

@ComponentAnnotation(label = "Options", identifier = "optionComponent")
public class OptionComponent extends ComponentToFill<OptionComponentController, String> {
    public OptionComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = OptionComponentController.class;
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
