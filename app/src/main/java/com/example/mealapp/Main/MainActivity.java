package com.example.mealapp.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.mealapp.AboutUsActivity;
import com.example.mealapp.Acceptances.ChatsViewActivity;
import com.example.mealapp.Donation.DonationActivity;
import com.example.mealapp.Login.LoginActivity;
import com.example.mealapp.Pickup.UserLocationActivity;
import com.example.mealapp.R;
import com.example.mealapp.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //private ViewPager viewPager;
    private ArrayList<MyModel> modelArrayList;
    //private RecyclerAdapter recyclerAdapter;

    private RecyclerView optionsRecyclerView,sponsorRecyclerView;
    private boolean isPermissionGranted;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private ScrollView scrollView;






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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar  = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.main_scroll);


        //check if the email is verified and then make the items visible

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null){

            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                navigationView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(this, "Email Not verified!", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            navigationView.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);

        }



        //toolbar
        setSupportActionBar(toolbar);

        //navigation
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.user_profile);






        //optionsRecyclerView
        optionsRecyclerView = findViewById(R.id.recycler_view1);
        optionsRecyclerView.setHasFixedSize(true);
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        //sponsorRecyclerView
        sponsorRecyclerView = findViewById(R.id.recycler_view2);
        sponsorRecyclerView.setHasFixedSize(true);
        sponsorRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        //checkLocPermission();




        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.sponsor1);
        imageList.add(R.drawable.sponsor2);
        imageList.add(R.drawable.sponsor3);
        imageList.add(R.drawable.sponsor4);
        imageList.add(R.drawable.sponsor5);
        imageList.add(R.drawable.sponsor6);
        imageList.add(R.drawable.sponsor7);


        SponsorAdapter sponsorAdapter = new SponsorAdapter(imageList);
        sponsorRecyclerView.setAdapter(sponsorAdapter);











        loadCards();






        //Recycler view

//        viewPager = findViewById(R.id.mainViewPager);
//        loadCards();
//
//        //set viewpager change listener
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                String title = modelArrayList.get(position).getTitle();
//
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });








    }

//    private void checkLocPermission() {
//
//        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                //Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
//                isPermissionGranted = true;
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), "");
//                intent.setData(uri);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                permissionToken.continuePermissionRequest();
//
//
//            }
//        }).check();
//    }


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

//        recyclerAdapter = new RecyclerAdapter(this,modelArrayList);
//        viewPager.setAdapter(recyclerAdapter);
//        //set default padding
//        viewPager.setPadding(100,0,100,0);

        MainRecyclerAdapter mainRecyclerAdapter = new MainRecyclerAdapter(modelArrayList,this);
        optionsRecyclerView.setAdapter(mainRecyclerAdapter);





    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else{

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
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
//
//        return true;
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.user_profile){
//            startActivity(new Intent(MainActivity.this, UserProfile.class));
//            finish();
//        }
//
//        if(id == R.id.donate_meal_menu){
//            startActivity(new Intent(MainActivity.this, DonationActivity.class));
//            finish();
//        }
//
//        if(id == R.id.about_us_menu){
//
//            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
//            finish();
//        }
//
//        if(id == R.id.share_menu){
//
//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            String shareBody = "Share the app with your friends and family.";
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share now");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//
//
//        }
//
//
//
//        if(id == R.id.user_logout){
//
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            finish();
//
//        }
//
//
//        return true;
//
//
//
//
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.user_profile){
            startActivity(new Intent(MainActivity.this, UserProfile.class));
            finish();
        }
        if (id == R.id.chat){
            startActivity(new Intent(MainActivity.this, ChatsViewActivity.class));
            finish();
        }

        if(id == R.id.donate_meal_menu){
            startActivity(new Intent(MainActivity.this, DonationActivity.class));
            finish();
        }

        if(id == R.id.about_us_menu){

            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            finish();
        }

        if(id == R.id.privacy_policy_menu){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("file:///C:/Users/ASUS/Downloads/PRIVACY-NOTICE.html"));
            startActivity(browserIntent);
            
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
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}