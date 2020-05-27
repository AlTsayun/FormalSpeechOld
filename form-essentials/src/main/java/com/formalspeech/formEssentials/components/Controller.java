package com.formalspeech.formEssentials.components;

import javafx.fxml.Initializable;

public abstract class Controller<ValueType> implements Initializable {

    protected ValueType value;

    public Controller(ValueType value){
        this.value = value;
    }
    abstract public void beforeEditing();
    abstract public void beforeFilling();
    abstract protected void saveValue();
    public ValueType getValue(){
        saveValue();
        return value;
    }
    abstract public void showIncorrectEntered();


}
