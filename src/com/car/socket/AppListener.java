package com.car.socket;

import com.car.pojo.EnvironmentInfo;
import com.car.request.*;
import com.car.thread.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;



public class AppListener implements Runnable {
    private static final int PORT = 7777;  //监听所用的端口

    private ServerSocket serverSocket;//等待客户端

    private Socket socket;//套接字

    private ObjectInputStream objectInputStream; //输入流

    private ObjectOutputStream objectOutputStream; //输出流


    public void start(){
        this.getServerSocket();
        System.out.println("开始监听");
        //死循环使其不断监听
        while(true){
            this.run();
        }
    }

    private void getServerSocket() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = serverSocket.accept();
            Object object = receiveObject();
            handlerRequest(object, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerRequest(Object object, Socket socket) {
        if (object instanceof EnvironmentInfoRequest) {
            System.out.println("receive environmentInfo request   time:" + new Date());
            new EnvironmentInfoThread((EnvironmentInfoRequest) object,socket).run();
        } else if (object instanceof PicRecRequest) {
            System.out.println("receive picrec request   time:" + new Date());
            new PicRecThread((PicRecRequest)object,socket).run();
        } else if (object instanceof WordRecRequest) {
            System.out.println("receive wordrec request   time:" + new Date());
            new WordRecThread((WordRecRequest)object,socket).run();
        } else if (object instanceof StationsRequest) {
            System.out.println("receive stations request   time:" + new Date());
            new StationsThread((StationsRequest)object,socket).run();
        }
    }

    private Object receiveObject() {
        Object object = null;
        try {
            objectInputStream = new ObjectInputStream(
            new BufferedInputStream(socket.getInputStream()));
            object = objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return object;
    }
}
