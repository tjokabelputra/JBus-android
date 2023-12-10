package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import java.sql.Timestamp;
import java.util.List;

public class Payment extends Invoice{
    public int busId;
    public Timestamp departureDate;
    public List<String> busSeat;
}
