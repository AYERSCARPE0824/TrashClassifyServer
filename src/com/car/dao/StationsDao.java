package com.car.dao;

import com.car.pojo.Station;

import java.util.List;

/**
 * @author jhyang
 * @create 2022-03-30 10:04
 */
public interface StationsDao {
    public List<Station> checkAround(int longitude, int latitude);
}
