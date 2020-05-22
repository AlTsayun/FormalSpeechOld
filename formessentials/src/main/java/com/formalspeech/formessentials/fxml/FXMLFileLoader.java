package com.formalspeech.formessentials.fxml;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class FXMLFileLoader {

    static public <T> FXMLFileLoaderResponse<Object, Object> loadFXML(String filename, Class<?> controllerClass, T param) throws IOException {
        FXMLLoader loader = new FXMLLoader(controllerClass.getResource(filename));

        Map<Class<?>, Callable<?>> creators = new HashMap<>();
        creators.put(controllerClass, () -> {
            Class<?> paramClass = param.getClass();
            return controllerClass.getConstructor(paramClass).newInstance(param);
        });

        loader.setControllerFactory(new Callback<Class<?>, Object>() {
            @SneakyThrows
            @Override
            public Object call(Class<?> controllerClass) {
                Callable<?> constructorToCall = creators.get(controllerClass);
                if (constructorToCall == null) {
                    //cannot find corresponding constructor with param
                    return controllerClass.getConstructor().newInstance();
                } else {
                    //constructor with param
                    return constructorToCall.call();
                }
            }
        });
        return new FXMLFileLoaderResponse<>(loader.load(), loader.getController());
    }
    static public <T> FXMLFileLoaderResponse<Object, Object> loadFXML(String filename, Class<?> controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader(controllerClass.getResource(filename));
        return new FXMLFileLoaderResponse<>(loader.load(), loader.getController());
    }
}
