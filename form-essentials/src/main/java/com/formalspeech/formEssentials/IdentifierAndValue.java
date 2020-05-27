package com.formalspeech.formEssentials;

import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class IdentifierAndValue implements Serializable {
    public final String identifier;
    public final String value;

}
