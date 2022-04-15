package com.car.utils;

import java.sql.*;

/**
 * @author jhyang
 * @create 2021-04-29 10:20
 */
public class DataSourceManager {
    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(Env.JDBC_DRIVER);
            conn = DriverManager.getConnection(Env.JDBC_URL,Env.JDBC_USER,Env.JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(Connection conn){
        try {
            if(conn != null && conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Statement state){
        try {
            if(state != null && state.isClosed()){
                state.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet set){
        try {
            if(set != null && set.isClosed()){
                set.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
