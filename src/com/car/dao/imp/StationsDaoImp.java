package com.car.dao.imp;

import com.car.dao.StationsDao;
import com.car.pojo.Station;
import com.car.utils.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jhyang
 * @create 2022-03-30 10:05
 */
public class StationsDaoImp implements StationsDao {
    private Connection conn;
    private PreparedStatement preparedStatement;
    private String sql;

    @Override
    public List<Station> checkAround(int longitude, int latitude) {
        ResultSet resultSet = null;
        conn = DataSourceManager.getConnection();

//        select * from stations where slongitude between 121300000 and 121400000 and slatitude between 28530000 and 29000000

        sql = "select * from stations where slongitude between ? and ? and slatitude between ? and ?";
        List<Station> stations = new ArrayList<>();
        Station station = null;

        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,longitude-1000000);
            preparedStatement.setInt(2,longitude+1000000);
            preparedStatement.setInt(3,latitude-1000000);
            preparedStatement.setInt(4,latitude+1000000);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                station = new Station();
                station.setSname(resultSet.getString("sname"));
                station.setSlongitude(resultSet.getInt("slongitude"));
                station.setSlatitude(resultSet.getInt("slatitude"));
                stations.add(station);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceManager.close(resultSet);
            DataSourceManager.close(preparedStatement);
            DataSourceManager.close(conn);
        }

        return stations;
    }
}
