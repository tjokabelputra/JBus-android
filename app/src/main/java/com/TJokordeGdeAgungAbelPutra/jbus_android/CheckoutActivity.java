    package com.TJokordeGdeAgungAbelPutra.jbus_android;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.widget.Button;
    import android.widget.ListView;
    import android.widget.Toast;

    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Invoice;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Payment;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.checkoutListAdapter;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class CheckoutActivity extends AppCompatActivity {
        private BaseApiService mApiService;
        private Context mContext;
        private List<Payment> payments = new ArrayList<>();
        private List<Payment> pendingPayments = new ArrayList<>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_checkout);

            mApiService = UtilsApi.getApiService();
            mContext = this;

            handlePaymentsList();

            Button backBtn = findViewById(R.id.back_btn);
            backBtn.setOnClickListener(view -> {
                finish();
            });
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.transaction_menu, menu);

            MenuItem hist_trans = menu.findItem(R.id.history_btn);
            hist_trans.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(mContext, TransactionHistoryActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
            return true;
        }

        private void handlePaymentsList(){
            mApiService.getUserPayment(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Payment>>() {
                @Override
                public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    payments = response.body();
                    for(Payment paymentCheck : payments){
                        if (paymentCheck.status.equals(Invoice.PaymentStatus.WAITING)){
                            pendingPayments.add(paymentCheck);
                        }
                    }
                    checkoutListAdapter checkoutListAdapter = new checkoutListAdapter(mContext,pendingPayments);
                    ListView checkoutList = findViewById(R.id.trans_list);
                    checkoutList.setAdapter(checkoutListAdapter);
                    checkoutListAdapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(Call<List<Payment>> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }