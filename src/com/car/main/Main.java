package com.car.main;

import com.car.getdata.PackageComm;
import com.car.getdata.StaticPort;
import com.car.socket.AppListener;

/**
 * @author jhyang
 * @create 2021-04-29 10:25
 */
public class Main {
    public static void main(String[] args) {

//        StaticPort.packageComm = new PackageComm();
//        StaticPort.packageComm.start();
        new AppListener().start();

    }
}
