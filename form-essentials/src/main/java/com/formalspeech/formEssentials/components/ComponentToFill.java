package com.formalspeech.formEssentials.components;

public abstract class ComponentToFill <ControllerType extends Controller, ValueType> extends Component<ControllerType, ValueType>{


    public ComponentToFill(String convertedToStringValue) {
        super(convertedToStringValue);
    }
}
