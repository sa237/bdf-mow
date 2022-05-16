package com.example.mealapp.UserPickup;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import org.w3c.dom.Text;

public class UserPickupsViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mDates,mFood,mNoOfMeals;
    public UserPickupsViewHolders(@NonNull View itemView) {
        super(itemView);

        //itemView.setOnClickListener(this);

        mDates = (TextView) itemView.findViewById(R.id.user_pickup_date);
        mFood = (TextView) itemView.findViewById(R.id.user_pickup_food);
        mNoOfMeals = (TextView) itemView.findViewById(R.id.user_pickup_number_meals);




    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(),"You are viewing a past pickup.",Toast.LENGTH_SHORT).show();


    }
}
