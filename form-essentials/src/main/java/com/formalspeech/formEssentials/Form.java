package com.formalspeech.formEssentials;

import java.io.Serializable;
import java.util.ArrayList;

public class Form implements Serializable {
    static public final String FORM_FILE_EXTENSION = ".frm";
    private final String name;
    private final ArrayList<IdentifierAndValue> componentsIdentifiersAndValues;

    public Form(String name, ArrayList<IdentifierAndValue> componentsIdentifiersAndValues) {
        this.name = name;
        this.componentsIdentifiersAndValues = componentsIdentifiersAndValues;
    }


    public ArrayList<IdentifierAndValue> getComponentsIdentifiersAndValues() {
        return componentsIdentifiersAndValues;
    }

    public ArrayList<String> getComponentsIdentifiers() {
        ArrayList<String> identifiers = new ArrayList<>();
        for (IdentifierAndValue identifiersAndValue:
             componentsIdentifiersAndValues) {
            identifiers.add(identifiersAndValue.identifier);
        }
        return identifiers;
    }


    public String getName() {
        return name;
    }
}
