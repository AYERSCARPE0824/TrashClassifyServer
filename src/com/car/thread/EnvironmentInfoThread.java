package com.car.thread;

import com.car.dao.EnvironmentInfoDao;
import com.car.dao.imp.EnvironmentInfoDaoImp;
import com.car.request.EnvironmentInfoRequest;
import com.car.result.EnvironmentInfoResult;

import java.net.Socket;
import java.util.Date;

public class EnvironmentInfoThread extends BaseThread{

    private EnvironmentInfoRequest request;
    private EnvironmentInfoDao environmentInfoDao;

    public EnvironmentInfoThread(EnvironmentInfoRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;

        environmentInfoDao = new EnvironmentInfoDaoImp();
    }

    @Override
    public void run() {
        boolean res;
        EnvironmentInfoResult result = new EnvironmentInfoResult();
        result.setEnvironmentInfo(environmentInfoDao.check());
        if (result.getEnvironmentInfo() != null) {
            res = true;
        } else {
            res = false;
        }
        result.setRes(res);

        sendObject(result);

        System.out.println("send EnvironmentInfo result   time" + new Date());
    }
}
