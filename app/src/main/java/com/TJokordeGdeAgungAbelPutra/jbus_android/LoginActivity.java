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

public class LoginActivity extends AppCompatActivity {
    private TextView registerHere = null;
    private Button loginButton = null;
    private TextView forgotPass = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email,password;
    public static Account loggedAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerHere = findViewById(R.id.registerhere);
        loginButton = findViewById(R.id.login);
        forgotPass = findViewById(R.id.forgotPassword);
        registerHere.setOnClickListener(v -> {moveActivity(this,RegisterActivity.class);});
        forgotPass.setOnClickListener(v->{moveActivity(this, ForgotPasswordActivity.class);});

        mContext = this;
        mApiService = UtilsApi.getApiService();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton.setOnClickListener(v->handleLogin());
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);
    }

    private void viewToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    protected void handleLogin(){
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if(emailS.isEmpty() || passwordS.isEmpty()){
            Toast.makeText(mContext,"Mohon isi email dan password",Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.login(emailS,passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(mContext,"Application Error" + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();
                if (res != null && res.success) {
                    loggedAccount = res.payload;
                    Toast.makeText(mContext, "Selamat Datang", Toast.LENGTH_SHORT).show();
                    moveActivity(mContext,MainActivity.class);
                    finish();
                }
                else {
                    Toast.makeText(mContext, "Pasword atau Email salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext,"Server Error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}