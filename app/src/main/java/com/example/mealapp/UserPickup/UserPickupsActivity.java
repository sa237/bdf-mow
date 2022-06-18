package com.example.mealapp.UserPickup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealapp.Pickup.AcceptanceActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserProfile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserPickupsActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView userPickupRecyclerView;
    private TextView noUserPickupText;
    private RecyclerView.Adapter mPickupsAdapter;
    private RecyclerView.LayoutManager mPickupsLayoutManager;
    private String date , userName,noOfMeals,id,location;
    private String userIdForIntent;
    private Date dateFormat;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pickups);

        userPickupRecyclerView = findViewById(R.id.user_pickup_recycler);
        noUserPickupText = findViewById(R.id.no_pickups_text);
        userPickupRecyclerView.setNestedScrollingEnabled(false);
        userPickupRecyclerView.setHasFixedSize(true);



        getUserPickupData();






//        Collections.sort(resultPickups, new Comparator<UserPickupsObject>() {
//            public int compare(UserPickupsObject o1, UserPickupsObject o2) {
//                return o1.getDate().compareTo(o2.getDate());
//            }
//        });
//
//        //reverse the list to bring the recent request at the top
//        Collections.reverse(resultPickups);



        mPickupsLayoutManager = new LinearLayoutManager(UserPickupsActivity.this);
        userPickupRecyclerView.setLayoutManager(mPickupsLayoutManager);
        mPickupsAdapter = new UserPickupsAdapter(getDataMatches(),UserPickupsActivity.this,this);
        userPickupRecyclerView.setAdapter(mPickupsAdapter);




    }



//    @RequiresApi(api = Build.VERSION_CODES.N)


    private void getUserPickupData() {

        DatabaseReference payDb = FirebaseDatabase.getInstance().getReference().child("Users");
        payDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){

                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                        if(dataSnapshot.child("pickups").getChildrenCount()> 0){

                            FetchUserPickupInfo(dataSnapshot.getKey());

                        }
                    }


                }}



            //                        FetchUserPickupInfo(dataSnapshot.getKey());

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void FetchUserPickupInfo(String userId) {


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    for(DataSnapshot dsnForEveryPickup : snapshot.getChildren()){
                        getDataFromDb(userId,dsnForEveryPickup.getKey());
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("pickups");
//
//        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
//                    userIdForIntent = key;
//
//                    for(DataSnapshot ds: snapshot.getChildren()){
//                        //String date = ds.getKey();
//                        //System.out.println(ds.getKey());
//
//                        getDataFromDb(key,ds.getKey());
//
//                    }
//
//
//                }
//
//                else{
//
//                    noUserPickupText.setVisibility(View.VISIBLE);
//                    noUserPickupText.setText("No pickups ordered yet.");
//
//                    //Toast.makeText(UserPickupsActivity.this, "No payments made yet. ", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



    }

    private void getDataFromDb(String keyUser, String keyDate) {


//        date = keyDate;
//        id = keyUser;
        try {
            dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(keyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DatabaseReference ref_for_username = FirebaseDatabase.getInstance().getReference().child("Users").child(keyUser);
        ref_for_username.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    userName = snapshot.child("name").getValue().toString();
                    noOfMeals = snapshot.child("pickups").child(keyDate).child("numberOfMeals").getValue().toString();
                    location = snapshot.child("pickups").child(keyDate).child("location").getValue().toString();
//                    latitudeForIntent = snapshot.child("pickups").child(keyDate).child("location").child("latitude").getValue().toString();
//                    longitudeForIntent = snapshot.child("pickups").child(keyDate).child("location").child("longitude").getValue().toString();

                    UserPickupsObject object = new UserPickupsObject(keyDate,noOfMeals,userName,keyUser,location);
                    resultPickups.add(object);




//
//                    //reverse the list to bring the recent request at the top
//                    Collections.reverse(resultPickups);

                    mPickupsAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Users").child(keyUser).child("pickups").child(keyDate);
//        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    noOfMeals = snapshot.child("numberOfMeals").getValue().toString();
//                    latitudeForIntent = snapshot.child("location").child("latitude").getValue().toString();
//                    longitudeForIntent = snapshot.child("location").child("longitude").getValue().toString();
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });





//        noOfMeals = snapshot.child(date).child("numberOfMeals").getValue().toString();
//        latitudeForIntent = snapshot.child(date).child("location").child("latitude").getValue().toString();
//        longitudeForIntent = snapshot.child(date).child("location").child("longitude").getValue().toString();
//        FinalFetchInfo(key);


    }


//    private void FinalFetchInfo(String key) {
//
//        DatabaseReference dr_for_username = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
//
//
//        dr_for_username.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    userName = snapshot.child("name").getValue().toString();
//
//                    UserPickupsObject object = new UserPickupsObject(date,noOfMeals,userName);
//                    resultPickups.add(object);
//                    mPickupsAdapter.notifyDataSetChanged();
//                    //String date = key1;
//                    // userName = snapshot.child("name").getValue().toString();
//
//
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//    }

    private ArrayList<UserPickupsObject> resultPickups = new ArrayList<UserPickupsObject>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<UserPickupsObject> getDataMatches(){
        return resultPickups;
    }


    @Override
    public void onItemClick(int position) {




        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("Users").child(resultPickups.get(position).getId()).child("pickups").child(resultPickups.get(position).getDate()).child("accepted");

        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(UserPickupsActivity.this, "The request has been accepted/rejected.", Toast.LENGTH_SHORT).show();
                }

                else{
                    Intent in = new Intent(getApplicationContext(),AcceptanceActivity.class);
                    in.putExtra("userId",resultPickups.get(position).getId());
                    in.putExtra("date",resultPickups.get(position).getDate());
//                    in.putExtra("latitude",latitudeForIntent);
//                    in.putExtra("longitude",longitudeForIntent);
                    startActivity(in);
                    finish();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserPickupsActivity.this, "checking error.", Toast.LENGTH_SHORT).show();

            }
        });

//        DatabaseReference dbCheckForAcceptance = FirebaseDatabase.getInstance().getReference().child("Users").child(userIdForIntent).child("pickups").child(date).child("accepted");
//
//        dbCheckForAcceptance.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                        Toast.makeText(UserPickupsActivity.this, "This request has already been accepted/rejected.", Toast.LENGTH_SHORT).show();
//
//
//
//                }
//                else{
//                    Intent intent = new Intent(UserPickupsActivity.this, AcceptanceActivity.class);
//                    intent.putExtra("userId",userIdForIntent);
//                    intent.putExtra("date",date);
//                    intent.putExtra("latitude",latitudeForIntent);
//                    intent.putExtra("longitude",longitudeForIntent);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UserPickupsActivity.this, "Checking error.", Toast.LENGTH_SHORT).show();
//
//            }
//        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserProfile.class));
        finish();
    }
}