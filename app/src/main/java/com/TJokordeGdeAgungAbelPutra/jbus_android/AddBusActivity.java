package com.TJokordeGdeAgungAbelPutra.jbus_android;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BusType;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Facility;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Station;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Button addButton;
    private EditText busName, capacity, price;
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            selectedBusType = busType[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            selectedDeptStationID = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            selectedArrStationID = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        ArrayAdapter<BusType> adBus = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        busTypeSpinner = findViewById(R.id.type_spinner);
        departureSpinner = findViewById(R.id.departure_spinner);
        arrivalSpinner = findViewById(R.id.arrival_spinner);

        busTypeSpinner.setAdapter(adBus);
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        handleStation();

        acCheckBox = findViewById(R.id.AC);
        wifiCheckBox = findViewById(R.id.WiFi);
        toiletCheckBox = findViewById(R.id.Toilet);
        lcdCheckBox = findViewById(R.id.LCDTV);
        coolboxCheckBox = findViewById(R.id.Coolbox);
        lunchCheckBox = findViewById(R.id.Lunch);
        baggageCheckBox = findViewById(R.id.LargeBaggage);
        electricCheckBox = findViewById(R.id.ElectricSocket);

        handleCheck();

        busName = findViewById(R.id.bus_name);
        capacity = findViewById(R.id.capacity);
        price = findViewById(R.id.price);
        addButton = findViewById(R.id.add_btn);

        addButton.setOnClickListener(v -> handleCreateBus());
    }
    protected void handleStation() {
        mApiService.getAllstation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application Error" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                stationList = response.body();
                ArrayAdapter<Station> deptBus = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, stationList);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);
                departureSpinner.setOnItemSelectedListener(deptOISL);

                ArrayAdapter<Station> arrBus = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, stationList);
                arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinner.setAdapter(arrBus);
                arrivalSpinner.setOnItemSelectedListener(arrOISL);
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void handleCheck() {
        selectedFacilities = new ArrayList<>();
        if (acCheckBox.isChecked()) {
            selectedFacilities.add(Facility.AC);
        }
        if (wifiCheckBox.isChecked()) {
            selectedFacilities.add(Facility.WIFI);
        }
        if (toiletCheckBox.isChecked()) {
            selectedFacilities.add(Facility.TOILET);
        }
        if (lcdCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LCD_TV);
        }
        if (coolboxCheckBox.isChecked()) {
            selectedFacilities.add(Facility.COOL_BOX);
        }
        if (lunchCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LUNCH);
        }
        if (baggageCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LARGE_BAGGAGE);
        }
        if (electricCheckBox.isChecked()) {
            selectedFacilities.add(Facility.ELECTRIC_SOCKET);
        }
    }
    protected void handleCreateBus() {
        String busNameS = busName.getText().toString();
        String capacityS = capacity.getText().toString();
        String priceS = price.getText().toString();
        handleCheck();
        if (busNameS.isEmpty() || capacityS.isEmpty() || priceS.isEmpty()) {
            Toast.makeText(mContext, "Mohon untuk mengisi semuanya", Toast.LENGTH_SHORT).show();
            return;
        }
        int capacity = Integer.parseInt(capacityS);
        int price = Integer.parseInt(priceS);
        mApiService.registerBus(LoginActivity.loggedAccount.id, busNameS, capacity, selectedFacilities, selectedBusType, price, selectedDeptStationID, selectedArrStationID).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                }
                BaseResponse<Bus> res = response.body();
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Bus berhasil dibuat", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}