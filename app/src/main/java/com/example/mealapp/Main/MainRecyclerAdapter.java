package com.example.mealapp.Main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.Donation.DonationActivity;
import com.example.mealapp.Pickup.MealPickupMain;
import com.example.mealapp.R;
import com.example.mealapp.Registration.RegistrationActivity;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {


    private List<MyModel> mainRecyclerList;
    private Context context;

    public MainRecyclerAdapter(List<MyModel> mainRecyclerList,Context context){
        this.mainRecyclerList = mainRecyclerList;
        this.context = context;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.mTitle.setText(mainRecyclerList.get(position).getTitle());
        holder.mDescription.setText(mainRecyclerList.get(position).getDescription());
        holder.mClickableText.setText(mainRecyclerList.get(position).getClickableText());
        holder.mImage.setImageResource(mainRecyclerList.get(position).getImage());


        holder.mClickableText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.mClickableText.getText().equals("Donate meals now")){
                    Intent intent = new Intent(view.getContext(), DonationActivity.class);
                    view.getContext().startActivity(intent);

                }
                else if(holder.mClickableText.getText().equals("Get monthly subscription now")){

                    Toast.makeText(context, "This feature is unavailable for now. ", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
//                    view.getContext().startActivity(intent);

                }

                else if(holder.mClickableText.getText().equals("Set meal pickup now")){

                    Intent intent = new Intent(view.getContext(), MealPickupMain.class);
                    view.getContext().startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mainRecyclerList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{

        private ImageView mImage;
        private TextView mTitle,mDescription,mClickableText;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.imgBanner);
            mTitle = itemView.findViewById(R.id.titleBanner);
            mDescription = itemView.findViewById(R.id.descBanner);
            mClickableText = itemView.findViewById(R.id.clickableTextBanner);



        }
    }
}
