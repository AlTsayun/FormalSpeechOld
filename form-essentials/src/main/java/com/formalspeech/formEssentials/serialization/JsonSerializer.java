package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.IllegalFormatException;

public class JsonSerializer<T> implements StringSerializer<T> {
    private Class<T> objectClass;

    public JsonSerializer(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public String writeAsString(T value) throws IllegalArgumentException{
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T readValueFromString(String data) throws IllegalArgumentException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, objectClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
}
