package com.example.mealapp.Main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import java.util.List;

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.SponsorViewHolder> {

    private List<Integer> imageList;

    public SponsorAdapter(List<Integer> imageList){
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public SponsorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsorcard,parent,false);
        return new SponsorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SponsorViewHolder holder, int position) {
        holder.mImageView.setImageResource(imageList.get(position));


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class SponsorViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;

        public SponsorViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.sponsor_images);
        }
    }
}
