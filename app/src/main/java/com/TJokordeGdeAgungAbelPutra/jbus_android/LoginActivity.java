package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView registerHere = null;
    private Button loginButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerHere = findViewById(R.id.registerhere);
        loginButton = findViewById(R.id.login);

        getSupportActionBar().hide();
    }

    private void moveActivity(Context context, Class<?> cls){
        Intent intent = new Intent(context,cls);
        startActivity(intent);

        registerHere.setOnClickListener(v -> {moveActivity(this,RegisterActivity.class);});
    }

    private void viewToast(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();

        loginButton.setOnClickListener(l ->{moveActivity(this, MainActivity.class);});
    }
}