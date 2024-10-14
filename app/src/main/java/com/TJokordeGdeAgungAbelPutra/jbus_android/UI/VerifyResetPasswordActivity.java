package com.TJokordeGdeAgungAbelPutra.jbus_android.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Account;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.JavaMailAPI;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.codeGenerator;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyResetPasswordActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private String verCode;
    private EditText code;
    private int attempt = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_reset_password);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        codeGenerator codeGenerator = new codeGenerator();
        verCode = codeGenerator.getCode();

        code = findViewById(R.id.verf_code);
        Button verBtn = findViewById(R.id.verf_btn);
        TextView reSend = findViewById(R.id.re_code);

        sendMail();

        reSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verCode = codeGenerator.getCode();
                sendMail();
            }
        });

        verBtn.setOnClickListener(v->checkCode());
    }

    private void moveActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    private void sendMail() {
        JavaMailAPI.sendEmail(ForgotPasswordActivity.emailResPass,"Verification Code",verCode);
        Toast.makeText(mContext,"Mengirim Kode",Toast.LENGTH_SHORT).show();
    }

    private void checkCode(){
        if(code.getText().toString().equals(verCode)){
            handleChangePassword();
        }
        else{
            attempt -= 1;
            Toast.makeText(mContext,"Kode salah, masih ada " + attempt +" kali percobaan lagi", Toast.LENGTH_SHORT).show();
            if(attempt == 0){
                finish();
            }
        }
    }

    private void handleChangePassword(){
        String email = ForgotPasswordActivity.emailResPass;
        String password = ForgotPasswordActivity.newPass;
        mApiService.resetPassword(email,password).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext,"App Error",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mContext,"Password Berhasil Diubah",Toast.LENGTH_SHORT).show();
                ForgotPasswordActivity.emailResPass = null;
                ForgotPasswordActivity.newPass = null;
                moveActivity(mContext,LoginActivity.class);
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}