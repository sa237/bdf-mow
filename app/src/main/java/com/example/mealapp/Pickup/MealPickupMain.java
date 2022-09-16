package com.example.mealapp.Pickup;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mealapp.Main.MainActivity;
import com.example.mealapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import static android.content.ContentValues.TAG;
import static com.paytm.pgsdk.Constants.CHANNEL_ID;

public class MealPickupMain extends AppCompatActivity {

    private EditText othersMention;
    private Button checkMap;
    private RadioGroup orgType;
    private String mName;
    private RadioButton radioButton;
    private String token;
    private DatabaseReference userDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_pickup_main);


        checkMap = (Button) findViewById(R.id.van_map_btn);
        othersMention = (EditText) findViewById(R.id.others_mention);

        userDb = FirebaseDatabase.getInstance().getReference().child("Users");

        checkMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(getApplicationContext(),ViewMapActivity.class));

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
                    //do nothing
                }
                break;
            case R.id.rb4:
                if(checked){
                    othersMention.setVisibility(View.VISIBLE);

                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}