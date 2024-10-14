package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
import com.TJokordeGdeAgungAbelPutra.jbus_android.adapter.BusArrayAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Station;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Station> stationList;
    private Spinner departureSpinner, arrivalSpinner;
    private BaseApiService mApiService;
    private Context mContext;
    private MenuItem accountInfo = null;
    private MenuItem logOutBtn = null;
    private MenuItem checkOutBtn = null;
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 5;
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    public static Bus selectedBus;
    private AlertDialog alertDialog;
    private int selectedDeptStationID, selectedArrStationID;
    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            selectedDeptStationID = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
            selectedArrStationID = i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.listView);

        getAllBusData();

        prevButton.setOnClickListener(v->{currentPage = currentPage != 0? currentPage - 1 : 0; goToPage(currentPage);});

        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });

        TextView filter = findViewById(R.id.filter);
        TextView clearFilter = findViewById(R.id.clear_filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterBus();
            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFilter();
            }
        });
    }

    private void getAllBusData() {
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if(!response.isSuccessful()){

                }
                listBus.clear();
                listBus.addAll(response.body());
                listSize = listBus.size();
                noOfPages = (int) Math.ceil((double) listSize / pageSize);
                paginationFooter();
                goToPage(currentPage);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        accountInfo = menu.findItem(R.id.profile_button);
        logOutBtn = menu.findItem(R.id.log_out_button);
        checkOutBtn = menu.findItem(R.id.payment_button);
        accountInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
            }
        });
        logOutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                LoginActivity.loggedAccount = null;
                startActivity(intent);
                return true;
            }
        });
        checkOutBtn.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this,CheckoutActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }
    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity = Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, 100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }
    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.darker_gray));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        BusArrayAdapter busArrayAdapter = new BusArrayAdapter(this,new ArrayList<>(paginatedList));
        busListView.setAdapter(busArrayAdapter);

        busListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBus = listBus.get(i);
                if(selectedBus.schedules != null) {
                    Intent intent = new Intent(mContext, busDetailActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(mContext,"Bus ini tidak memiliki jadwal",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFilterBus(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View filterPopUp = LayoutInflater.from(mContext).inflate(R.layout.filter_layout, null);
        builder.setView(filterPopUp);

        alertDialog = builder.create();
        departureSpinner = filterPopUp.findViewById(R.id.departure_spinner);
        arrivalSpinner = filterPopUp.findViewById(R.id.arrival_spinner);

        TextView cancelFilter = filterPopUp.findViewById(R.id.cancel_filter);
        TextView filter = filterPopUp.findViewById(R.id.filter);

        handleStation();
        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBus();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
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

    protected void filterBus(){
        mApiService.filter(selectedDeptStationID,selectedArrStationID).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application Error" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listBus = response.body();
                viewPaginatedList(listBus,currentPage);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void clearFilter(){
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Application Error" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                listBus = response.body();
                viewPaginatedList(listBus,currentPage);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}