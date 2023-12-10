package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import java.sql.Timestamp;
import java.util.Map;

public class Schedule {
    public Timestamp departureSchedule;
    public Map<String,Boolean> seatAvailability;

    public String toString(){
        return String.valueOf(departureSchedule);
    }
    public Map<String, Boolean> getSeatAvailability() {
        return seatAvailability;
    }
}
