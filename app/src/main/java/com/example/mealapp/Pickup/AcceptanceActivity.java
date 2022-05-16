package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Acceptances.ChatsViewActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserPickup.UserPickupsActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcceptanceActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView acceptanceText;
    private Button acceptBtn,declineBtn;
    private String userIdFromIntent,dateFromIntent,latitudeFromIntent,longitudeFromIntent;
    GoogleMap user_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        acceptanceText = (TextView) findViewById(R.id.acceptance_txt);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        declineBtn = (Button) findViewById(R.id.decline_btn);


        //getting data from intent
        Intent intent = getIntent();
        userIdFromIntent = intent.getStringExtra("userId");
        dateFromIntent = intent.getStringExtra("date");
        latitudeFromIntent = intent.getStringExtra("latitude");
        longitudeFromIntent = intent.getStringExtra("longitude");

        //initializing map fragment
        SupportMapFragment mapFinalFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.user_loc_final);
        mapFinalFragment.getMapAsync(this);


        //if accept button clicked

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add request as accepted in the database
                DatabaseReference dbAccept = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);
                //create and ID for a chat for the admin and the user
                String chatKey = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                dbAccept.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){


                            dbAccept.child("accepted").setValue("true");


                            //check if chat already exists for user
                            DatabaseReference dbAddChatToUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("chat");
                            dbAddChatToUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        //do nothing
                                    }
                                    else{

                                        dbAddChatToUser.child("chatId").setValue(chatKey);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }); } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Acceptance error.", Toast.LENGTH_SHORT).show();

                    }});


                DatabaseReference dbAcceptForAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acceptances").child(userIdFromIntent);
                dbAcceptForAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //do nothing

                        }
                        else{
                            dbAcceptForAdmin.child("chatId").setValue(chatKey);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }});
                Toast.makeText(AcceptanceActivity.this, "The request has been accepted. You can now chat with the user.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChatsViewActivity.class));
                finish();
            }});

        //if decline button clicked

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference dbDecline = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);

                dbDecline.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dbDecline.child("accepted").setValue("false");

                        } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Rejection error.", Toast.LENGTH_SHORT).show();
                    }});

                Toast.makeText(AcceptanceActivity.this, "The request has been rejected.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),UserPickupsActivity.class));
                finish();
            }});
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        user_map = googleMap;
        LatLng loc = new LatLng(Double.parseDouble(latitudeFromIntent),Double.parseDouble(longitudeFromIntent));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(loc);
        CameraUpdate zoomTo = CameraUpdateFactory.zoomTo(10);
        Marker marker = user_map.addMarker(new MarkerOptions().position(loc).title("User Location"));
        user_map.moveCamera(cameraUpdate);
        user_map.animateCamera(zoomTo);



    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please accept/decline the request to move back.", Toast.LENGTH_SHORT).show();
    }
}