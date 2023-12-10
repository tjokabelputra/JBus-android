package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import java.sql.Timestamp;

public class Invoice extends Serializable{
    public Timestamp time;
    public int buyerId;
    public int renterId;
    public BusRating rating = BusRating.NONE;
    public PaymentStatus status = PaymentStatus.WAITING;

    public enum PaymentStatus{
        FAILED,
        WAITING,
        SUCCESS;
    }

    public enum BusRating{
        NONE,
        NEUTRAL,
        GOOD,
        BAD;
    }
}
