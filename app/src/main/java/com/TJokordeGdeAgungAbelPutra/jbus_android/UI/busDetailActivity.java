package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Facility;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;
import com.TJokordeGdeAgungAbelPutra.jbus_android.adapter.scheduleListAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class busDetailActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName, capInfo, priceInfo, busTypeInfo, originInfo, destInfo;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Button nextBtn, prevBtn;
    private ListView scheduleList;
    public List<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.bus_name);
        capInfo = findViewById(R.id.cap_info);
        priceInfo = findViewById(R.id.price_info);
        busTypeInfo = findViewById(R.id.type_info);
        originInfo = findViewById(R.id.origin_info);
        destInfo = findViewById(R.id.dest_info);
        busName.setText(MainActivity.selectedBus.name);
        capInfo.setText(String.valueOf(MainActivity.selectedBus.capacity));
        int priceI = (int) MainActivity.selectedBus.price.price;
        priceInfo.setText(String.valueOf(priceI));
        busTypeInfo.setText(String.valueOf(MainActivity.selectedBus.busType));
        originInfo.setText(MainActivity.selectedBus.departure.stationName);
        destInfo.setText(MainActivity.selectedBus.arrival.stationName);

        acCheckBox = findViewById(R.id.AC);
        wifiCheckBox = findViewById(R.id.WiFi);
        toiletCheckBox = findViewById(R.id.Toilet);
        lcdCheckBox = findViewById(R.id.LCDTV);
        coolboxCheckBox = findViewById(R.id.Coolbox);
        lunchCheckBox = findViewById(R.id.Lunch);
        baggageCheckBox = findViewById(R.id.LargeBaggage);
        electricCheckBox = findViewById(R.id.ElectricSocket);

        disableCheckBox();

        handleSchedules();
        scheduleList = findViewById(R.id.schedule_list);

        nextBtn = findViewById(R.id.continue_btn);
        prevBtn = findViewById(R.id.back_btn);

        nextBtn.setOnClickListener(v->{moveActivity(this,BuyTicketActivity.class);});
        prevBtn.setOnClickListener(v->{moveActivity(this, MainActivity.class);});
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    private void disableCheckBox(){
        acCheckBox.setClickable(false);
        wifiCheckBox.setClickable(false);
        toiletCheckBox.setClickable(false);
        lcdCheckBox.setClickable(false);
        coolboxCheckBox.setClickable(false);
        lunchCheckBox.setClickable(false);
        baggageCheckBox.setClickable(false);
        electricCheckBox.setClickable(false);
        showFacilities();
    }

    private void showFacilities(){
        if (MainActivity.selectedBus != null && MainActivity.selectedBus.facility!= null) {
            List<Facility> currFacilities = new ArrayList<>(MainActivity.selectedBus.facility);
            acCheckBox.setChecked(currFacilities.contains(Facility.AC));
            wifiCheckBox.setChecked(currFacilities.contains(Facility.WIFI));
            toiletCheckBox.setChecked(currFacilities.contains(Facility.TOILET));
            lcdCheckBox.setChecked(currFacilities.contains(Facility.LCD_TV));
            coolboxCheckBox.setChecked(currFacilities.contains(Facility.COOL_BOX));
            lunchCheckBox.setChecked(currFacilities.contains(Facility.LUNCH));
            baggageCheckBox.setChecked(currFacilities.contains(Facility.LARGE_BAGGAGE));
            electricCheckBox.setChecked(currFacilities.contains(Facility.ELECTRIC_SOCKET));
        }
    }

    private void handleSchedules() {
        mApiService.getAllSchedule(MainActivity.selectedBus.id).enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                schedules = response.body();
                scheduleListAdapter scheduleListAdapter = new scheduleListAdapter(mContext, schedules);
                scheduleList.setAdapter(scheduleListAdapter);
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}