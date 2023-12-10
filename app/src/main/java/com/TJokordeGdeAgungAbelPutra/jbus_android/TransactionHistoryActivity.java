package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Invoice;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Payment;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.TransactionListAdapter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionHistoryActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private List<Payment> histPayments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        handleCompletedTransaction();

        Button backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleCompletedTransaction(){
        mApiService.getUserPayment(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                for(Payment paymentList : response.body()){
                    if(paymentList.status == Invoice.PaymentStatus.SUCCESS){
                        histPayments.add(paymentList);
                    }
                }
                TransactionListAdapter transactionListAdapter = new TransactionListAdapter(mContext,histPayments);
                ListView listView = findViewById(R.id.trans_hist);
                listView.setAdapter(transactionListAdapter);
                transactionListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {

            }
        });
    }
}