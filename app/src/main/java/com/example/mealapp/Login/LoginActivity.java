package com.example.mealapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.AsteriskPasswordTransformationMethod;
import com.example.mealapp.MainActivity;
import com.example.mealapp.R;
import com.example.mealapp.Registration.RegistrationActivity;
import com.example.mealapp.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText loginEmail,loginPassword;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersDb;
    private AlertDialog.Builder reset_alert;
    private LayoutInflater inflater;
    private TextView forgotPassword,signUp,phoneLogin;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");


        inflater = this.getLayoutInflater();


        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPass);
        signUp = findViewById(R.id.textViewSignUp);
        phoneLogin = findViewById(R.id.loginPhone);
        reset_alert = new AlertDialog.Builder(this);


        loginPassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());






        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

               // user.reload();



                //checking that the variables are non-empty

                if(email.isEmpty()){
                    loginEmail.setError("Email is missing");
                    return;
                }


                if(password.isEmpty()){
                    loginPassword.setError("Password is missing");
                    return;

                }




                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            user = firebaseAuth.getCurrentUser();
                            user.reload();
                            DatabaseReference userDb = usersDb.child(user.getUid());


                            if(user.isEmailVerified()){

                                userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            if(snapshot.child("admin").getValue().equals("true")){
                                                //Toast.makeText(LoginActivity.this,"Welcome admin",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), UserProfile.class));
                                                Toast.makeText(getApplicationContext(),"Admins will be directed to this activity for now",Toast.LENGTH_SHORT).show();
                                                finish();


                                            }
                                            else if(snapshot.child("admin").getValue().equals("false")){
                                                //Toast.makeText(LoginActivity.this,"Welcome user",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            else{

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Email is not verified. Code has been resent. Verify Email and login again.",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }


//                        if(userDb.child("admin").equals("true")){
//                            Toast.makeText(LoginActivity.this,"Welcome admin",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),UserActivity.class));
//                            finish();
//
//                        }



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

















//                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if(task.isSuccessful()){
//
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            DatabaseReference userDb = usersDb.child(user.getUid());
//                            if(userDb.child("admin").equals(true)){
//                                System.out.println("welcome admin");
//                                Toast.makeText(LoginActivity.this,"Welcome Admin",Toast.LENGTH_SHORT).show();
//
//
//                            }
//                            else{
//                                startActivity(new Intent(getApplicationContext(),UserActivity.class));
//                                finish();
//                            }
//
//                        }
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });







            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PhoneLoginActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = inflater.inflate(R.layout.resetemail_pop,null);
                reset_alert.setTitle("Reset Forgot Password?")
                        .setMessage("Enter your email to get password reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //validate the email address
                        EditText email = v.findViewById(R.id.resetEmailPop);
                        if(email.getText().toString().isEmpty()){
                            email.setError("Required field");
                            return;
                        }
                        //send the reset link
                        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Reset link sent.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).setNegativeButton("Cancel", null).setView(v)
                        .create().show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();






//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//
//
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//
//
//
//                Toast.makeText(getApplicationContext(),"Email not verified",Toast.LENGTH_SHORT).show();
//
//
//        }
    }
}