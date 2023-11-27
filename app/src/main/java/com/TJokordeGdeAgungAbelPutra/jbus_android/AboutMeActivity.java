package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView userInfo,emailInfo,balanceInfo;
    public  static TextView noRenter,renterReg,existRenter;
    private EditText topUp;
    private Button topUpBtn;
    public static Button manageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        userInfo = findViewById(R.id.username);
        emailInfo = findViewById(R.id.email);
        balanceInfo = findViewById(R.id.balance);
        topUp = findViewById(R.id.topup);
        topUpBtn = findViewById(R.id.topup_btn);

        if (LoginActivity.loggedAccount != null) {
            userInfo.setText(LoginActivity.loggedAccount.name);
            emailInfo.setText(LoginActivity.loggedAccount.email);
            balanceInfo.setText(String.valueOf(LoginActivity.loggedAccount.balance));
        }
        else {
            userInfo.setText("N/A");
            emailInfo.setText("N/A");
            balanceInfo.setText("N/A");
        }
        topUpBtn.setOnClickListener(v->handleTopUp());

        noRenter = findViewById(R.id.no_renter);
        renterReg = findViewById(R.id.no_renter_reg);
        existRenter = findViewById(R.id.renter);
        manageButton = findViewById(R.id.manage_button);

        if(LoginActivity.loggedAccount.company == null){
            noRenter.setVisibility(TextView.VISIBLE);
            renterReg.setVisibility(TextView.VISIBLE);
            existRenter.setVisibility(TextView.GONE);
            manageButton.setVisibility(TextView.GONE);
        }
        else {
            noRenter.setVisibility(TextView.GONE);
            renterReg.setVisibility(TextView.GONE);
            existRenter.setVisibility(TextView.VISIBLE);
            manageButton.setVisibility(TextView.VISIBLE);
        }
        renterReg.setOnClickListener(v->{moveActivity(mContext,RegisterRenterActivity.class);});
        manageButton.setOnClickListener(l->{moveActivity(mContext,ManageBusActivity.class);});
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    protected void handleTopUp() {
        String topUpS = topUp.getText().toString();
        if (topUpS.isEmpty()) {
            Toast.makeText(mContext, "Enter the Amount Please", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpA = Double.parseDouble(topUpS);
        mApiService.topUp(LoginActivity.loggedAccount.id,topUpA).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Top up Failed", Toast.LENGTH_SHORT).show();
                }
                double upBalance = response.body().payload;
                LoginActivity.loggedAccount.balance = upBalance;
                balanceInfo.setText(String.valueOf(upBalance));
                Toast.makeText(mContext, "Top Up Successful", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

