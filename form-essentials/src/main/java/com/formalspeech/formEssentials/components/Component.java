package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ControllerAnnotation;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.Serializable;

public abstract class Component<ControllerType extends Controller, ValueType> implements Serializable {

//    public abstract Pane getLoadedPaneForEditing() throws IOException;
//    public abstract Pane getLoadedPaneForFilling() throws IOException;
//    public abstract String getValue() throws IOException;

    protected ControllerType controller;
    protected Class<ControllerType> controllerClass;
    protected ValueType value;
    protected boolean isEditing;

    protected abstract ValueType convertStringAsValue(String str);
    protected abstract String convertValueAsString(ValueType value);
    protected abstract boolean checkValueForEditing(ValueType value);
    protected abstract boolean checkValueForFilling(ValueType value);

    protected void showIncorrectEntered(){
        controller.showIncorrectEntered();
    }

    public Component(String convertedToStringValue) {
        value = convertStringAsValue(convertedToStringValue);
    }

    public Pane getLoadedPaneForEditing() throws IOException{
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(controllerClass.getAnnotation(ControllerAnnotation.class).fxmlFileName(), controllerClass, value);
        controller = (ControllerType) loaderResponse.controller;
        controller.beforeEditing();
        isEditing = true;
        return (Pane) loaderResponse.loadedObject;
    }

    public Pane getLoadedPaneForFilling() throws IOException{
        FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(controllerClass.getAnnotation(ControllerAnnotation.class).fxmlFileName(), controllerClass, value);
        controller = (ControllerType) loaderResponse.controller;
        controller.beforeFilling();
        isEditing = false;
        return (Pane) loaderResponse.loadedObject;
    }

    protected void saveValue() throws IllegalStateException, IOException{
        if (controller != null){
            ValueType valueToSave = (ValueType) controller.getValue();
            if ((isEditing && checkValueForEditing(valueToSave)) ||
                    ((!isEditing) && checkValueForFilling(valueToSave))) {
                value = valueToSave;
            } else {
                showIncorrectEntered();
                throw new IOException("Incorrect data entered");
            }

        } else {
            throw new IllegalStateException("saveValue() call before loading controller!");
        }

    }

    public String getValueAsString() throws IOException{
        try {
            saveValue();
        } catch (IllegalStateException e) {
            value = null;
        }
        return convertValueAsString(value);
    }

}
