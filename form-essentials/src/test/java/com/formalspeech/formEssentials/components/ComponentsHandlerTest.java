package com.formalspeech.formEssentials.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentsHandlerTest {
    @Test
    void givenComponentClasses_whenLoading_thenCorrect(){
        assertDoesNotThrow(() -> {
            ComponentsHandler handler = new ComponentsHandler();
            System.out.println(handler.getAvailableComponents().toString());
        });
    }
}