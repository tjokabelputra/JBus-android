package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

public class Station extends Serializable {
    public String stationName;
    public City city;
    public String address;

    @Override
    public String toString(){
        return stationName;
    }
}