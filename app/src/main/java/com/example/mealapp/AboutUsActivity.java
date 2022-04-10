package com.example.mealapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutUsActivity extends AppCompatActivity {

    private Button fb,insta,yt,linkedIn, twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        fb = findViewById(R.id.facebook_btn);
        insta = findViewById(R.id.insta_btn);
        yt = findViewById(R.id.yt_btn);
        linkedIn = findViewById(R.id.linkedin_btn);
        twitter = findViewById(R.id.twitter_btn);


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/bdf.ngo?_rdc=1&_rdr"));
                startActivity(browserIntent);
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/bdf.ngo/"));
                startActivity(browserIntent);

            }
        });

        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCBA3xL-gA42gYJJoSz8EnFQ"));
                startActivity(browserIntent);
            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/buildingdreamsfoundation/"));
                startActivity(browserIntent);
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/bdf_ngo"));
                startActivity(browserIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutUsActivity.this,MainActivity.class));
        finish();
    }
}