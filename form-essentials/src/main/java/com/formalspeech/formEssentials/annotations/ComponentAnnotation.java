package com.formalspeech.formEssentials.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentAnnotation {
    String label();
    String identifier();
}
