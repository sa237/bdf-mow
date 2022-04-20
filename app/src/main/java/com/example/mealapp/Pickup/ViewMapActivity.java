package com.example.mealapp.Pickup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.adapters.ViewBindingAdapter;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mealapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    private Button connectToAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        connectToAdmin = (Button) findViewById(R.id.map_btn);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        connectToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //temporarily placing this here
                Toast.makeText(ViewMapActivity.this, "Your pickup request has been sent to the admin. The admin will accept/ reject the request within 10 minutes.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), PickupFormActivity.class));


//                if(!name.getText().toString().isEmpty()){
//
//
//
//
//                    userDb.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            if(snapshot.exists()){
//                                for(DataSnapshot childSnapshot: snapshot.getChildren()){
//                                    if(childSnapshot.child("admin").getValue().equals("true")){
//
//                                        String token = childSnapshot.child("token").getValue().toString();
//
//                                        if(!token.isEmpty()){
//
//                                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(token,name.getText().toString(),"New user has sent a request for meal pickup.",getApplicationContext(),MealPickupMain.this);
//                                            notificationsSender.SendNotifications();
//
//                                            Toast.makeText(MealPickupMain.this, "Your pickup request has been sent to the admin. The admin will accept/ reject the request within 10 minutes.", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(getApplicationContext(),PickupFormActivity.class));
//                                            finish();
//
//
//                                        }
//
//
//
//
//
//
//
//                                    }
//
//                                }
//
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                }
//                else{
//
//                    Toast.makeText(MealPickupMain.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng lat1 = new LatLng(30.335176,77.925813);
        map.addMarker(new MarkerOptions().position(lat1).title("Position1"));
        map.moveCamera(CameraUpdateFactory.newLatLng(lat1));



    }
}