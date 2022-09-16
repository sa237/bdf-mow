package com.example.mealapp.Pickup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mealapp.Acceptances.ChatViewObject;
import com.example.mealapp.Constants.AllConstants;
import com.example.mealapp.Main.MainActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserPickup.UserPickupsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class PickupFormActivity extends AppCompatActivity {

    private EditText numberMeals , mFlatNumber, mArea, mTown, mState;

    private Button provideLocation;
    private ArrayList<String> arr;
    private boolean checked;
    private String userId , userName;
    private String title,body,date;
    private DatabaseReference usersDb,currUserDb2,currUserDb1,currUserDb3;
    private String locationFinal , foodItems;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private int numberOfMeals;
    private String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_form);

        numberMeals = (EditText) findViewById(R.id.form_mealnumber);
        mFlatNumber = (EditText) findViewById(R.id.form_flat_number);
        mArea = (EditText) findViewById(R.id.form_area);
        mTown = (EditText) findViewById(R.id.form_town);
        mState = (EditText) findViewById(R.id.form_state);
        provideLocation = (Button) findViewById(R.id.provide_location_btn);
        arr = new ArrayList<String>();
        foodItems = "";
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //stuff related to notifs

        //FirebaseMessaging.getInstance().subscribeToTopic("all");

        //get user's name from the database
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userName = snapshot.child("name").getValue().toString();
                    System.out.println(userName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PickupFormActivity.this, "User error.", Toast.LENGTH_SHORT).show();

            }
        });

        title = "New pickup request.";




        provideLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = numberMeals.getText().toString();
                String flatNumber = mFlatNumber.getText().toString();
                String area = mArea.getText().toString();
                String town = mTown.getText().toString();
                String state = mState.getText().toString();




                try{
                    numberOfMeals = Integer.parseInt(text);
                    if(numberOfMeals < 30){
                        numberMeals.setError("More than 30 meals required.");

                    }


                    if(checked == false){ Toast.makeText(PickupFormActivity.this, "Please select the meals you want to donate.", Toast.LENGTH_SHORT).show(); }

                    if(flatNumber.isEmpty()){
                        mFlatNumber.setError("Please enter the details"); }
                    if(area.isEmpty()){
                        mArea.setError("Please enter the details"); }
                    if(town.isEmpty()){
                        mTown.setError("Please enter the details"); }
                    if(state.isEmpty()){
                        mState.setError("Enter your state");}

                    if(!town.equals("Dehradun")){
                        mTown.setError("Pickups only available for Dehradun."); }

                    else{

                        locationFinal = flatNumber + "," + area + "," + town + "," + state;

                        // add user details in the database
                        addToDbDate();
                        addToDbLocation();
                        addToDbFoods();

                        //send notification to everyone
                        body = userName + " has placed a new pickup request.Tap to view.";
                        getAdminId();


                        //when everything is done

                        AlertDialog.Builder builder = new AlertDialog.Builder(PickupFormActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle("Pickup Request Successful.");
                        builder.setMessage("Your request has been sent to the admin. The request will be accepted/rejected by the admin within the next 10 minutes.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                        });
                        builder.create().show();
//                        AlertDialog dlg = new AlertDialog.Builder(PickupFormActivity.this)
//                                .setTitle("Pickup Request Successful")
//                                .setMessage("Your request has been sent to the admin. The request will be accepted/rejected by the admin within the next 10 minutes.")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        dialogInterface.dismiss();
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                        finish();
//                                    }
//                                }).create();
//                        dlg.show();



                    }

                }catch (NumberFormatException e){
                    numberMeals.setError("Enter the number of servings.");
                } }});




    }

    private void getAdminId() {
        DatabaseReference dbrforAdmin = FirebaseDatabase.getInstance().getReference().child("Users");

        dbrforAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dsnp: snapshot.getChildren()){
                        checkIfAdmin(dsnp.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }}); }

    private void checkIfAdmin(String key) {
        DatabaseReference check = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("admin").getValue().toString().equals("true")){
                        adminId = key;
                        getTok(body,adminId);

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

    private void getTok(String message,String adminId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String token = snapshot.child("token").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();

                try {
                    data.put("title",title);
                    data.put("message",message);
                    data.put("userIdFromIntent",adminId);

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


    private void addToDbFoods() {

        currUserDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date).child("food");
        ArrayList<String> newList = removeDuplicates(arr);
        for(int i=0 ; i < newList.size()-1 ; i++){
            foodItems = foodItems + newList.get(i) + ", ";


            //userOtherInfo.put(arr.get(i) , arr.get(i));
        }
        foodItems  = foodItems + newList.get(newList.size()-1);
        currUserDb2.setValue(foodItems);


        //currUserDb2.setValue(userOtherInfo);
    }

    private ArrayList<String> removeDuplicates(List<String> arr) {
        ArrayList<String> newList = new ArrayList<String>();
        for(String element: arr){
            if(!newList.contains(element)){
                newList.add(element);
            }
        }
        return newList;

    }

    private void addToDbLocation() {

        currUserDb1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date).child("location");

//        Map userInfo = new HashMap<>();
//        userInfo.put("latitude", latitude);
        currUserDb1.setValue(locationFinal);
    }

    private void addToDbDate() {

        //get date to put in database
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpleDateFormat.format(calendar.getTime());
        //String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        currUserDb3 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("pickups").child(date);
        Map userMealInfo = new HashMap<>();
        userMealInfo.put("numberOfMeals",String.valueOf(numberOfMeals));

        currUserDb3.setValue(userMealInfo);
    }

//    private void sendNotif() {
//
//        //checking if the user is an admin
//        DatabaseReference admDb = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        admDb.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
//                    for (DataSnapshot dsc : snapshot.getChildren()) {
//                        getAdmin(dsc.getKey());
//
//                    } }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


//    private void getAdmin(String key) {
//
//
//
//        DatabaseReference dbAd = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
//
//
//
//
//            dbAd.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        if(snapshot.child("admin").getValue().toString().equals("true")){
//
//                            //send through all topic
//                            body = userName + " has sent a pickup request. Tap to view.";
//                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",title,body,PickupFormActivity.this,"ViewRequest");
//                            notificationsSender.SendNotifications();
//
//                        }
//
//                        else{
//                            body = "A new order has been placed!.";
//                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",title,body,PickupFormActivity.this,"Main");
//                            notificationsSender.SendNotifications();
//
//
//
//
//                        }
//
//
//
//
//
//
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }







    






    public void onCheckBoxClicked(View view) {
        checked = ((CheckBox) view).isChecked();

        //do whatever I want to with the selected options
        switch(view.getId()){
            case R.id.cb1_dry_ration:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb1_cooked_food:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_rice:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_pulses:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_rotis:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
            case R.id.cb2_others:
                if(checked){
                    arr.add(((CheckBox) view).getText().toString());
                }
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),ViewMapActivity.class));
    }
}