package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ComponentAnnotation(label = "Text field for login", identifier = "loginComponent")
public class LoginComponent extends ComponentToFill<LoginComponentController,String> {


    public LoginComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = LoginComponentController.class;
    }

    @Override
    protected String convertStringAsValue(String str) {
        return str;
    }

    @Override
    protected String convertValueAsString(String value) {
        return value;
    }

    @Override
    protected boolean checkValueForEditing(String value) {
        Pattern pattern = Pattern.compile("[a-z0-9-]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    @Override
    protected boolean checkValueForFilling(String value) {
        return value.equals("") || checkValueForEditing(value);
    }


}
