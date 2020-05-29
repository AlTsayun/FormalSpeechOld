package com.formalspeech.networkService.conntection;

public interface Connection {
    public void send(String data);
    public void close();
}
