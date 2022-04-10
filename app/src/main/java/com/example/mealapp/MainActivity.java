package com.example.mealapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mealapp.Donation.DonationActivity;
import com.example.mealapp.Login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ArrayList<MyModel> modelArrayList;
    private RecyclerAdapter recyclerAdapter;

//    ViewPager viewPager;
//    ArrayList<Integer> images = new ArrayList<>();
//    SliderAdapter sliderAdapter;



//    private FirebaseAuth firebaseAuth;
//    private String userId;
//    private FirebaseUser user;
//    private TextView verifyMsg;
//    private Button resendCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.mainViewPager);
        loadCards();

        //set viewpager change listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                String title = modelArrayList.get(position).getTitle();


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//
//        viewPager = findViewById(R.id.viewPager);
//
//        images.add(R.drawable.pic1);
//        images.add(R.drawable.pic2);
//        images.add(R.drawable.pic3);
//
//        sliderAdapter = new SliderAdapter(this,images);
//        viewPager.setPadding(100,0,100,0);
//        viewPager.setAdapter(sliderAdapter);










//        verifyMsg = findViewById(R.id.verifyMsg);
//        resendCode = findViewById(R.id.resendCode);

//        firebaseAuth = FirebaseAuth.getInstance();
//        userId = firebaseAuth.getCurrentUser().getUid();
//
//        user = firebaseAuth.getCurrentUser();

//        if(!user.isEmailVerified()){
//            verifyMsg.setVisibility(View.VISIBLE);
//            resendCode.setVisibility(View.VISIBLE);
//
//            resendCode.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(MainActivity.this,"Verification Email Sent.",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//                            finish();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//
//                }
//            });
//
//
//
//        }







    }

    private void loadCards() {
        modelArrayList = new ArrayList<>();

        modelArrayList.add(new MyModel("Donate Money for Meals",
                "Thousands of people in India go to sleep hungry everyday.Help them by donating only Rs.60 for a basic meal per person.",
                "Donate meals now",
                R.drawable.pic1));

        modelArrayList.add(new MyModel("Subscribe Monthly",
                "Get a basic monthly subscription to be regular with your donations.",
                "Get monthly subscription now",
                R.drawable.pic2));

        modelArrayList.add(new MyModel("Leftover Meal Pickup",
                "Threw a party and have leftover food? Make someone's day by donating the food to us. We'll make sure to deliver it fresh.",
                "Set meal pickup now",
                R.drawable.pic3));

        recyclerAdapter = new RecyclerAdapter(this,modelArrayList);
        viewPager.setAdapter(recyclerAdapter);
        //set default padding
        viewPager.setPadding(100,0,100,0);



    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if(doubleBackToExitPressedOnce){
            finishAffinity();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(),"Please click BACK again to exit",Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },2000);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.user_profile){
            startActivity(new Intent(MainActivity.this,UserProfile.class));
            finish();
        }

        if(id == R.id.donate_meal_menu){
            startActivity(new Intent(MainActivity.this, DonationActivity.class));
            finish();
        }

        if(id == R.id.about_us_menu){

            startActivity(new Intent(MainActivity.this,AboutUsActivity.class));
            finish();
        }

        if(id == R.id.share_menu){

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Share the app with your friends and family.";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share now");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));


        }



        if(id == R.id.user_logout){

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }

        return true;




    }
}