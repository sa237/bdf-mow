package com.example.mealapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.AsteriskPasswordTransformationMethod;
import com.example.mealapp.Login.LoginActivity;
import com.example.mealapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;

public class AdminRegistrationActivity extends AppCompatActivity {

    private EditText adminName,adminEmail,adminPassword,adminConfPassword,adminCode;
    private Button adminBtn;
    private TextView adminAlreadyAccount;
    private FirebaseAuth fAuth;
    private String token;
    private CountryCodePicker ccpForAdmin;
    private EditText phoneNoAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_activity);

        //registration variables
        adminName = findViewById(R.id.adminRegisterName);
        adminEmail = findViewById(R.id.adminRegisterEmail);
        adminPassword = findViewById(R.id.adminRegisterPassword);
        adminConfPassword = findViewById(R.id.adminRegisterConfirmPassword);
        adminCode = findViewById(R.id.adminCode);
        adminBtn = findViewById(R.id.adminRegisterButton);
        adminAlreadyAccount = findViewById(R.id.adminAlreadyHaveAccount);
        ccpForAdmin = (CountryCodePicker) findViewById(R.id.ccp_admin);
        phoneNoAdmin = (EditText) findViewById(R.id.mobileno_admin);
        ccpForAdmin.registerCarrierNumberEditText(phoneNoAdmin);

        fAuth = FirebaseAuth.getInstance();
        adminPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        adminConfPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());


        //get token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    token = task.getResult();
                }
            }
        });



        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = adminName.getText().toString();
                String email = adminEmail.getText().toString();
                String password = adminPassword.getText().toString();
                String confPassword = adminConfPassword.getText().toString();
                String code = adminCode.getText().toString();

                //checking that all the variables have value

                if(name.isEmpty()){
                    adminName.setError("Name is a required field.");
                    return;
                }

                if(email.isEmpty()){
                    adminEmail.setError("E-mail is a required field.");
                    return;
                }
                if(password.isEmpty()){
                    adminPassword.setError("Password is a required field.");
                    return;
                }
                if(confPassword.isEmpty()){
                    adminConfPassword.setError("Type the same password here.");
                    return;
                }

                if(!password.equals(confPassword)){
                    Toast.makeText(AdminRegistrationActivity.this,"Passwords do not match.",Toast.LENGTH_SHORT).show();
                    return;
                }

                //checking validity of phone number
                if(phoneNoAdmin.getText().toString().isEmpty()){
                    phoneNoAdmin.setError("Enter a valid number");
                    return;
                }

                if(!code.equals("99901")){
                    Toast.makeText(AdminRegistrationActivity.this,"Code entered wrong.",Toast.LENGTH_SHORT).show();
                    return;


                }

                //Toast.makeText(AdminRegistrationActivity.this,"Data is validated",Toast.LENGTH_SHORT).show();


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = fAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);



                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("admin", "true");
                            userInfo.put("phoneNumber",ccpForAdmin.getFullNumberWithPlus().replace(" ",""));
                            userInfo.put("token", token);
                            currentUserDb.updateChildren(userInfo);


                            startActivity(new Intent(getApplicationContext(), VerificationActivity.class));
                            finish();
                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminRegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        adminAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}