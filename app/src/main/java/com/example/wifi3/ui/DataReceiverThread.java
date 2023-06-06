package com.example.wifi3.ui;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class DataReceiverThread extends Thread {

    private Socket socket;
    private DataReceivedListener listener;

    public DataReceiverThread(Socket socket, DataReceivedListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String receivedData;
            while (!isInterrupted() && (receivedData = reader.readLine()) != null) {
                // Notify the listener with the received data
                Log.d("message", receivedData);
                listener.onDataReceived(receivedData);
            }

            socket.close();
        } catch (IOException e) {
            listener.onError();
            e.printStackTrace();
        }
    }

    public void cancel() {
        interrupt();
    }

    public interface DataReceivedListener {
        void onDataReceived(String data);
        void onError();
    }
}
