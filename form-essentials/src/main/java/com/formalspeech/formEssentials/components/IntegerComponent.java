package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;

@ComponentAnnotation(label = "Text field for integer number", identifier = "integerComponent")
public class IntegerComponent extends ComponentToFill<IntegerComponentController, Integer>{
    public IntegerComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = IntegerComponentController.class;
    }

    @Override
    public Integer convertStringAsValue(String str) {
        if(str.equals("")){
            return 0;
        } else {
            return Integer.parseInt(str);
        }
    }

    @Override
    public String convertValueAsString(Integer value) {
        return Integer.toString(value);
    }

    @Override
    protected boolean checkValueForEditing(Integer value) {
        return true;
    }

    @Override
    protected boolean checkValueForFilling(Integer value) {
        return true;
    }
}
