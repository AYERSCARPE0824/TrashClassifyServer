package com.car.dao.imp;

import com.car.dao.ClassifyDao;
import com.car.utils.DataSourceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author jhyang
 * @create 2022-03-29 16:06
 */
public class ClassifyDaoImp implements ClassifyDao {

    private Connection conn;
    private PreparedStatement preparedStatement;
    private String sql;

    @Override
    public String checkClassify(String name) {
        String className = null;

        ResultSet resultSet = null;
        conn = DataSourceManager.getConnection();

        sql = "select * from classify where wname = ?";
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                className = resultSet.getString("wclassify");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceManager.close(resultSet);
            DataSourceManager.close(preparedStatement);
            DataSourceManager.close(conn);
        }

        return className;
    }
}
