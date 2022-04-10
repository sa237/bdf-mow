package com.example.mealapp.Donation;

import android.os.TestLooperManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import org.w3c.dom.Text;

public class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mDate,mMoney,mID;



    public PaymentViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        mDate = (TextView) itemView.findViewById(R.id.hist_date);
        mMoney = (TextView) itemView.findViewById(R.id.hist_amount);
        mID = (TextView) itemView.findViewById(R.id.hist_payment_id);


    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(),"You are viewing a past payment.",Toast.LENGTH_SHORT).show();


    }
}
