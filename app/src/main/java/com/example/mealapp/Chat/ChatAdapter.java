package com.example.mealapp.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<ChatObject> chatObjectArrayList;

    public ChatAdapter(Context context, ArrayList<ChatObject> chatObjectArrayList) {
        this.chatObjectArrayList = chatObjectArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View lv = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lv.setLayoutParams(lp);
        MyviewHolder chatviewHolders = new MyviewHolder(lv);
        return chatviewHolders;

//        LayoutInflater inf = LayoutInflater.from(context);
//        View v = inf.inflate(R.layout.item_chat_view,parent,false);

//        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_view,parent,false);
//        RecyclerView.LayoutParams layPar = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(layPar);
//        MyviewHolder chatViewHolder = new MyviewHolder(view);


//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.item_chat_view,parent,false);

//        return new ChatViewAdapter.MyviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.mMessage.setText(chatObjectArrayList.get(position).getMessage());


        if(chatObjectArrayList.get(position).getCurrentUser()){
            holder.mSemiContainer.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#404040"));
          //  holder.mWrapper.setBackgroundColor(Color.parseColor("#F4F4F4"));
            holder.mWrapper.setBackground(ContextCompat.getDrawable(context, R.drawable.msg_bg_grey));

        }
        else {
            holder.mSemiContainer.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
           // holder.mWrapper.setBackgroundColor(Color.parseColor("#ABC4FF"));
            holder.mWrapper.setBackground(ContextCompat.getDrawable(context, R.drawable.msg_bg_blue));

        }




    }

    @Override
    public int getItemCount() {
        return chatObjectArrayList.size();
    }

    public static class MyviewHolder  extends RecyclerView.ViewHolder {
        public TextView mMessage;
        public LinearLayout mContainer , mSemiContainer , mWrapper;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.msg);
            mContainer = itemView.findViewById(R.id.container);
            mSemiContainer = itemView.findViewById(R.id.semi_container);
            mWrapper = itemView.findViewById(R.id.wrap_message);



        }
    }
}
