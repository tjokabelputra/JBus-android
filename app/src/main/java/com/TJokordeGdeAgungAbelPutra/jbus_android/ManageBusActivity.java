package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.busListAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    private static final int ADD_BUS_REQUEST_CODE = 1;
    private BaseApiService mApiService;
    private Context mContext;
    private MenuItem addBus;
    private List<Bus> busList = new ArrayList<>();
    public static Bus selectedBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        handleBusList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_bus_menu, menu);
        addBus = menu.findItem(R.id.add_bus);
        addBus.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ManageBusActivity.this, AddBusActivity.class);
                startActivityForResult(intent, ADD_BUS_REQUEST_CODE);
                return true;
            }
        });
        return true;
    }

    private void handleBusList() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                }
                busList.clear();
                busList.addAll(response.body());
                displayListView();
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayListView() {
        ListView busListView = findViewById(R.id.listView);
        busListAdapter busListAdapter = new busListAdapter(mContext, busList);
        busListView.setAdapter(busListAdapter);
        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedBus = busList.get(position);
                if (selectedBus.schedules != null && !selectedBus.schedules.isEmpty()) {
                    Intent intent = new Intent(mContext, ViewBusActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Mohon is jadwal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_BUS_REQUEST_CODE && resultCode == RESULT_OK) {
            handleBusList();
        }
    }
}