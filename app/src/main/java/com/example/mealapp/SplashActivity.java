package com.example.mealapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import  androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    ImageView bg,logo,layout_pic,logo_small;
    TextView appName,appText,comp_name;
    LottieAnimationView lottieAnimationView;

    private static final int NUM_PAGES = 3;
    //private ViewPager2 viewPager;
    //private ScreenSlidePageAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //getActionBar().hide();

        Typeface tf1 = Typeface.createFromAsset(getAssets(),"font/EBGaramond-Regular.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(),"font/Lobster-Regular.ttf");

        bg = findViewById(R.id.splash_bg);
        logo = findViewById(R.id.logo);
        logo_small = findViewById(R.id.logo_second);
        appText = findViewById(R.id.text_random);
//        layout_pic = findViewById(R.id.small_bg);
//        appText = findViewById(R.id.frontText);
        appName = findViewById(R.id.app_name);
        appName.setTypeface(tf2);
        comp_name = findViewById(R.id.comp_name);
        comp_name.setTypeface(tf1);

//        viewPager = findViewById(R.id.pager);
//        pagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);

        bg.animate().translationY(-2600).setDuration(1000).setStartDelay(4000);
        logo.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
//        layout_pic.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
//        appText.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
        appName.animate().translationY(2000).setDuration(1000).setStartDelay(4000);
        appText.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
        logo_small.animate().translationY(1800).setDuration(1000).setStartDelay(4000);
        comp_name.animate().translationY(1800).setDuration(1000).setStartDelay(4000);

        //startActivity(new Intent(getApplicationContext(),MainActivity.class));

       // Handler().postDelayed({startActivity(Intent(this, MainActivity::class.java))}, 3000);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),OnboardingMainActivity.class));
                //Do something after 100ms
            }
        }, 6000);






    }
    //getSupportFragmentManager()




//    private class ScreenSlidePageAdapter  extends FragmentStateAdapter{
//
//
//
//
//        public ScreenSlidePageAdapter(@NonNull FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//
////        public ScreenSlidePageAdapter(@NonNull FragmentManager fragmentManager) {
////            super(fragmentManager);
////        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//
//            switch (position){
//                case 0:
//                    OnBoardingFragment1 tab1 = new OnBoardingFragment1();
//                    return tab1;
//                case 1:
//                    OnBoardingFragment2 tab2 = new OnBoardingFragment2();
//                    return tab2;
//
//                case 2:
//                    OnBoardingFragment3 tab3 = new OnBoardingFragment3();
//                    return tab3;
//            }
//
//            return null;
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return NUM_PAGES;
//        }
//    }
}