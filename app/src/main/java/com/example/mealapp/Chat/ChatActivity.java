package com.example.mealapp.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mealapp.Acceptances.ChatViewAdapter;
import com.example.mealapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView chatsRecyclerView;
    private RecyclerView.Adapter chatsAdapter;
    private RecyclerView.LayoutManager chatsManager;
    private String currentUserId , userIdFromIntent,chatId;

    private EditText mSendEditText;
    private Button mSendButton;
    DatabaseReference mDatabaseUser , mDatabaseChat,mCheckIfAdmin;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userIdFromIntent = getIntent().getStringExtra("userId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chats");

        chatsRecyclerView = findViewById(R.id.chatFinalRecycler);
        chatsRecyclerView.setNestedScrollingEnabled(false);
        chatsRecyclerView.setHasFixedSize(false);
        chatsManager = new LinearLayoutManager(ChatActivity.this);
        chatsRecyclerView.setLayoutManager(chatsManager);
        chatsAdapter = new ChatAdapter(ChatActivity.this,getChatMatches());
        chatsRecyclerView.setAdapter(chatsAdapter);
        mSendEditText = (EditText) findViewById(R.id.message_box);
        mSendButton = (Button) findViewById(R.id.text_send_btn);

        //check if the current user is an admin or not
        mCheckIfAdmin = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        mCheckIfAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("admin").getValue().toString().equals("true")){
                        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("acceptances").child(userIdFromIntent).child("chatId");
                        getChatId();

                    }

                    else {

                        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("chat").child("chatId");
                        getChatId();

                    } } }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
            }
        });




    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){

                    String message = null;
                    String createdByUser = null;

                    if(snapshot.child("text").getValue() != null){
                        message = snapshot.child("text").getValue().toString();
                    }

                    if(snapshot.child("createdByUser").getValue() != null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if(message != null && createdByUser!=null){
                        Boolean currentUserBoolean = false;
                        if(createdByUser.equals(currentUserId)){
                            currentUserBoolean  = true;

                        }
                        ChatObject newMessage = new ChatObject(message,currentUserBoolean);
                        allChats.add(newMessage);
                        chatsAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void sendMessage() {
        String sendMessageText = mSendEditText.getText().toString();

        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();

            Map newMessage = new HashMap();
            newMessage.put("createdByUser",currentUserId);
            newMessage.put("text",sendMessageText);

            newMessageDb.setValue(newMessage);
        }
        mSendEditText.setText(null);

    }

    private ArrayList<ChatObject> allChats = new ArrayList<ChatObject>();
    private ArrayList<ChatObject> getChatMatches(){return allChats;}
}