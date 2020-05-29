package com.formalspeech.networkService.conntection;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SocketConnection implements Connection {
    private BufferedReader in;
    private Thread thread;
    private BufferedWriter out;

    @SneakyThrows
    public SocketConnection(Socket socket, ConnectionListener listener) {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        thread = new Thread(new Runnable() {

            @SneakyThrows
            @Override
            public void run() {

                try {
                    String data;
                    listener.onConnected(SocketConnection.this);
                    while (!thread.isInterrupted() && (data = in.readLine()) != null) {
                        listener.onReceive(SocketConnection.this, data);
                    }
                } finally {
                    listener.onDisconnected(SocketConnection.this);
                }
            }
        });
        thread.start();
    }

    @Override
    @SneakyThrows
    public synchronized void send(String data) {
        out.write(data);
        out.newLine();
        out.flush();
    }

    @Override
    @SneakyThrows
    public void close() {
        thread.interrupt();
        in.close();
        out.close();
    }

}
