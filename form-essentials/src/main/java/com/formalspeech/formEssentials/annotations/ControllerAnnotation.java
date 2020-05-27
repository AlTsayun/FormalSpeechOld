package com.formalspeech.formEssentials.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerAnnotation {
    String fxmlFileName();
}
