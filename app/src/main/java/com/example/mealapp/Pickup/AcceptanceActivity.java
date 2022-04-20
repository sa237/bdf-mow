package com.example.mealapp.Pickup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealapp.R;

public class AcceptanceActivity extends AppCompatActivity {

    private TextView acceptanceText;
    private Button acceptBtn,declineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        acceptanceText = (TextView) findViewById(R.id.acceptance_txt);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        declineBtn = (Button) findViewById(R.id.decline_btn);






    }
}