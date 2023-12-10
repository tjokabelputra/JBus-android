package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, passRes, passResConf;
    private Button confirmButton;
    public static String emailResPass, newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.res_pass_email);
        passRes = findViewById(R.id.res_pass);
        passResConf = findViewById(R.id.res_pass_rep);
        confirmButton = findViewById(R.id.res_pass_btn);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        confirmButton.setOnClickListener(v -> {
            checkEmailExist();
        });
    }

    private void moveActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    private void checkEmailExist() {
        String emailS = email.getText().toString();
        mApiService.accByEmail(emailS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().payload != null) {
                    emailResPass = response.body().payload.email;
                    checkPassSame();
                } else {
                    Toast.makeText(mContext, "Email Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    emailResPass = null;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPassSame() {
        String passResS = passRes.getText().toString();
        String passResConfS = passResConf.getText().toString();

        if (passResS.equals(passResConfS)) {
            handleSamePass(passResS);
        } else {
            Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_LONG).show();
            newPass = null;
        }
    }

    private void handleSamePass(String password) {
        mApiService.passExist(emailResPass, password).enqueue(new Callback<BaseResponse<Boolean>>() {
            @Override
            public void onResponse(Call<BaseResponse<Boolean>> call, Response<BaseResponse<Boolean>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().payload == false) {
                    newPass = password;
                    moveActivity(mContext, VerifyResetPasswordActivity.class);
                } else {
                    Toast.makeText(mContext, "Password Tidak Boleh Sama dengan Password Sekarang", Toast.LENGTH_SHORT).show();
                    newPass = null;
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Boolean>> call, Throwable t) {
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
