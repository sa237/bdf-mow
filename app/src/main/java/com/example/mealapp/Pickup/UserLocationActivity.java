package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mealapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//implements OnMapReadyCallback, LocationListener

public class UserLocationActivity extends AppCompatActivity {


    private GoogleMap mGoogleMap;
    boolean isPermissionGranted;
    private List<String> foods;
    private String numberOfMeals;
    private Double latitude;
    private Double longitude;
    private LocationManager mLocationManager;
    private String userId,userName;
    private DatabaseReference usersDb,currUserDb2,currUserDb1,currUserDb3;
    private String title,body,date;
    private Button submitDetails;
    private Calendar calendar;
    SimpleDateFormat simpleDateFormat;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        foods = new ArrayList<String>();
        submitDetails = (Button) findViewById(R.id.submit_details_btn);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        latitude = 0.0;
        longitude = 0.0;



        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //get user's name from the database
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName = snapshot.child("name").getValue().toString();
                    System.out.println(userName);
                    body = userName + " has sent a pickup request. Tap to view.";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserLocationActivity.this, "User error.", Toast.LENGTH_SHORT).show();

            }
        });

        title = "New pickup request.";
//
//
//
        //getting the array of food from the intent
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        foods = (ArrayList<String>) args.getSerializable("foods");

        //getting the number of meals from the last element of the array
        int size = foods.size();
        numberOfMeals = foods.get(size - 1);
        foods.remove(size - 1);
//        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_show);
//        client = LocationServices.getFusedLocationProviderClient(this);


        checkMyPermission();



//

        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add user details in the database

                addToDbDate();
                addToDbLocation();
                addToDbFoods();




                //send notification to the admin

                sendNotif();





            }
        });


    }


    private void addToDbFoods() {

        currUserDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date).child("food");
        Map userOtherInfo = new HashMap<>();
        for(int i=0 ; i < foods.size() ; i++){
            userOtherInfo.put(foods.get(i) , foods.get(i));
        }

        currUserDb2.setValue(userOtherInfo);
    }

    private void addToDbLocation() {

        currUserDb1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date).child("location");
        Map userInfo = new HashMap<>();
        userInfo.put("latitude", latitude);
        userInfo.put("longitude", longitude);
        currUserDb1.setValue(userInfo);
    }

    private void addToDbDate() {

        //get date to put in database
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpleDateFormat.format(calendar.getTime());
        //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        currUserDb3 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date);
        Map userMealInfo = new HashMap<>();
        userMealInfo.put("numberOfMeals",numberOfMeals);

        currUserDb3.setValue(userMealInfo);
    }

    private void sendNotif() {

        //checking if the user is an admin
        DatabaseReference admDb = FirebaseDatabase.getInstance().getReference().child("Users");

        admDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dsc : snapshot.getChildren()) {

                        getAdmin(dsc.getKey());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//        //when need to send notif to all the users
//        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",title,userName + " has sent a pickup request. Tap to view.",getApplicationContext(),UserLocationActivity.this);
//        notificationsSender.SendNotifications();

    //sending it to just admins


    //}

    private void getAdmin(String key) {

        DatabaseReference dbAd = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        dbAd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("admin").getValue().toString().equals("true")){

                        //send through all topic
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",title,body,UserLocationActivity.this,"ViewRequest");
                        notificationsSender.SendNotifications();

                    }

                    else{
                        //send through all topic
                        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",title,body,UserLocationActivity.this,"Main");
                        notificationsSender.SendNotifications();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//
//

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                //Toast.makeText(UserLocationActivity.this, "Granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
                //getMyLocation();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();


            }
        }).check();
    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), PickupFormActivity.class));
    }



}


