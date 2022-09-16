package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealapp.Acceptances.ChatsViewActivity;
import com.example.mealapp.Constants.AllConstants;
import com.example.mealapp.chatss.ChatsListModel;
import com.example.mealapp.chatss.MessageModel;
import com.example.mealapp.R;
import com.example.mealapp.UserPickup.UserPickupsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AcceptanceActivity extends FragmentActivity {

    private TextView locationText;
    private Button acceptBtn,declineBtn;
    private String userIdFromIntent,dateFromIntent,location;
    private String chatKey = null;
    private DatabaseReference mDbChat,mDbUser;
    private String currentUserId , currentUserName;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance);

        locationText = (TextView) findViewById(R.id.location_text_from_db);
        acceptBtn = (Button) findViewById(R.id.accept_btn);
        declineBtn = (Button) findViewById(R.id.decline_btn);
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

        //get username of the current user
        DatabaseReference dbforun = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        dbforun.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentUserName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //initializing map fragment
//        SupportMapFragment mapFinalFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.user_loc_final);
//        mapFinalFragment.getMapAsync(this);


        //if accept button clicked

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add request as accepted in the database
                DatabaseReference dbAccept = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);

                dbAccept.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dbAccept.child("accepted").setValue("true");
                            String acceptanceMessage = "Hurray! You're a true hero! Your request for pickup has been accepted. The team will contact you as soon as possible. Keep in touch.";
                            if(chatKey == null){
                                checkChat(userIdFromIntent);
                            }
                            sendMessage(acceptanceMessage);
                            getT(acceptanceMessage,userIdFromIntent,chatKey);
                        } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Acceptance error.", Toast.LENGTH_SHORT).show();

                    }});


                DatabaseReference dbAcceptForAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acceptances");
                dbAcceptForAdmin.push().setValue(userIdFromIntent);

                Toast.makeText(AcceptanceActivity.this, "Request accepted.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChatsViewActivity.class));
                finish();
            }});

        //if decline button clicked

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference dbDecline = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups").child(dateFromIntent);

//                //create and ID for a chat for the admin and the user
//                chatKey = FirebaseDatabase.getInstance().getReference().child("Chats").push().getKey();

                dbDecline.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            dbDecline.child("accepted").setValue("false");
                            String rejectionMessage = "Sorry for the inconvenience! Your pick-up request has been denied as the driver is temporarily unavailable.";
                            if(chatKey == null){
                                checkChat(userIdFromIntent);
                            }
                            sendMessage(rejectionMessage);
                            getT(rejectionMessage,userIdFromIntent,chatKey);

//                            //check if chat already exists for user
//                            DatabaseReference dbAddChatToUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("chat");
//                            dbAddChatToUser.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.exists()){
//                                        //do nothing
//                                    }
//                                    else{
//
//                                        dbAddChatToUser.child("chatId").setValue(chatKey);
//                                        sendRejectionMessage();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });

                        } }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AcceptanceActivity.this, "Rejection error.", Toast.LENGTH_SHORT).show();
                    }});

                DatabaseReference dbRejectForAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("acceptances");
                dbRejectForAdmin.push().setValue(userIdFromIntent);
//                dbRejectForAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(snapshot.exists()){
//                            //do nothing
//
//                        }
//                        else{
//                            dbRejectForAdmin.child("chatId").setValue(chatKey);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) { }});

                Toast.makeText(AcceptanceActivity.this, "The request has been rejected.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),UserPickupsActivity.class));
                finish();
            }});
    }

    private void checkChat(final String userIdFromIntent){
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUserId);
        Query query = databaseReference.orderByChild("member").equalTo(userIdFromIntent);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String id = ds.child("member").getValue().toString();
                        if(id.equals(userIdFromIntent)){
                            chatKey = ds.getKey();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void createChat(String msg){
        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUserId);
        chatKey = databaseReference.push().getKey();
        ChatsListModel chatsListModel = new ChatsListModel(chatKey,msg,userIdFromIntent);
        databaseReference.child(chatKey).setValue(chatsListModel);


        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(userIdFromIntent);
        ChatsListModel chatList = new ChatsListModel(chatKey,msg,currentUserId);
        databaseReference.child(chatKey).setValue(chatList);

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatKey);
        MessageModel messageModel = new MessageModel(currentUserId,userIdFromIntent,msg);
        databaseReference.push().setValue(messageModel);


    }


    private void sendMessage(String msg){
        if(chatKey == null){
            createChat(msg);

        }
        else{
            MessageModel messageModel = new MessageModel(currentUserId,userIdFromIntent,msg);
            databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatKey);
            databaseReference.push().setValue(messageModel);

            Map<String, Object> map = new HashMap<>();
            map.put("lastMessage", msg);
            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUserId).child(chatKey);
            databaseReference.updateChildren(map);


            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(userIdFromIntent).child(chatKey);
            Map<String, Object> update = new HashMap<>();
            update.put("lastMessage", msg);
            databaseReference.updateChildren(update);
        }


    }

    private void getT(String message,String userIdFromIntent , String chatId){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userIdFromIntent);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();
                String name = snapshot.child("name").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();

                try {
                    data.put("title",currentUserName);
                    data.put("message",message);
                    data.put("userIdFromIntent",userIdFromIntent);
                    data.put("chatId",chatId);

                    to.put("to" , token);
                    to.put("data",data);

                    sendNotification(to);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL,to , response -> {

            Log.d("notification","sendNotification: " +  response);


        },error -> {
            Log.d("notification","sendNotification: " + error);

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("Authorization" , "key=" + AllConstants.SERVER_KEY);
                map.put("Content-Type","application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
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
    public void onBackPressed() {
        Toast.makeText(this, "Please accept/decline the request to move back.", Toast.LENGTH_SHORT).show();
    }
}