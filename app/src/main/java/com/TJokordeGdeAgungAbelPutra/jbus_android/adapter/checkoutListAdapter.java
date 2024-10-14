    package com.TJokordeGdeAgungAbelPutra.jbus_android.adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.TJokordeGdeAgungAbelPutra.jbus_android.UI.LoginActivity;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Payment;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

    import java.sql.Timestamp;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class checkoutListAdapter extends ArrayAdapter<Payment> {
        private BaseApiService mApiService;
        private Context mContext;
        private Bus bookedBus;
        private Payment handlePayment;
        public int priceTotal;

        private RecyclerView orderedSeats;
        public checkoutListAdapter(Context context, List<Payment> payments){
            super(context,0,payments);
            mApiService = UtilsApi.getApiService();
            mContext = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View currentItemView = convertView;

            if(currentItemView == null){
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.checkout_list_layout,parent,false);
            }

            Payment currentPaymentPosition = getItem(position);

            TextView deptTime = currentItemView.findViewById(R.id.time_info);
            deptTime.setText(String.valueOf(currentPaymentPosition.time));

            TextView buyerName = currentItemView.findViewById(R.id.buyer_name);
            buyerName.setText(LoginActivity.loggedAccount.name);

            orderedSeats = currentItemView.findViewById(R.id.orderedSeats);
            orderedSeats.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            orderedSeatsAdapter adapter = new orderedSeatsAdapter(mContext, new ArrayList<>());
            orderedSeats.setAdapter(adapter);
            handleSeatsOrdered(currentPaymentPosition.id, adapter);

            displayBusInfo(currentPaymentPosition.busId,currentPaymentPosition.departureDate,currentPaymentPosition.busSeat.size(),currentItemView);

            TextView cancelOrder = currentItemView.findViewById(R.id.cancel_btn);
            TextView accOrder = currentItemView.findViewById(R.id.acc_btn);

            cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleCancelOrder(currentPaymentPosition.id);
                }
            });

            accOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handlePayment = currentPaymentPosition;
                    getBusTransaction();
                }
            });
            return currentItemView;
        }

        private void displayBusInfo(int busId, Timestamp departureDate,int size,View itemView){
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
                    priceTotal = size * (int) busInfo.price.price;
                    TextView priceInfo = itemView.findViewById(R.id.price_info);
                    priceInfo.setText(String.valueOf(priceTotal));
                }
                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleCancelOrder(int id){
            mApiService.cancel(id).enqueue(new Callback<BaseResponse<Payment>>() {
                @Override
                public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(mContext,"Booking Canceled",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void handleAcceptOrder(int id){
            mApiService.accept(id).enqueue(new Callback<BaseResponse<Payment>>() {
                @Override
                public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(mContext,"Payment Successfull",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                    Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getBusTransaction(){
            mApiService.getBusInfo(handlePayment.busId).enqueue(new Callback<BaseResponse<Bus>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bookedBus = response.body().payload;
                    checkSeatStillAvailable();
                }

                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void checkSeatStillAvailable(){
            mApiService.busSeatInfo(bookedBus.id, handlePayment.departureDate).enqueue(new Callback<BaseResponse<Map<String, Boolean>>>() {
                @Override
                public void onResponse(Call<BaseResponse<Map<String, Boolean>>> call, Response<BaseResponse<Map<String, Boolean>>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String,Boolean> seatCheck = response.body().payload;
                    for(String seatsOrdered : handlePayment.busSeat){
                        boolean notBooked = seatCheck.get(seatsOrdered);
                        if(notBooked){
                            continue;
                        }
                        else{
                            Toast.makeText(mContext,"Seat: " + seatsOrdered + " telah dibeli",Toast.LENGTH_SHORT).show();
                            handleCancelOrder(handlePayment.id);
                        }
                    }
                    handleBalance();
                }

                @Override
                public void onFailure(Call<BaseResponse<Map<String, Boolean>>> call, Throwable t) {
                    Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void handleBalance() {
            if (LoginActivity.loggedAccount.balance < priceTotal) {
                Toast.makeText(mContext, "Saldo Tidak Cukup", Toast.LENGTH_SHORT).show();
            }
            else{
                mApiService.getAccountbyid(handlePayment.renterId).enqueue(new Callback<BaseResponse<Account>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Account renterAcc = response.body().payload;
                        int renterBalance = (int) renterAcc.balance;
                        int buyerBalance = (int) LoginActivity.loggedAccount.balance;
                        renterBalance += priceTotal;
                        buyerBalance -= priceTotal;
                        handleUpdateBalance(renterAcc.id, renterBalance);
                        handleUpdateBalance(LoginActivity.loggedAccount.id, buyerBalance);
                        handleSeatMapping();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                        Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        private void handleUpdateBalance(int id, int balance){
            mApiService.updateBalance(id,balance).enqueue(new Callback<BaseResponse<Account>>() {
                @Override
                public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                @Override
                public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void handleSeatMapping(){
            mApiService.setSeatMapping(bookedBus.id,handlePayment.departureDate,handlePayment.busSeat).enqueue(new Callback<BaseResponse<Bus>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    handleAcceptOrder(handlePayment.id);
                }

                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
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
