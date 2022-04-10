package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mealapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;

public class MealPickupMain extends AppCompatActivity {

    private EditText name,othersMention;
    private Button connectToAdmin;
    private RadioGroup orgType;
    private String mName;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_pickup_main);

        name = (EditText) findViewById(R.id.edt_txt_name);
        connectToAdmin = (Button) findViewById(R.id.connectToAdminBtn);
        othersMention = (EditText) findViewById(R.id.others_mention);


        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //messaging push notifications
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Toast.makeText(MealPickupMain.this, token, Toast.LENGTH_SHORT).show();
                    }
                });







        connectToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Button clicked",Toast.LENGTH_SHORT).show();
                if(name.getText().toString() != null) {
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",name.getText().toString(),"Message",getApplicationContext(),MealPickupMain.this);
                    notificationsSender.SendNotifications();
                }



            }
        });







    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch(view.getId()){
            case R.id.rb1:
            case R.id.rb2:
            case R.id.rb3:
                if(checked){
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rb4:
                if(checked){
                    othersMention.setVisibility(View.VISIBLE);

                }
                break;

        }
    }
}