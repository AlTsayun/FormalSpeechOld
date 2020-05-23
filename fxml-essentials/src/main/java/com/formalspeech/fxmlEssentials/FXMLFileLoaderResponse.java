package com.formalspeech.fxmlEssentials;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FXMLFileLoaderResponse<P, C> {
    public final P loadedObject;
    public final C controller;
}
