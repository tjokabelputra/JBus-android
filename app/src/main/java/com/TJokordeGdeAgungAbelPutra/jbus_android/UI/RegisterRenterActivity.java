package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Renter;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText companyName,address,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        companyName = findViewById(R.id.comp_name);
        address = findViewById(R.id.comp_address);
        phoneNumber = findViewById(R.id.comp_phone);
        Button registerCompany = findViewById(R.id.regist_comp_btn);

        registerCompany.setOnClickListener(v->handleRegisterCompany());
    }

    protected void handleRegisterCompany(){
        String companyNameS = companyName.getText().toString();
        String addressS = address.getText().toString();
        String phoneNumberS = phoneNumber.getText().toString();

        if(companyNameS.isEmpty() || addressS.isEmpty() || phoneNumberS.isEmpty()){
            Toast.makeText(mContext,"Mohon isi semua",Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.registerRenter(LoginActivity.loggedAccount.id,companyNameS,addressS,phoneNumberS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext,"Application Error" + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Renter> res = response.body();
                AboutMeActivity.noRenter.setVisibility(TextView.GONE);
                AboutMeActivity.renterReg.setVisibility(TextView.GONE);
                AboutMeActivity.manageButton.setVisibility(TextView.VISIBLE);
                if (res.success) finish();
                Toast.makeText(mContext,"Akun renter berhasil dibuat",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext,"Server Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}