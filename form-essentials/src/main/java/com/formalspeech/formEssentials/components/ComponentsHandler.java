package com.formalspeech.formEssentials.components;

import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.fxmlEssentials.FXMLFileLoader;
import com.formalspeech.fxmlEssentials.FXMLFileLoaderResponse;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

@Slf4j
public class ComponentsHandler {

    private final ClassLoader classLoader;
    private final URL resourceFolder;

    public ComponentsHandler() {
        resourceFolder = ComponentsHandler.class.getResource("");
        if (resourceFolder == null){
            throw new MissingResourceException("Cannot find resource", "com.formalspeech.formEssentials.components", "");
        }
        URL[] urls = new URL[]{resourceFolder};
        classLoader = new URLClassLoader(urls);
    }

    public List<Component> getAvailableComponents() throws IOException {

        List<Component> components = new ArrayList<>();

        try (InputStream in = resourceFolder.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String resourceContent;

            while ((resourceContent = reader.readLine()) != null) {
                int pos = resourceContent.lastIndexOf(".");
                if (resourceContent.substring(pos).equals(".class")){
                    String className = resourceContent.substring(0, pos);
                    try {
                        Class<?> loadedClass = classLoader.loadClass("com.formalspeech.formEssentials.components." + className);;
                        if(Component.class.isAssignableFrom(loadedClass) &&
                                (!Modifier.isAbstract(loadedClass.getModifiers()))){

                            try {
                                Component component = (Component) loadedClass.getConstructor(String.class).newInstance("");
                                components.add(component);
                            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                                log.info("Error while calling  constructor of " + loadedClass.toString());
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        log.info("Failed loading \"" + className + "\"");
                    }
                }
            }
        }

        return components;
    }

    public List<Class<Component>> getAvailableComponentClasses() throws IOException{
        List<Class<Component>> componentClasses = new ArrayList<>();

        try (InputStream in = resourceFolder.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String resourceContent;

            while ((resourceContent = reader.readLine()) != null) {
                int pos = resourceContent.lastIndexOf(".");
                if (resourceContent.substring(pos).equals(".class")){
                    String className = resourceContent.substring(0, pos);
                    try {
                        Class<?> loadedClass = classLoader.loadClass("com.formalspeech.formEssentials.components." + className);;
                        if(Component.class.isAssignableFrom(loadedClass) &&
                                (!Modifier.isAbstract(loadedClass.getModifiers()))
                        ){
                            componentClasses.add((Class<Component>) loadedClass);
                        }
                    } catch (ClassNotFoundException e) {
                        log.info("Failed loading \"" + className + "\"");
                    }
                }
            }
        }

        return componentClasses;
    }

    public Component getNewInstance(Class<? extends Component> componentClass, String value) throws IllegalArgumentException{

        try {
            return componentClass.getConstructor(String.class).newInstance(value);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Error while calling  constructor of " + componentClass.toString());
        }
    }

    public Component getNewInstance(String componentIdentifier, String value) throws IllegalArgumentException{
        try {
            List<Class<Component>> componentClasses = getAvailableComponentClasses();
            for (Class<Component> componentClass:
                    componentClasses){
                if (componentClass.getAnnotation(ComponentAnnotation.class).identifier().equals(componentIdentifier)){
                    return getNewInstance(componentClass, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Cannot find such component!");
    }
}
