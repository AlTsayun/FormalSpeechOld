package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@ComponentAnnotation(label = "Text field for emails", identifier = "emailComponent")
public class EmailComponent extends ComponentToFill<EmailComponentController, String>{


    public EmailComponent(String convertedToStringValue) {
        super(convertedToStringValue);
        controllerClass = EmailComponentController.class;
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
    protected boolean checkValueForFilling(String value) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    @Override
    protected boolean checkValueForEditing(String value) {
        return value.equals("") || checkValueForFilling(value);
    }


}
