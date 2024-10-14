package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditScheduleActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    private String stringDate, stringTime, stringTimestamp = null;
    private TextView chosenTime;
    private Button selectTime, addSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        mContext = this;
        chosenTime = findViewById(R.id.show_time);
        selectTime = findViewById(R.id.time_pick);
        addSchedule = findViewById(R.id.add_schedule);
        mApiService = UtilsApi.getApiService();

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogDate();
            }
        });

        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSchedule();
            }
        });
    }

    private void openDialogDate(){
        DatePickerDialog dialogDate = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                stringDate = String.format("%04d-%02d-%02d", year, month + 1, date);;
                openDialogTime();
            }
        }, 2023, 11, 10);
        dialogDate.show();
    }

    private void openDialogTime(){
        TimePickerDialog dialogTime = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                stringTime = String.format("%02d:%02d:00", hour, minute);
                stringTimestamp = stringDate + " " + stringTime;
                chosenTime.setText(stringTimestamp);
            }
        }, 00, 00, true);
        dialogTime.show();
    }

    private void handleSchedule(){
        if(stringTimestamp == null || stringTimestamp.isEmpty()){
            Toast.makeText(mContext,"Please Enter The Date and Time",Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.addSchedule(ManageBusActivity.selectedBus.id,stringTimestamp).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext,"Application Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(mContext,"Jadwal Berhasil Ditambah",Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}