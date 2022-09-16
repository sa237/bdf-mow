package com.example.mealapp.chatss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TintableCheckedTextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.mealapp.R;
import com.example.mealapp.databinding.ActivityChatsBinding;
import com.example.mealapp.databinding.LeftItemLayoutBinding;
import com.example.mealapp.databinding.RightItemLayoutBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class ChatsActivity extends AppCompatActivity {

    private String currentUserId , userIdFromIntent,chatId = null,currentUserName;
    private DatabaseReference  databaseReference;
    private TextView headingText;
    ActivityChatsBinding binding;
    private SharedPreferences sharedPreferences;
    private FirebaseRecyclerAdapter<MessageModel,ViewHolder> firebaseRecyclerAdapter;
    private String chatIdToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.activity_chats,null,false);
        setContentView(binding.getRoot());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        DatabaseReference dbrt = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        dbrt.addListenerForSingleValueEvent(new ValueEventListener() {
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


        if(getIntent().hasExtra("chatId")){

            userIdFromIntent = getIntent().getStringExtra("userId");
            chatId = getIntent().getStringExtra("chatId");

        }
        else{
            userIdFromIntent = getIntent().getStringExtra("userId");
        }



        headingText = findViewById(R.id.msg_username);

        getUsernameForToolbar();
        checkIfAdmin();


        if(chatId == null){
            checkChat(userIdFromIntent);
        }

        binding.setActivity(this);







        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.messageTextEditText.getText().toString().trim();
                if(message.isEmpty()){
                    binding.messageTextEditText.setError("Enter message...");
                }

                else{
                    sendMessage(message);
                    getToken(message,userIdFromIntent,chatId);

                }
                binding.messageTextEditText.setText("");

            }
        });

        binding.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRequestByUser();
                deleteAcceptanceByAdmin();
                deleteChats();
            }
        });

        binding.checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ms = "Your order has been pickup up and will be delivered safely. Thank you for your contribution! Hope to see you again.";
                if(chatId == null){
                    checkChat(userIdFromIntent);
                }
                sendMessage(ms);
                getToken(ms,userIdFromIntent,chatId);
            }
        });

    }

    private void deleteAcceptanceByAdmin() {
        DatabaseReference admDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("acceptances");

        admDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    for(DataSnapshot d: snapshot.getChildren()){
                        if(d.getValue().toString().equals(userIdFromIntent)){
                            DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("acceptances").child(d.getKey());
                            del.removeValue();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        admDb.orderByKey().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue().toString().equals(userIdFromIntent)){
//                    snapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

    private void deleteChats() {
        DatabaseReference dbForChats = FirebaseDatabase.getInstance().getReference("ChatList").child(userIdFromIntent);


        dbForChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    for(DataSnapshot sd: snapshot.getChildren()){
                        chatIdToDelete = sd.getKey();
                        Toast.makeText(ChatsActivity.this, chatIdToDelete, Toast.LENGTH_SHORT).show();
                        sd.getRef().removeValue();

                        //remove the same chat from the admin side
                        DatabaseReference r = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUserId).child(chatIdToDelete);
//                        r.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if(snapshot.exists())
//                                    snapshot.getRef().removeValue();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                        r.removeValue();

                        //remove the chats completely from the "Chats" attribute
                        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Chats").child(chatIdToDelete);
                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    snapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //dbr.removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        startActivity(new Intent(getApplicationContext(),ChatsViewActivity.class));


    }

    private void deleteRequestByUser() {

        DatabaseReference userD = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent).child("pickups");


        userD.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    for(DataSnapshot sc: snapshot.getChildren()){
                        sc.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkIfAdmin() {

        DatabaseReference checkAd = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        checkAd.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("admin").getValue().toString().equals("true")){
                        binding.deletebtn.setVisibility(View.VISIBLE);
                        binding.checkbtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUsernameForToolbar() {
        DatabaseReference dbRefForUsername = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdFromIntent);
        dbRefForUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.messageTb.msgUsername.setText(snapshot.child("name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                            chatId = ds.getKey();
                            readMessages(chatId);
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
        chatId = databaseReference.push().getKey();
        ChatsListModel chatsListModel = new ChatsListModel(chatId,msg,userIdFromIntent);
        databaseReference.child(chatId).setValue(chatsListModel);


        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(userIdFromIntent);
        ChatsListModel chatList = new ChatsListModel(chatId,msg,currentUserId);
        databaseReference.child(chatId).setValue(chatList);

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
        MessageModel messageModel = new MessageModel(currentUserId,userIdFromIntent,msg);
        databaseReference.push().setValue(messageModel);

        readMessages(chatId);


    }

    private void sendMessage(String msg){
        if(chatId == null){
            createChat(msg);

        }
        else{
            MessageModel messageModel = new MessageModel(currentUserId,userIdFromIntent,msg);
            databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
            databaseReference.push().setValue(messageModel);

            Map<String, Object> map = new HashMap<>();
            map.put("lastMessage", msg);
            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(currentUserId).child(chatId);
            databaseReference.updateChildren(map);


            databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(userIdFromIntent).child(chatId);
            Map<String, Object> update = new HashMap<>();
            update.put("lastMessage", msg);
            databaseReference.updateChildren(map);
        }


    }

    private void readMessages(String chatId){
        Query query = FirebaseDatabase
                .getInstance().getReference().child("Chats")
                .child(chatId);
        FirebaseRecyclerOptions<MessageModel> options = new FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(query,MessageModel.class).build();

        query.keepSynced(true);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MessageModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MessageModel model) {
                holder.viewDataBinding.setVariable(BR.message,model);


            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                if(viewType == 0){
                    ViewDataBinding viewDataBinding = RightItemLayoutBinding.inflate(
                            LayoutInflater.from(getBaseContext()),parent,false);
                    return new ViewHolder(viewDataBinding);

                }
                else{
                    ViewDataBinding dataBinding = LeftItemLayoutBinding.inflate(
                            LayoutInflater.from(getBaseContext()),parent,false);
                    return new ViewHolder(dataBinding);
                }

            }

            @Override
            public int getItemViewType(int position) {
                MessageModel messageModel = getItem(position);
                if(currentUserId.equals(messageModel.getSender()))
                    return 0;
                else
                    return 1;
            }
        };
        binding.recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessage.setHasFixedSize(false);
        binding.recyclerViewMessage.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();






    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding viewDataBinding;

        public ViewHolder(@NonNull ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());
            this.viewDataBinding = viewDataBinding;
        }
    }

    private void getToken(String message,String userIdFromIntent , String chatId){
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL,to ,response -> {

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),
                ChatsViewActivity.class));
        finish();
    }
}


