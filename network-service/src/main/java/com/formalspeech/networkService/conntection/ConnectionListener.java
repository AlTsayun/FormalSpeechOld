package com.formalspeech.networkService.conntection;

public interface ConnectionListener {
    public void onReceive(Connection connection, String data);
    public void onConnected(Connection connection);
    public void onDisconnected(Connection connection);

}
