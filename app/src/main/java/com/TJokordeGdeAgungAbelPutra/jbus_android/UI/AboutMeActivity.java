package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private LinearLayout accountInfoPress, companyInfoPress, topUpPress;
    private LinearLayout accInfo, compInfo, topUpMenu, noComp;
    private TextView userInfo,emailInfo,balanceInfo;
    private TextView compName, compAddress, compPhone;
    private TextView topUpBalance;
    public  static TextView noRenter,renterReg;
    private EditText topUpForm;
    private Button topUpBtn, homeButton;
    public static Button manageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        accountInfoPress = findViewById(R.id.acc_press);
        companyInfoPress = findViewById(R.id.comp_press);
        topUpPress = findViewById(R.id.topup_press);

        accInfo = findViewById(R.id.acc_info);
        compInfo =findViewById(R.id.comp_info);
        topUpMenu = findViewById(R.id.topup);
        noComp = findViewById(R.id.no_comp);

        userInfo = findViewById(R.id.username);
        emailInfo = findViewById(R.id.email);
        balanceInfo = findViewById(R.id.balance);

        compName = findViewById(R.id.company_name);
        compAddress = findViewById(R.id.address);
        compPhone = findViewById(R.id.phone);

        topUpBalance = findViewById(R.id.topup_balance);

        topUpForm = findViewById(R.id.topup_form);
        topUpBtn = findViewById(R.id.topup_btn);

        noRenter = findViewById(R.id.no_renter);
        renterReg = findViewById(R.id.no_renter_reg);
        manageButton = findViewById(R.id.manage_bus_btn);

        homeButton = findViewById(R.id.home_btn);

        accInfo.setVisibility(View.VISIBLE);
        compInfo.setVisibility(View.GONE);
        topUpMenu.setVisibility(View.GONE);
        accountInfoPress.setBackgroundResource(R.drawable.border_3_press);

        userInfo.setText(LoginActivity.loggedAccount.name);
        emailInfo.setText(LoginActivity.loggedAccount.email);
        balanceInfo.setText(String.valueOf(LoginActivity.loggedAccount.balance));

        accountInfoPress.setOnClickListener(v->{showAccountInfo();});
        companyInfoPress.setOnClickListener(v->{showCompanyInfo();});
        topUpPress.setOnClickListener(v->{showTopUpMenu();});

        topUpBtn.setOnClickListener(v->{handleTopUp();});
        renterReg.setOnClickListener(v->{moveActivity(mContext,RegisterRenterActivity.class);});
        manageButton.setOnClickListener(v->{moveActivity(mContext,ManageBusActivity.class);});
        homeButton.setOnClickListener(v->{moveActivity(mContext, MainActivity.class);});
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    protected void showAccountInfo(){
        accInfo.setVisibility(View.VISIBLE);
        compInfo.setVisibility(View.GONE);
        topUpMenu.setVisibility(View.GONE);
        accountInfoPress.setBackgroundResource(R.drawable.border_3_press);
        companyInfoPress.setBackgroundResource(R.drawable.border_3);
        topUpPress.setBackgroundResource(R.drawable.border_3);

        userInfo.setText(LoginActivity.loggedAccount.name);
        emailInfo.setText(LoginActivity.loggedAccount.email);
        balanceInfo.setText(String.valueOf(LoginActivity.loggedAccount.balance));
    }

    protected void showCompanyInfo(){
        accInfo.setVisibility(View.GONE);
        topUpMenu.setVisibility(View.GONE);
        accountInfoPress.setBackgroundResource(R.drawable.border_3);
        companyInfoPress.setBackgroundResource(R.drawable.border_3_press);
        topUpPress.setBackgroundResource(R.drawable.border_3);

        if(LoginActivity.loggedAccount.company != null){
            compInfo.setVisibility(View.VISIBLE);
            noComp.setVisibility(View.GONE);
            compName.setText(LoginActivity.loggedAccount.company.companyName);
            compAddress.setText(LoginActivity.loggedAccount.company.address);
            compPhone.setText(LoginActivity.loggedAccount.company.phoneNumber);
        }
        else{
            compInfo.setVisibility(View.GONE);
            noComp.setVisibility(View.VISIBLE);
        }
    }

    protected void showTopUpMenu(){
        accountInfoPress.setBackgroundResource(R.drawable.border_3);
        companyInfoPress.setBackgroundResource(R.drawable.border_3);
        topUpPress.setBackgroundResource(R.drawable.border_3_press);
        accInfo.setVisibility(View.GONE);
        compInfo.setVisibility(View.GONE);
        noComp.setVisibility(View.GONE);
        topUpMenu.setVisibility(View.VISIBLE);
        topUpBalance.setText(String.valueOf(LoginActivity.loggedAccount.balance));
    }

    protected void handleTopUp() {
        String topUpS = topUpForm.getText().toString();
        if (topUpS.isEmpty()) {
            Toast.makeText(mContext, "Mohon untuk mengisi nominal", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpA = Double.parseDouble(topUpS);
        mApiService.topUp(LoginActivity.loggedAccount.id,topUpA).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext, "Top up Gagal", Toast.LENGTH_SHORT).show();
                }
                double upBalance = response.body().payload;
                LoginActivity.loggedAccount.balance = upBalance;
                balanceInfo.setText(String.valueOf(upBalance));
                topUpBalance.setText(String.valueOf(upBalance));
                Toast.makeText(mContext, "Top Up Berhasil", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

