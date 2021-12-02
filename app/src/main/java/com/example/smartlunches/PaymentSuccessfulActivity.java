package com.example.smartlunches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentSuccessfulActivity extends AppCompatActivity {

    Button continue_home;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        continue_home = findViewById(R.id.continue_home);

        continue_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentSuccessfulActivity.this , HomeActivity.class));
            }
        });
    }
}