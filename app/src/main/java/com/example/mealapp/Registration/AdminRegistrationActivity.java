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

import java.util.HashMap;
import java.util.Map;

public class AdminRegistrationActivity extends AppCompatActivity {

    EditText adminName,adminEmail,adminPassword,adminConfPassword,adminCode;
    Button adminBtn;
    TextView adminAlreadyAccount;
    FirebaseAuth fAuth;



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

        fAuth = FirebaseAuth.getInstance();
        adminPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        adminConfPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

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
                    adminName.setError("Name is a required field");
                    return;
                }

                if(email.isEmpty()){
                    adminEmail.setError("E-mail is a required field");
                    return;
                }
                if(password.isEmpty()){
                    adminPassword.setError("Password is a required field");
                    return;
                }
                if(confPassword.isEmpty()){
                    adminConfPassword.setError("Type the same password here");
                    return;
                }

                if(!password.equals(confPassword)){
                    Toast.makeText(AdminRegistrationActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!code.equals("99901")){
                    Toast.makeText(AdminRegistrationActivity.this,"Code entered wrong.",Toast.LENGTH_SHORT).show();
                    return;


                }

                Toast.makeText(AdminRegistrationActivity.this,"Data is validated",Toast.LENGTH_SHORT).show();


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userId = fAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("admin", "true");
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