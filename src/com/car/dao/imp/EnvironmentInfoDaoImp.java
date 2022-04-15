package com.car.dao.imp;

import com.car.dao.EnvironmentInfoDao;
import com.car.pojo.EnvironmentInfo;
import com.car.utils.DataSourceManager;

import java.sql.*;

/**
 * @author jhyang
 * @create 2021-05-21 20:18
 */
public class EnvironmentInfoDaoImp implements EnvironmentInfoDao {
    private Connection conn;
    private PreparedStatement preparedStatement;
    private String sql;

    @Override
    public EnvironmentInfo check() {
        boolean res = false;
        ResultSet resultSet = null;
        conn = DataSourceManager.getConnection();
        EnvironmentInfo environmentInfo = null;

        sql = "select TOP 1* from environment_info";
        try {
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                environmentInfo = new EnvironmentInfo();
                environmentInfo.setTemp(resultSet.getDouble("temp"));
                environmentInfo.setHumidity(resultSet.getDouble("humidity"));
                environmentInfo.setUltra_distance(resultSet.getInt("ultra_distance"));
            }
            System.out.println(environmentInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceManager.close(resultSet);
            DataSourceManager.close(preparedStatement);
            DataSourceManager.close(conn);
        }
        return environmentInfo;
    }

    @Override
    public boolean updateEnvironmentInfo(EnvironmentInfo environmentInfo) {
        int col = 0;
        boolean res = false;

        conn = DataSourceManager.getConnection();
        sql = "update environment_info set temp = ?,humidity = ?, ultra_distance = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1,environmentInfo.getTemp());
            preparedStatement.setDouble(2,environmentInfo.getHumidity());
            preparedStatement.setInt(3,environmentInfo.getUltra_distance());
            col = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceManager.close(preparedStatement);
            DataSourceManager.close(conn);
        }
        if (col == 1) {
            res = true;
        }
        return res;
    }
}
