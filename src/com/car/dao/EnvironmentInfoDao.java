package com.car.dao;

import com.car.pojo.EnvironmentInfo;

/**
 * @author jhyang
 * @create 2021-05-21 20:17
 */
public interface EnvironmentInfoDao {
    public  EnvironmentInfo check();

    public boolean updateEnvironmentInfo(EnvironmentInfo environmentInfo);
}
