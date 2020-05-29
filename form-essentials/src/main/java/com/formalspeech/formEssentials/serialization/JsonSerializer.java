package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.IllegalFormatException;

public class JsonSerializer<T> implements StringSerializer<T> {
    private Class<T> objectClass;
    private ObjectMapper objectMapper;

    public JsonSerializer(Class<T> objectClass) {
        this.objectMapper = new ObjectMapper();
        this.objectClass = objectClass;
    }

    @Override
    public String writeAsString(T value) throws IllegalArgumentException{
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T readValueFromString(String data) throws IllegalArgumentException {
        try {
            return objectMapper.readValue(data, objectClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }
}
