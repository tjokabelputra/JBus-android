package com.TJokordeGdeAgungAbelPutra.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView userInfo = findViewById(R.id.username);
        TextView emailInfo = findViewById(R.id.email);
        TextView balanceInfo = findViewById(R.id.balance);

        userInfo.setText("Pathricc1234");
        emailInfo.setText("abelputra101@gmail.com");
        balanceInfo.setText("IDR 5000000.0");
    }
}