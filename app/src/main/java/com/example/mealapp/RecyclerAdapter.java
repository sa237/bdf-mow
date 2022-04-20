package com.example.mealapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.mealapp.Donation.DonationActivity;
import com.example.mealapp.Main.MyModel;
import com.example.mealapp.Pickup.MealPickupMain;
import com.example.mealapp.Registration.RegistrationActivity;

import java.util.ArrayList;

public class RecyclerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<MyModel> modelArrayList;

    public RecyclerAdapter(Context context, ArrayList<MyModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }



    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_item,container,false);

        ImageView imageBanner = view.findViewById(R.id.imgBanner);
        TextView titleBanner = view.findViewById(R.id.titleBanner);
        TextView descBanner = view.findViewById(R.id.descBanner);
        TextView clickTextBanner = view.findViewById(R.id.clickableTextBanner);

        //get data
        MyModel model = modelArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        String clickableText = model.getClickableText();
        int image = model.getImage();

        //set data

        imageBanner.setImageResource(image);
        titleBanner.setText(title);
        descBanner.setText(description);
        clickTextBanner.setText(clickableText);


        clickTextBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clickTextBanner.getText().equals("Donate meals now")){
                    Intent intent = new Intent(view.getContext(), DonationActivity.class);
                    view.getContext().startActivity(intent);

                }
                else if(clickTextBanner.getText().equals("Get monthly subscription now")){

                    Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                    view.getContext().startActivity(intent);

                }

                else if(clickTextBanner.getText().equals("Set meal pickup now")){

                    Intent intent = new Intent(view.getContext(), MealPickupMain.class);
                    view.getContext().startActivity(intent);
                }



            }
        });



//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show();
//            }
//        });

        //add view to container
        container.addView(view,position);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
