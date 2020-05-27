package com.formalspeech.formEssentials.serialization;

import com.formalspeech.formEssentials.Form;

import java.io.IOException;
import java.util.IllegalFormatException;

public interface StringSerializer<T> {
    String writeAsString(T value) throws IllegalArgumentException;
    T readValueFromString(String data) throws IllegalArgumentException;
}
