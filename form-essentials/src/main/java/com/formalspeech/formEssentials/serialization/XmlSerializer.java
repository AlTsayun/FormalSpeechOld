package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.IllegalFormatException;

public class XmlSerializer<T> implements StringSerializer<T>{
    private Class<T> objectClass;

    public XmlSerializer(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public String writeAsString(T value) throws IllegalArgumentException {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T readValueFromString(String data) throws IllegalArgumentException {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(data, objectClass);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
