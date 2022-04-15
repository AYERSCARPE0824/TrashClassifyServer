package com.car.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author jhyang
 * @create 2021-04-29 10:19
 */
public class Env extends Properties {
    public static final String JDBC_URL;
    public static final String JDBC_DRIVER;
    public static final String JDBC_USER;
    public static final String JDBC_PASSWORD;

    private static final String CONF_FILE="dataSource.properties";
    private static Env env;

    static {
        if (env == null)
            env = new Env();
        InputStream input = env.getClass().getClassLoader().getResourceAsStream(CONF_FILE);
        try {
            env.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JDBC_URL=env.getProperty("jdbc_url");
        JDBC_DRIVER=env.getProperty("jdbc_driver");
        JDBC_USER=env.getProperty("jdbc_user");
        JDBC_PASSWORD=env.getProperty("jdbc_password");
    }

    private Env(){
        String a = Env.JDBC_DRIVER;
    }
}
