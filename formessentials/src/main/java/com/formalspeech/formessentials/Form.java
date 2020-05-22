package com.formalspeech.formessentials;

import com.formalspeech.formessentials.components.Component;

public class Form {

    private String name;
    private Class<Component>[] componentsClasses;
    private Component[] filledComponents;

    public Form(String name, Class<Component>[] componentsClasses) {
        this.name = name;
        this.componentsClasses = componentsClasses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<Component>[] getComponentsClasses() {
        return componentsClasses;
    }

    public void setComponentsClasses(Class<Component>[] componentsClasses) {
        this.componentsClasses = componentsClasses;
    }

    public Component[] getFilledComponents() {
        return filledComponents;
    }

    public void setFilledComponents(Component[] filledComponents) {
        this.filledComponents = filledComponents;
    }

    public String getCreateTableQuery(){
        return null;
    }
}
