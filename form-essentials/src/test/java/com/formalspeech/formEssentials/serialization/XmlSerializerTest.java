package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.formalspeech.formEssentials.Form;
import com.formalspeech.formEssentials.IdentifierAndValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
class XmlSerializerTest {

    @Test
    void givenPOJO_whenSerializeAndDeserialize_thenCorrect()throws IOException {

        StringSerializer<Form> serializer = new XmlSerializer<>(Form.class);
        Form value = new Form("form", new ArrayList<IdentifierAndValue>(List.of(new IdentifierAndValue("loginComponent", "infotext"))));
        String data = serializer.writeAsString(value);
        log.info(data);
        value = serializer.readValueFromString(data);

    }
}