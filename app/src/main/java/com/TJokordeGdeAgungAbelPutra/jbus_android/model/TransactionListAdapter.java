    package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.TJokordeGdeAgungAbelPutra.jbus_android.LoginActivity;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

    import java.sql.Timestamp;
    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class TransactionListAdapter extends ArrayAdapter<Payment> {
        private BaseApiService mApiService;
        private Context mContext;
        private Invoice.BusRating rating = Invoice.BusRating.NONE;
        private RecyclerView orderedSeats;
        public TransactionListAdapter(Context ctx, List<Payment> payments){
            super(ctx,0,payments);
            mApiService = UtilsApi.getApiService();
            mContext = ctx;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View currentItemView = convertView;

            if(currentItemView == null){
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_history_layout,parent,false);
            }

            Payment currentPaymentPosition = getItem(position);

            TextView deptTime = currentItemView.findViewById(R.id.time_info);
            deptTime.setText(String.valueOf(currentPaymentPosition.time));

            TextView buyerName = currentItemView.findViewById(R.id.buyer_name);
            buyerName.setText(LoginActivity.loggedAccount.name);

            displayBusInfo(currentPaymentPosition.busId,currentPaymentPosition.departureDate,currentPaymentPosition.busSeat.size(),currentItemView);

            orderedSeats = currentItemView.findViewById(R.id.orderedSeats);
            orderedSeats.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            orderedSeatsAdapter adapter = new orderedSeatsAdapter(mContext, new ArrayList<>());
            orderedSeats.setAdapter(adapter);
            handleSeatsOrdered(currentPaymentPosition.id, adapter);

            TextView badBtn = currentItemView.findViewById(R.id.bad_btn);
            TextView neutralBtn = currentItemView.findViewById(R.id.neutral_btn);
            TextView goodBtn = currentItemView.findViewById(R.id.good_btn);

            LinearLayout badBorder = currentItemView.findViewById(R.id.bad_border);
            LinearLayout neutralBorder = currentItemView.findViewById(R.id.neutral_border);
            LinearLayout goodBorder = currentItemView.findViewById(R.id.good_border);

            Button rateBtn = currentItemView.findViewById(R.id.rate_btn);

            if(currentPaymentPosition.rating.equals(Invoice.BusRating.NONE)){
                badBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badBorder.setBackgroundResource(R.drawable.border_press);
                        neutralBorder.setBackgroundResource(R.drawable.border);
                        goodBorder.setBackgroundResource(R.drawable.border);
                        rating = Invoice.BusRating.BAD;
                    }
                });

                neutralBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badBorder.setBackgroundResource(R.drawable.border);
                        neutralBorder.setBackgroundResource(R.drawable.border_press);
                        goodBorder.setBackgroundResource(R.drawable.border);
                        rating = Invoice.BusRating.NEUTRAL;
                    }
                });

                goodBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badBorder.setBackgroundResource(R.drawable.border);
                        neutralBorder.setBackgroundResource(R.drawable.border);
                        goodBorder.setBackgroundResource(R.drawable.border_press);
                        rating = Invoice.BusRating.GOOD;
                    }
                });

                rateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleRating(currentPaymentPosition.id);
                        rateBtn.setVisibility(View.GONE);
                    }
                });
            }
            else{
                if(currentPaymentPosition.rating.equals(Invoice.BusRating.BAD)){
                    badBorder.setBackgroundResource(R.drawable.border_press);
                }
                else if(currentPaymentPosition.rating.equals(Invoice.BusRating.NEUTRAL)){
                    neutralBorder.setBackgroundResource(R.drawable.border_press);
                }
                else if(currentPaymentPosition.rating.equals(Invoice.BusRating.GOOD)){
                    goodBorder.setBackgroundResource(R.drawable.border_press);
                }
                rateBtn.setVisibility(View.GONE);
            }
            return currentItemView;
        }

        private void displayBusInfo(int busId, Timestamp departureDate, int size, View itemView){
            mApiService.getBusInfo(busId).enqueue(new Callback<BaseResponse<Bus>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bus busInfo = response.body().payload;

                    TextView busName = itemView.findViewById(R.id.bus_name);
                    busName.setText(busInfo.name);
                    TextView deptInfo = itemView.findViewById(R.id.dept_info);
                    deptInfo.setText(busInfo.departure.stationName);
                    TextView arrInfo = itemView.findViewById(R.id.arr_info);
                    arrInfo.setText(busInfo.arrival.stationName);
                    TextView timeInfo = itemView.findViewById(R.id.time_info);
                    for(Schedule schedules : busInfo.schedules) {
                        if (schedules.departureSchedule.equals(String.valueOf(departureDate))) {
                            timeInfo.setText(String.valueOf(schedules.departureSchedule));
                        }
                    }
                    int priceTotal = size * (int) busInfo.price.price;
                    TextView priceInfo = itemView.findViewById(R.id.price_info);
                    priceInfo.setText(String.valueOf(priceTotal));
                }
                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleRating(int id){
            mApiService.setRating(id, rating).enqueue(new Callback<BaseResponse<Payment>>() {
                @Override
                public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(mContext,"Rating Berhasil",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleSeatsOrdered(int id, orderedSeatsAdapter adapter) {
            mApiService.getSeats(id).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<String> seats = response.body();
                    System.out.println(seats);
                    if (seats != null && !seats.isEmpty()) {
                        handleDisplayOrderedSeats(adapter, seats);
                    } else {
                        Toast.makeText(mContext, "No seat information available", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleDisplayOrderedSeats(orderedSeatsAdapter adapter, List<String> seats) {
            if (seats != null && !seats.isEmpty()) {
                adapter.setData(seats);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "No seat information available", Toast.LENGTH_SHORT).show();
            }
        }

    }
