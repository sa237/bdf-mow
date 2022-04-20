package com.example.mealapp.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealapp.AsteriskPasswordTransformationMethod;
import com.example.mealapp.Login.LoginActivity;
import com.example.mealapp.Pickup.MealPickupMain;
import com.example.mealapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText registerName, registerEmail, registerPassword, registerConfPassword;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private TextView alreadyAccount,adminRegister;
    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //registration variables
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfPassword = findViewById(R.id.registerConfirmPassword);
        registerBtn = findViewById(R.id.registerButton);
        alreadyAccount = findViewById(R.id.alreadyHaveAccount);
        adminRegister = findViewById(R.id.reg_admin);

        //to replace password with *

        registerPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        registerConfPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        //Database variables
        mAuth = FirebaseAuth.getInstance();

        //get the token of the user

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Toast.makeText(RegistrationActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confPassword = registerConfPassword.getText().toString();

                //checking that all the variables have value

                if(name.isEmpty()){
                    registerName.setError("Name is a required field");
                    return;
                }

                if(email.isEmpty()){
                    registerEmail.setError("E-mail is a required field");
                    return;
                }
                if(password.isEmpty()){
                    registerPassword.setError("Password is a required field");
                    return;
                }
                if(confPassword.isEmpty()){
                    registerConfPassword.setError("Type the same password here");
                    return;
                }

                if(!password.equals(confPassword)){
                    Toast.makeText(RegistrationActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("admin", "false");
                            userInfo.put("token",token);
                            currentUserDb.updateChildren(userInfo);


                            startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                            finish();
                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });





//                mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                    @Override
//                    public void onSuccess(AuthResult authResult) {
//                        startActivity(new Intent(getApplicationContext(),VerificationActivity.class));
//                        finish();
//
//
//
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });






            }

        });

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        adminRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminRegistrationActivity.class));
            }
        });










    }
}
