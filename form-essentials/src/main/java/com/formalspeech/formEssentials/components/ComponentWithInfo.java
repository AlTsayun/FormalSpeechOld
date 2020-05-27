package com.formalspeech.formEssentials.components;

public abstract class ComponentWithInfo<ControllerType extends Controller, ValueType> extends Component<ControllerType, ValueType> {


    public ComponentWithInfo(String convertedToStringValue) {
        super(convertedToStringValue);
    }
}
