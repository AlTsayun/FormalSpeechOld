package com.formalspeech.networkService.serverSide;

import com.formalspeech.formEssentials.Form;

public interface ServerConnectionHandler {
    void disable();
    void sendFormToUser(String login, String serializedForm);

}
