package com.example.mealapp.UserPickup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserPickupsAdapter extends RecyclerView.Adapter<UserPickupsAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    private List<UserPickupsObject> userPickupsObjectList;
    private Context context;

    public UserPickupsAdapter(List<UserPickupsObject> userPickupsObjectList, Context context,RecyclerViewInterface recyclerViewInterface) {
        this.userPickupsObjectList = userPickupsObjectList;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_pickup,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layView.setLayoutParams(layoutParams);
        MyViewHolder userPickupsViewHolders = new MyViewHolder(layView,recyclerViewInterface);
        return  userPickupsViewHolders;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //final UserPickupsObject modelItems = userPickupsObjectList.get(position);
        //Collections.sort(userPickupsObjectList);



        holder.mDates.setText("Date: " + userPickupsObjectList.get(position).getDate());
        holder.mFood.setText("Username: "+ userPickupsObjectList.get(position).getFood());
        holder.mNoOfMeals.setText("Number of Meals: " + userPickupsObjectList.get(position).getNoOfMeals());
        holder.mFoodItems.setText("Food Items: " + userPickupsObjectList.get(position).getFoodItems());
        holder.mId.setText("UserId: " + userPickupsObjectList.get(position).getId());
        holder.mLocation.setText("Location: " + userPickupsObjectList.get(position).getLocation());



    }

    @Override
    public int getItemCount() {
        return userPickupsObjectList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mDates,mFood,mNoOfMeals, mId,mFoodItems,mLocation;

        public MyViewHolder(@NonNull View itemView,RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            mDates = (TextView) itemView.findViewById(R.id.user_pickup_date);
            mFood = (TextView) itemView.findViewById(R.id.user_pickup_food);
            mNoOfMeals = (TextView) itemView.findViewById(R.id.user_pickup_number_meals);
            mFoodItems = (TextView) itemView.findViewById(R.id.user_pickup_food_items);
            mId = (TextView) itemView.findViewById(R.id.user_userId);
            mLocation = (TextView) itemView.findViewById(R.id.user_location);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }





}


