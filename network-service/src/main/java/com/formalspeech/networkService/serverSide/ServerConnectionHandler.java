package com.formalspeech.networkService.serverSide;

import com.formalspeech.formEssentials.Form;

import java.util.List;

public interface ServerConnectionHandler {
    void disable();
    boolean enable();
    void sendFormToUser(String login, String serializedForm);
    List<String> getSerializedFormsForUser(String login);
}
