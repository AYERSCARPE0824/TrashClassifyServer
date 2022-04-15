package com.car.dao.imp;

import com.car.dao.LableDao;
import com.car.utils.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jhyang
 * @create 2022-03-29 15:53
 */
public class LableDaoImp implements LableDao {

    private Connection conn;
    private PreparedStatement preparedStatement;
    private String sql;

    @Override
    public String checkNameFormLable(String lable) {
        String name = null;

        ResultSet resultSet = null;
        conn = DataSourceManager.getConnection();

        sql = "select * from lable_to_name where lable = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,lable);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceManager.close(resultSet);
            DataSourceManager.close(preparedStatement);
            DataSourceManager.close(conn);
        }

        return name;
    }
}
