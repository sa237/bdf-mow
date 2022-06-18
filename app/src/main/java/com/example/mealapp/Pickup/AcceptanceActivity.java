package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Acceptances.ChatsViewActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserPickup.UserPickupsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AcceptanceActivity extends FragmentActivity implements OnMapReadyCallback{

    private TextView locationText;
    private Button acceptBtn,declineBtn;
    private String userIdFromIntent,dateFromIntent,location;
    GoogleMap user_map;
    private Geocoder geocoder;
    private String chatKey;
    private DatabaseReference mDbChat,mDbUser;
    private String currentUserId;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        locationText = (TextView) findViewById(R.id.location_text_from_db);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        declineBtn = (Button) findViewById(R.id.decline_btn);
        geocoder = new Geocoder(this);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDbChat = FirebaseDatabase.getInstance().getReference().child("Chats");



        //getting data from intent
        Intent intent = getIntent();
        userIdFromIntent = intent.getStringExtra("userId");
        dateFromIntent = intent.getStringExtra("date");
//        latitudeFromIntent = intent.getStringExtra("latitude");
//        longitudeFromIntent = intent.getStringExtra("longitude");

        //get location and print in the text box

        getLocationFromDb();

        //initializing map fragment
//        SupportMapFragment mapFinalFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.user_loc_final);
//        mapFinalFragment.getMapAsync(this);


        //if accept button clicked

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add request as accepted in the database
                DatabaseReference dbAccept = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);
                //create and ID for a chat for the admin and the user
                chatKey = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

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
                                        sendAcceptanceMessage();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Acceptance error.", Toast.LENGTH_SHORT).show();

                    }});


                DatabaseReference dbAcceptForAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acceptances").child(userIdFromIntent);
                dbAcceptForAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            sendAcceptanceMessage();
                            //do nothing

                        }
                        else{
                            dbAcceptForAdmin.child("chatId").setValue(chatKey);
                            sendAcceptanceMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }});

                //send automatic acceptance message to the user.
//                user = FirebaseAuth.getInstance().getCurrentUser();
//                user.reload();


                Toast.makeText(AcceptanceActivity.this, "The request has been accepted. You can now chat with the user.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChatsViewActivity.class));
                finish();
            }});

        //if decline button clicked

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference dbDecline = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);

                //create and ID for a chat for the admin and the user
                chatKey = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                dbDecline.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dbDecline.child("accepted").setValue("false");

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
                                        sendRejectionMessage();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Rejection error.", Toast.LENGTH_SHORT).show();
                    }});

                DatabaseReference dbRejectForAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acceptances").child(userIdFromIntent);
                dbRejectForAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            sendRejectionMessage();
                            //do nothing

                        }
                        else{
                            dbRejectForAdmin.child("chatId").setValue(chatKey);
                            sendRejectionMessage();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }});

                Toast.makeText(AcceptanceActivity.this, "The request has been rejected.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),UserPickupsActivity.class));
                finish();
            }});
    }

    private void sendRejectionMessage() {
        mDbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("chat").child("chatId");
        getChatId("reject");
    }

    private void sendAcceptanceMessage() {


        mDbUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("chat").child("chatId");
        getChatId("accept");
    }

    private void getChatId(String message) {
        mDbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatKey = snapshot.getValue().toString();
                    mDbChat = mDbChat.child(chatKey);
                    if(message.equals("accept")){
                        sendMessageFinal("accept");

                    }
                    else if(message.equals("reject")){
                        sendMessageFinal("reject");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AcceptanceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendMessageFinal(String message) {
        String msg;
        if(message.equals("accept")){
            msg = "Dear user,your request has been accepted. Please wait while the admin connects with you.";

        }
        else{
            msg = "Dear user,the admin is unavailable right now. We hope to connect with you soon.";
        }


        DatabaseReference newMessageDb = mDbChat.push();

        Map newMessage = new HashMap();
        newMessage.put("createdByUser",currentUserId);
        newMessage.put("text",msg);

        newMessageDb.setValue(newMessage);
    }

    private void getLocationFromDb() {
        DatabaseReference dbRefForLoc = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);
        dbRefForLoc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    location = snapshot.child("location").getValue().toString();
                    locationText.setText(location);

                }
                else {
                    locationText.setText("Error getting user location.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        user_map = googleMap;

        try {
            List<Address> addresses = geocoder.getFromLocationName(location,1);
            Address address = addresses.get(0);
            Log.d(TAG,"onMapReady:" + address.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        LatLng loc = new LatLng(Double.parseDouble(latitudeFromIntent),Double.parseDouble(longitudeFromIntent));
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(loc);
//        CameraUpdate zoomTo = CameraUpdateFactory.zoomTo(10);
//        Marker marker = user_map.addMarker(new MarkerOptions().position(loc).title("User Location"));
//        user_map.moveCamera(cameraUpdate);
//        user_map.animateCamera(zoomTo);



    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please accept/decline the request to move back.", Toast.LENGTH_SHORT).show();
    }
}