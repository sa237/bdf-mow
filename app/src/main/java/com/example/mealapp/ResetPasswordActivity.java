package com.example.mealapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealapp.Login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText newPass,confNewPass;
    Button resetPassBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPass = findViewById(R.id.newPassword);
        confNewPass = findViewById(R.id.newConfirmPassword);
        resetPassBtn = findViewById(R.id.resetPasswordButton);
        user = FirebaseAuth.getInstance().getCurrentUser();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPass.getText().toString().isEmpty()){
                    newPass.setError("Required field ");
                    return;
                }

                if(confNewPass.getText().toString().isEmpty()){
                    confNewPass.setError("Required field");
                    return;
                }

                if(!newPass.getText().toString().equals(confNewPass.getText().toString())){
                    confNewPass.setError("Passwords do not match");
                    return;
                }

                user.updatePassword(newPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPasswordActivity.this,"Password changed.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),UserProfile.class));
        finish();
    }
}