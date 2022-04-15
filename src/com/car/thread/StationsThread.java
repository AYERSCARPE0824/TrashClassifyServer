package com.car.thread;

import com.car.dao.StationsDao;
import com.car.dao.imp.StationsDaoImp;
import com.car.pojo.Station;
import com.car.request.StationsRequest;
import com.car.result.StationsResult;

import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * @author jhyang
 * @create 2022-03-30 10:03
 */
public class StationsThread extends BaseThread {
    private StationsRequest request;
    private StationsDao stationsDao;

    public StationsThread(StationsRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;
        this.stationsDao = new StationsDaoImp();
    }

    @Override
    public void run() {
        int longitude = (int)request.getLongitude()*1000000;
        int latitude = (int)request.getLatitude()*1000000;
        List<Station> stations = stationsDao.checkAround(longitude, latitude);
        int num = stations.size();

        StationsResult stationsResult = new StationsResult();
        stationsResult.setNum(num);
        stationsResult.setStations(stations);
        sendObject(stationsResult);

        System.out.println("send stations result   time" + new Date());

    }
}
