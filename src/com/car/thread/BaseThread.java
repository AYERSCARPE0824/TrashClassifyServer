package com.car.thread;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author jhyang
 * @create 2021-04-29 11:33
 */
public class BaseThread implements Runnable {
    protected Socket socket;
    protected ObjectOutputStream objectOutputStream;
    @Override
    public void run() {

    }
    public void sendObject(Object obj){
        try {
            objectOutputStream = new ObjectOutputStream(socket
                    .getOutputStream());
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
