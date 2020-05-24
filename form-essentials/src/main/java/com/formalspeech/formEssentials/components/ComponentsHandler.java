package com.formalspeech.formEssentials.components;

import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public List<Class<Component>> getAvailableComponents() throws IOException {

        List<Class<Component>> componentsClasses = new ArrayList<>();

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
                                (!Component.class.equals(loadedClass))){
                            componentsClasses.add((Class<Component>) loadedClass);
                        }
                    } catch (ClassNotFoundException e) {
                        log.info("Failed loading \"" + className + "\"");
                    }
                }
            }
        }

        return componentsClasses;
    }

    static public ArrayList<Pane> loadPanes(ArrayList<Component> components){
        ArrayList<Pane> loadedPanes = new ArrayList<>();
        components.forEach((Component) ->{
            loadedPanes.add(Component.getLoadedPane());
        });
        return loadedPanes;
    }

}
