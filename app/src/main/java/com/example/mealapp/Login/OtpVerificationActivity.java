package com.example.mealapp.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.MainActivity;
import com.example.mealapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {
    private EditText otpText;
    private Button verifyOtp;
    private String number;
    private String name;
    private FirebaseAuth mAuth;
    private String otpId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otpText = (EditText) findViewById(R.id.OtpTxt);
        verifyOtp = (Button) findViewById(R.id.verifyOtpBtn);
        number = getIntent().getStringExtra("number").toString();
        name = getIntent().getStringExtra("name").toString();
        mAuth = FirebaseAuth.getInstance();




        
        //function to create otp
        initiateOtp();

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking the validity of the otp
                if(otpText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter the OTP.", Toast.LENGTH_SHORT).show();
                }

                else if(otpText.getText().toString().length() != 6){
                    Toast.makeText(getApplicationContext(), "The OTP is invalid.", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,otpText.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });









    }

    private void initiateOtp() {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(
                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                        otpId = s;
                                    }

                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        signInWithPhoneAuthCredential(phoneAuthCredential);



                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                }
                        )          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);




    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();
                            long creationTimeStamp = user.getMetadata().getCreationTimestamp();
                            long lastSignInTimeStamp = user.getMetadata().getLastSignInTimestamp();

                            if(creationTimeStamp == lastSignInTimeStamp){

                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getApplicationContext(),"New user signInWithCredential:success",Toast.LENGTH_SHORT).show();

                                //save user data to database

                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("admin", "false");
                                currentUserDb.updateChildren(userInfo);


                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();



                            }

                            else{

                                // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(),"signInWithCredential:success",Toast.LENGTH_SHORT).show();



                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();

                            }
//


                        } else {

                            Toast.makeText(getApplicationContext(),"signIn:failure",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}