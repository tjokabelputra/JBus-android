package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private Button registerAcc = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText name,email,password,passwordRepeat;
    private TextView accExist;
    private Button registerButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerAcc = findViewById(R.id.regist_button);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        name = findViewById(R.id.user_reg);
        email = findViewById(R.id.mail_reg);
        password = findViewById(R.id.pass_reg);
        passwordRepeat = findViewById(R.id.pass_reg_repeat);
        registerButton = findViewById(R.id.regist_button);
        registerButton.setOnClickListener(v->handleRegister());

        accExist = findViewById(R.id.acc_exist);
        accExist.setOnClickListener(view -> {moveActivity(this,LoginActivity.class);});
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    protected void handleRegister(){
        String nameS = name.getText().toString();
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        String passwordRepeatS = passwordRepeat.getText().toString();

        if (nameS.isEmpty() || emailS.isEmpty() || passwordS.isEmpty() || passwordRepeatS.isEmpty()) {
            Toast.makeText(mContext, "Mohon untuk mengisi semua", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!passwordS.equals(passwordRepeatS)){
            Toast.makeText(mContext, "Password Tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.register(nameS,emailS,passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application Error" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();

                if (res.success) finish();
                Toast.makeText(mContext,"Akun berhasil dibuat",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext,"Problem with the server",Toast.LENGTH_SHORT).show();
            }
        });
    }
}