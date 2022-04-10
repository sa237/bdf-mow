package com.example.mealapp.Donation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {

    private List<PaymentObject> paymentObjectList;
    private Context context;

    public PaymentAdapter(List<PaymentObject> paymentObjectList, Context context) {
        this.paymentObjectList = paymentObjectList;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        PaymentViewHolder pvh = new PaymentViewHolder(layoutView);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {

        holder.mDate.setText("Date:  " + paymentObjectList.get(position).getDate());
        holder.mMoney.setText("Amount:  "+ paymentObjectList.get(position).getAmount());
        holder.mID.setText("Payment ID:  " + paymentObjectList.get(position).getPaymentID());







    }

    @Override
    public int getItemCount() {
        return paymentObjectList.size();
    }
}
