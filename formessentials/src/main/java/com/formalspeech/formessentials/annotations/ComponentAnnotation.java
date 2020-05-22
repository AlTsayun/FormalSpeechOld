package com.formalspeech.formessentials.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentAnnotation {
    String fxmlFileName();
    String label();
}
