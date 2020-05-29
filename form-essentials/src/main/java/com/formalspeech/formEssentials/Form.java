package com.formalspeech.formEssentials;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.formalspeech.formEssentials.annotations.ComponentAnnotation;
import com.formalspeech.formEssentials.components.Component;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class Form {

    final public static String FORM_FILE_EXTENSION = ".frm";
    final private String name;
    final private ArrayList<IdentifierAndValue> componentsIdentifiersAndValues;

    public Form(String name, List<Component> components) throws IOException {
        this.name = name;
        componentsIdentifiersAndValues = new ArrayList<>();
        for (Component component:
             components) {
            componentsIdentifiersAndValues.add(new IdentifierAndValue(component.getClass().getAnnotation(ComponentAnnotation.class).identifier(), component.getValueAsString()));
        }
    }

    public List<String> getComponentsIdentifiers() {
        ArrayList<String> identifiers = new ArrayList<>();
        for (IdentifierAndValue identifiersAndValue:
             componentsIdentifiersAndValues) {
            identifiers.add(identifiersAndValue.identifier);
        }
        return identifiers;
    }

    public Map<String, String> getComponentIdentifierToValue(){
        HashMap<String, String> identifierToValue = new HashMap<>();
        for (IdentifierAndValue identifierAndValue:
             componentsIdentifiersAndValues) {
            identifierToValue.put(identifierAndValue.identifier, identifierAndValue.value);
        }
        return  identifierToValue;
    }

}
