package com.example.mealapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Donation.PaymentActivity;
import com.example.mealapp.Login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class UserProfile extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }


    private TextView heading, resetPass,updateEmail,paymentHist , subHeading;
    private AlertDialog.Builder reset_alert;
    private LayoutInflater inflater;
    private FirebaseAuth firebaseAuth;
    private String name,userId;
    private DatabaseReference mDatabaseReference , mDatabaseMoneyReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        inflater = this.getLayoutInflater();
        reset_alert = new AlertDialog.Builder(this);
        firebaseAuth = FirebaseAuth.getInstance();
        resetPass = findViewById(R.id.setting_btn2);
        updateEmail = findViewById(R.id.setting_btn3);
        paymentHist = findViewById(R.id.setting_btn4);
        heading = findViewById(R.id.settings_heading);
        subHeading = findViewById(R.id.setting_heading_sub);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        mDatabaseMoneyReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("payments");


        getHeading();
        getSubHeading();




        

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));


            }
        });

        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View v = inflater.inflate(R.layout.resetemail_pop,null);
                reset_alert.setTitle("Update Email")
                        .setMessage("Enter new Email ")
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
                                FirebaseUser user = firebaseAuth.getCurrentUser();


                                user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(UserProfile.this,"Email has been updated.",Toast.LENGTH_SHORT).show();

                                        //reloading user and sending verification email to the new user
                                        user.reload();
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UserProfile.this,"Email has been sent to new email.Verify now and login again.",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).setNegativeButton("Cancel", null).setView(v)
                        .create().show();



            }
        });

        paymentHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PaymentActivity.class));

            }
        });





    }

    private void getSubHeading() {

        mDatabaseMoneyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    int moneyToDisplay = 0;
                    int mealsToDisplay = 0;


                    for(DataSnapshot postSnapshot: snapshot.getChildren()){
                        moneyToDisplay += Integer.parseInt(postSnapshot.child("amount").getValue().toString());

                    }

                    mealsToDisplay = moneyToDisplay/60;

                    subHeading.setText("Hurray Superhuman! You have donated " + mealsToDisplay + " meals a total of " + moneyToDisplay + " Rupees." );

                }
                else{

                    subHeading.setText("You haven't made any donations yet. Be out superhuman by donating meals.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHeading() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String,Object> map = (Map<String,Object>) snapshot.getValue();

                    if(map.get("name") !=null){
                        name = map.get("name").toString();
                        heading.setText("Welcome " + name + " !");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this,"Could not get name",Toast.LENGTH_SHORT).show();

            }
        });


    }


}