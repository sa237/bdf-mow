package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.adapters.ViewBindingAdapter;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mealapp.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.FormatFlagsConversionMismatchException;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private Button connectToAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        connectToAdmin = (Button) findViewById(R.id.map_btn);

        //title and body of the notification
//        title = "New request received.";
//        body = name + " has sent a new request. Tap to view.";






        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //on clicking button to connect to admin

        connectToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PickupFormActivity.class));

            }
        });




//        connectToAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all","New request",name + "sent a new request. Click tp view.",getApplicationContext(),ViewMapActivity.this);
//                notificationsSender.SendNotifications();
//                startActivity(new Intent(getApplicationContext(),PickupFormActivity.class));
//                //temporarily placing this here
////                Toast.makeText(ViewMapActivity.this, "Your pickup request has been sent to the admin. The admin will accept/ reject the request within 10 minutes.", Toast.LENGTH_SHORT).show();
////                startActivity(new Intent(getApplicationContext(), PickupFormActivity.class));
//
//
//
//
//
////                    userDb.addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot snapshot) {
////
////                            if(snapshot.exists()){
////                                for(DataSnapshot childSnapshot: snapshot.getChildren()){
////                                    if(childSnapshot.child("admin").getValue().equals("true")){
////                                        //System.out.println("Exists");
////
////                                        String token = childSnapshot.child("token").getValue().toString();
////
////                                        if(!token.isEmpty()){
////
////                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,name,"New user has sent a request for meal pickup.",getApplicationContext(),ViewMapActivity.this);
////                                            notificationsSender.SendNotifications();
////
////                                            Toast.makeText(ViewMapActivity.this, "Your pickup request has been sent to the admin. The admin will accept/ reject the request within 10 minutes.", Toast.LENGTH_SHORT).show();
////                                            startActivity(new Intent(getApplicationContext(),PickupFormActivity.class));
////                                            finish();
////
////
////                                        }
////
////
////
////
////
////
////
////                                    }
////
////                                }
////
////
////                            }
////
////                        }
////
////
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError error) {
////
////                        }
////                    });
//
//
//
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng lat1 = new LatLng(30.335176,77.925813);
        CameraUpdate centre = CameraUpdateFactory.newLatLng(lat1);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
        Marker marker = map.addMarker(new MarkerOptions().position(lat1).title("Thakurpur,Dehradun,Uttarakhand"));
        map.moveCamera(centre);
        map.animateCamera(zoom);


//        map.addMarker(new MarkerOptions().position(lat1).title("Position1"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(lat1));



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MealPickupMain.class));
    }
}