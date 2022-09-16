package com.example.mealapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealapp.R;
import com.hbb20.CountryCodePicker;

public class PhoneLoginActivity extends AppCompatActivity {
    private CountryCodePicker ccp;
    private EditText userName;
    private EditText phoneNumber;
    private Button getOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);


        userName = (EditText) findViewById(R.id.userNamePhone);
        phoneNumber = (EditText) findViewById(R.id.mobileno);
        getOtp = (Button) findViewById(R.id.getOtp);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNumber);




        getOtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String mUserName = userName.getText().toString();
                String mPhoneNumber = phoneNumber.getText().toString();

                if(mUserName.isEmpty()){
                    userName.setError("Enter a username.");
                }
                if(mPhoneNumber.isEmpty()){
                    phoneNumber.setError("Please enter a valid phone number.");
                }
                else{

                    Intent intent = new Intent(PhoneLoginActivity.this, OtpVerificationActivity.class);
                    intent.putExtra("number",ccp.getFullNumberWithPlus().replace(" ",""));
                    intent.putExtra("name",userName.getText().toString());
                    startActivity(intent);

                }





            }
        });








    }
}