package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.busSeatListChooseAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.busSeatsAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Facility;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBusActivity extends AppCompatActivity {
    private Map<String,Boolean> currentSeatMap = new HashMap<>();
    private BaseApiService mApiService;
    private Context mContext;
    private TextView busName, capInfo, priceInfo, busTypeInfo, originInfo, destInfo;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Spinner scheduleSpinner;
    private List<Schedule> scheduleList = ManageBusActivity.selectedBus.schedules;
    private Button showMap;
    public static Schedule selectedSchedule;
    AdapterView.OnItemSelectedListener scheduleOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            selectedSchedule = scheduleList.get(position);
            handleSeatMap();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.bus_name);
        capInfo = findViewById(R.id.cap_info);
        priceInfo = findViewById(R.id.price_info);
        busTypeInfo = findViewById(R.id.type_info);
        originInfo = findViewById(R.id.origin_info);
        destInfo = findViewById(R.id.dest_info);
        busName.setText(ManageBusActivity.selectedBus.name);
        capInfo.setText(String.valueOf(ManageBusActivity.selectedBus.capacity));
        int priceI = (int) ManageBusActivity.selectedBus.price.price;
        priceInfo.setText(String.valueOf(priceI));
        busTypeInfo.setText(String.valueOf(ManageBusActivity.selectedBus.busType));
        originInfo.setText(ManageBusActivity.selectedBus.departure.stationName);
        destInfo.setText(ManageBusActivity.selectedBus.arrival.stationName);

        acCheckBox = findViewById(R.id.AC);
        wifiCheckBox = findViewById(R.id.WiFi);
        toiletCheckBox = findViewById(R.id.Toilet);
        lcdCheckBox = findViewById(R.id.LCDTV);
        coolboxCheckBox = findViewById(R.id.Coolbox);
        lunchCheckBox = findViewById(R.id.Lunch);
        baggageCheckBox = findViewById(R.id.LargeBaggage);
        electricCheckBox = findViewById(R.id.ElectricSocket);

        disableCheckBox();

        ArrayAdapter<Schedule> viewSched = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, scheduleList);
        viewSched.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        scheduleSpinner = findViewById(R.id.date_spinner);
        scheduleSpinner.setAdapter(viewSched);
        scheduleSpinner.setOnItemSelectedListener(scheduleOISL);
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
        if (ManageBusActivity.selectedBus != null && ManageBusActivity.selectedBus.facility!= null) {
            List<Facility> currFacilities = new ArrayList<>(ManageBusActivity.selectedBus.facility);
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

    private void handleSeatMap() {
        if (ManageBusActivity.selectedBus == null) {
            Toast.makeText(mContext, "Invalid bus selection", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedSchedule == null) {
            Toast.makeText(mContext, "Invalid schedule selection", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.busSeatInfo(ManageBusActivity.selectedBus.id, selectedSchedule.departureSchedule).enqueue(new Callback<BaseResponse<Map<String, Boolean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<Map<String, Boolean>>> call, Response<BaseResponse<Map<String, Boolean>>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.body() != null && response.body().payload != null) {
                    currentSeatMap = response.body().payload;
                    displaySeatMap();
                } else {
                    Toast.makeText(mContext, "Empty response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Map<String, Boolean>>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySeatMap() {
        ListView listView = findViewById(R.id.seat_list);
        busSeatsAdapter adapter = new busSeatsAdapter(this, currentSeatMap);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}