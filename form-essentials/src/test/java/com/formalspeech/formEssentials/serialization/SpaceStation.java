package com.formalspeech.formEssentials.serialization;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.formalspeech.formEssentials.IdentifierAndValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class SpaceStation {
    final private String string;
//    @JacksonXmlElementWrapper(useWrapping = false)
    final private List<SpaceShip> spaceShips;
}
