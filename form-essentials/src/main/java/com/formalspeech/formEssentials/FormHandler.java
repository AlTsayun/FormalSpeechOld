package com.formalspeech.formEssentials;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import com.formalspeech.formEssentials.components.ComponentsHandler;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormHandler {

    static public ArrayList<Component> loadComponents(Form form) throws IOException {
        ArrayList<Component> components = new ArrayList<>();
        for (Class<Component> componentClass:
                form.getComponentsClasses()){
            FXMLFileLoaderResponse<Object, Object> loaderResponse = FXMLFileLoader.loadFXML(
                    componentClass.getAnnotation(ComponentAnnotation.class).fxmlFileName(),
                    componentClass);
            ((Component) loaderResponse.controller).setLoadedPane((Pane) loaderResponse.loadedObject);
            components.add((Component) loaderResponse.controller);
        }
        return components;

    }


    static public ArrayList<Pane> loadPanes(Form form)throws IOException{
        ArrayList<Component> loadedComponents = FormHandler.loadComponents(form);
        return ComponentsHandler.loadPanes(loadedComponents);
    }




}
