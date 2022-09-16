package com.example.mealapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int image[] = {

            R.drawable.frontslider1,
            R.drawable.frontslider2,
            R.drawable.frontslider3

    };

    int heading1[] = {

            R.string.heading_one_a,
            R.string.heading_one_b,
            R.string.heading_one_c




    };

    int heading2[] = {
            R.string.heading_two_a,
            R.string.heading_two_b,
            R.string.heading_two_c

    };

    int description[] = {

            R.string.desc_one,
            R.string.desc_two,
            R.string.desc_three

    };

    public  ViewPagerAdapter(Context context){
        this.context = context;
    }




    @Override
    public int getCount() {
        return description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);
        TextView slideTitle1 = (TextView) view.findViewById(R.id.titleText_pt1);
        TextView slideTitle2 = (TextView) view.findViewById(R.id.titleText_pt2);
        TextView slideDescription = (TextView) view.findViewById(R.id.descText);

        slideTitleImage.setImageResource(image[position]);
        slideTitleImage.setClipToOutline(true);
        slideTitle1.setText(heading1[position]);
        slideTitle2.setText(heading2[position]);
        slideDescription.setText(description[position]);

        container.addView(view);

        return view;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
