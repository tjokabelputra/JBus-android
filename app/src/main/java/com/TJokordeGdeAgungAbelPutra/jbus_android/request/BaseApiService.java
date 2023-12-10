package com.TJokordeGdeAgungAbelPutra.jbus_android.request;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BusType;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Facility;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Invoice;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Payment;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Renter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Station;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @GET("station/getAll")
    Call<List<Station>> getAllstation();

    @GET("account/getAccount")
    Call<BaseResponse<Account>> getAccountbyid(@Query("accountId") int accountId);

    @POST("account/register")
    Call<BaseResponse<Account>> register(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("account/login")
    Call<BaseResponse<Account>> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("account/{id}/registerRenter")
    Call<BaseResponse<Renter>> registerRenter(
            @Path("id") int id,
            @Query("companyName") String companyName,
            @Query("address") String address,
            @Query("phoneNumber") String phoneNumber
    );

    @POST("account/{id}/topUp")
    Call<BaseResponse<Double>> topUp(
            @Path("id") int id,
            @Query("amount") double amount
    );

    @GET("account/findEmail")
    Call<BaseResponse<Account>> accByEmail(
            @Query("email") String email
    );

    @GET("account/passExist")
    Call<BaseResponse<Boolean>> passExist(
            @Query("email") String email,
            @Query("password") String password
    );

    @POST("account/resetPassword")
    Call<BaseResponse<Account>> resetPassword(
            @Query("email") String email,
            @Query("newPass") String newPass
    );

    @POST("account/updateBalance")
    Call<BaseResponse<Account>> updateBalance(
            @Query("accountId") int accountId,
            @Query("balance") int balance
    );

    @GET("bus/getMyBus")
    Call<List<Bus>> getMyBus(
            @Query("accountId") int accountId
    );

    @GET("bus/getAllBus")
    Call<List<Bus>> getAllBus();

    @POST("bus/create")
    Call<BaseResponse<Bus>> registerBus(
            @Query("accountId") int id,
            @Query("name") String name,
            @Query("capacity") int capacity,
            @Query("facility") List<Facility> facility,
            @Query("busType") BusType busType,
            @Query("price") int price,
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    @POST("bus/addSchedule")
    Call<BaseResponse<Bus>> addSchedule(
            @Query("busId") int busId,
            @Query("time") String time
    );

    @POST("bus/setSeatMapping")
    Call<BaseResponse<Bus>> setSeatMapping(
            @Query("busId") int busId,
            @Query("time") Timestamp time,
            @Query("busSeats") List<String> busSeats
    );

    @DELETE("bus/removeBus")
    Call<BaseResponse<Bus>> deleteBus(
            @Query("busId") int busId
    );

    @GET("bus/busSeatInfo")
    Call<BaseResponse<Map<String, Boolean>>> busSeatInfo(
            @Query("busId") int busId,
            @Query("time") Timestamp time
    );

    @GET("bus/getAllSchedule")
    Call<List<Schedule>> getAllSchedule(
            @Query("busId") int busId
    );

    @GET("bus/getBusInfo")
    Call<BaseResponse<Bus>> getBusInfo(@Query("busId") int busId);

    @GET("bus/filter")
    Call<List<Bus>> filter(
            @Query("stationDepartureId") int stationDepartureId,
            @Query("stationArrivalId") int stationArrivalId
    );

    @POST("payment/makeBooking")
    Call<BaseResponse<Payment>> makeBooking(
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("busId") int busId,
            @Query("busSeats") List<String> busSeats,
            @Query("departureDate") String departureDate
    );

    @POST("payment/setRating")
    Call<BaseResponse<Payment>> setRating(
            @Query("id") int id,
            @Query("rating")Invoice.BusRating rating
    );

    @POST("payment/{id}/accept")
    Call<BaseResponse<Payment>> accept(@Path("id") int id);

    @POST("payment/{id}/cancel")
    Call<BaseResponse<Payment>> cancel(@Path("id") int id);

    @GET("payment/getMyPayment")
    Call<List<Payment>> getUserPayment(
            @Query("accountId") int accountId
    );

    @GET("payment/getSeats")
    Call<List<String>> getSeats(
            @Query("paymentId") int paymentId
    );
}