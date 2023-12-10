        package com.TJokordeGdeAgungAbelPutra.jbus_android;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Payment;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.model.busSeatListChooseAdapter;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.model.seatSelectedAdapter;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
        import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

        import java.sql.Timestamp;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Locale;
        import java.util.Map;
        import java.util.TimeZone;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        public class BuyTicketActivity extends AppCompatActivity {
            private Button book;
            private BaseApiService mApiService;
            private Context mContext;
            private Map<String, Boolean> currentSeatMap = new HashMap<>();
            private String selectedSeatString;
            private Spinner scheduleSpinner;
            public static Schedule bookSchedule;
            private List<Schedule> scheduleList;
            public static List<String> selectedSeats = new ArrayList<>();

            AdapterView.OnItemSelectedListener scheduleOISL = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                    if (position >= 0 && position < scheduleList.size()) {
                        bookSchedule = scheduleList.get(position);
                        handleSeatMap();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_buy_ticket);

                mApiService = UtilsApi.getApiService();
                mContext = this;

                if (MainActivity.selectedBus != null && MainActivity.selectedBus.schedules != null) {
                    scheduleList = MainActivity.selectedBus.schedules;

                    ArrayAdapter<Schedule> viewSched = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, scheduleList);
                    viewSched.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

                    scheduleSpinner = findViewById(R.id.date_spinner);
                    scheduleSpinner.setAdapter(viewSched);
                    scheduleSpinner.setOnItemSelectedListener(scheduleOISL);
                }
                else {
                    Toast.makeText(mContext, "No schedules available", Toast.LENGTH_SHORT).show();
                }

                book = findViewById(R.id.book_btn);
                book.setOnClickListener(v->{
                    handleBooking();
                });
            }

            private void handleSeatMap() {
                if (bookSchedule != null) {
                    mApiService.busSeatInfo(MainActivity.selectedBus.id, bookSchedule.departureSchedule).enqueue(new Callback<BaseResponse<Map<String, Boolean>>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Map<String, Boolean>>> call, Response<BaseResponse<Map<String, Boolean>>> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            currentSeatMap = response.body().payload;
                            selectedSeats.clear();
                            displaySeatMap();
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Map<String, Boolean>>> call, Throwable t) {
                            Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(mContext, "No schedule selected", Toast.LENGTH_SHORT).show();
                }
            }

            private void displaySeatMap() {
                ListView listView = findViewById(R.id.seat_list);
                busSeatListChooseAdapter adapter = new busSeatListChooseAdapter(this, currentSeatMap);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object selectedItem = adapter.getItem(position);

                        if (selectedItem != null && selectedItem instanceof Map.Entry) {
                            Map.Entry<String, Boolean> selectedEntry = (Map.Entry<String, Boolean>) selectedItem;
                            String seatKey = selectedEntry.getKey();
                            Boolean isSeatAvailable = selectedEntry.getValue();

                            if (isSeatAvailable != null && isSeatAvailable) {
                                if(isSeatAvailable){
                                    selectedSeatString = seatKey;
                                    selectedSeats.add(selectedSeatString);
                                    displaySelectedSeat();
                                }
                            } else {
                                Toast.makeText(mContext, "Seat not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(mContext, "Invalid seat selection", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

            private void displaySelectedSeat(){
                ListView listView = findViewById(R.id.chose_seats);
                seatSelectedAdapter adapter = new seatSelectedAdapter(this,selectedSeats);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String revSeat = adapter.getItem(i);
                        if(selectedSeats != null){
                            selectedSeats.remove(revSeat);
                            displaySelectedSeat();
                        }
                        else{
                            Toast.makeText(mContext,"No Seat Selected",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void handleBooking() {
                if (selectedSeats.isEmpty()) {
                    Toast.makeText(mContext, "Mohon untuk memilih kursi", Toast.LENGTH_SHORT).show();
                    return;
                }
                mApiService.makeBooking(LoginActivity.loggedAccount.id,MainActivity.selectedBus.accountId,MainActivity.selectedBus.id,selectedSeats,String.valueOf(bookSchedule.departureSchedule)).enqueue(new Callback<BaseResponse<Payment>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                        if(!response.isSuccessful()){
                            Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(mContext,"Pemesanan Berhasil",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                        Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }