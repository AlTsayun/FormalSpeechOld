package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

import java.util.List;

@Data
public final class Person {
    final private String firstName;
    final private String lastName;
    final private List<String> phoneNumbers;
    @JacksonXmlElementWrapper(useWrapping = false)
    final private List<Address> address;

}
