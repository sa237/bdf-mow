package com.example.mealapp.Acceptances;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Chat.ChatActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsViewActivity extends AppCompatActivity implements ChatRecyclerInterface {


    private String mNumber;
    private String mName;
    private String mId;
    private RecyclerView chatRecyclerView;
    private RecyclerView.Adapter chatViewAdapter;
    private RecyclerView.LayoutManager chatViewManager;
    private TextView noChatText;
    private DatabaseReference currentUserDb;
    private String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_view);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        noChatText = (TextView) findViewById(R.id.no_chats_text);
        chatRecyclerView.setNestedScrollingEnabled(false);
        chatRecyclerView.setHasFixedSize(true);
        chatViewManager = new LinearLayoutManager(ChatsViewActivity.this);
        chatRecyclerView.setLayoutManager(chatViewManager);
        chatViewAdapter = new ChatViewAdapter(ChatsViewActivity.this,getChatMatches(),this);
        chatRecyclerView.setAdapter(chatViewAdapter);
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if (snapshot.child("admin").getValue().toString().equals("true")) {
                        AddInfoToRecycler();

                    }
                    else{
                        AddInfoToUserRecycler();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





//        chatViewAdapter = new ChatViewAdapter(this,chatViewObjectArrayList);
//        chatRecyclerView.setAdapter(chatViewAdapter);
//        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));




    }

    private void AddInfoToUserRecycler() {

        currentUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("chat").exists() && snapshot.child("chat").getChildrenCount() > 0){
                    getAdminInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                noChatText.setVisibility(View.VISIBLE);
                noChatText.setText("No chats. No requests have been accepted yet.");

            }
        });

//        DatabaseReference userChatCheck = currentUserDb.child("chat");
//        userChatCheck.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    getAdminInfo();
//
//                }
//                else{
//
//                    noChatText.setVisibility(View.VISIBLE);
//                    noChatText.setText("No chats.No requests have been accepted yet.");
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void getAdminInfo() {

        DatabaseReference allDb = FirebaseDatabase.getInstance().getReference().child("Users");
        allDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dscn: snapshot.getChildren()){

                    getAdminCredentials(dscn.getKey());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAdminCredentials(String key) {

        DatabaseReference check = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("admin").getValue().toString().equals("true")){
                        mId = currentUserId;
                        mName = snapshot.child("name").getValue().toString();
                        mNumber = snapshot.child("phoneNumber").getValue().toString();

                        ChatViewObject obj = new ChatViewObject(mNumber,mName,mId);
                        resultChats.add(obj);
                        chatViewAdapter.notifyDataSetChanged();

                    }
                }

                else{
                    System.out.println("error getting admin details");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddInfoToRecycler() {

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("Users");

        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    for(DataSnapshot dsn: snapshot.getChildren()){
                        getChats(dsn.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChatsViewActivity.this, "problem", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getChats(String key) {

        DatabaseReference dbR = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        dbR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //check if admin
                    if(snapshot.child("admin").getValue().toString().equals("true")){
                        DatabaseReference dbAdm = FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("acceptances");

                        dbAdm.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                                    for(DataSnapshot idChild: snapshot.getChildren()){
                                        getInfo(idChild.getKey());
                                    }

                                }
                                else{

                                    noChatText.setVisibility(View.VISIBLE);
                                    noChatText.setText("No chats.You haven't accepted any requests yet.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }}); } } }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }}); }



    private void getInfo(String key) {


        DatabaseReference dbForName = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        //mId = key;
        dbForName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mName = snapshot.child("name").getValue().toString();
                    mNumber = snapshot.child("phoneNumber").getValue().toString();
                    mId = key;

                    ChatViewObject obj = new ChatViewObject(mNumber,mName,key);
                    resultChats.add(obj);
                    chatViewAdapter.notifyDataSetChanged();

                    //chatViewObjectArrayList.add(new ChatViewObject(mName,mId));
                }
                else{
                    Toast.makeText(ChatsViewActivity.this, "Failed to get number.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ChatsViewActivity.this, "Failed to get username.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private ArrayList<ChatViewObject> resultChats = new ArrayList<ChatViewObject>();
    private ArrayList<ChatViewObject> getChatMatches(){return resultChats;}

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserProfile.class));
        finish();
    }


    @Override
    public void onChatClick(int position) {
        Intent chatIntent = new Intent(getApplicationContext() , ChatActivity.class);
        chatIntent.putExtra("userId" , resultChats.get(position).getId());
        startActivity(chatIntent);


    }
}
