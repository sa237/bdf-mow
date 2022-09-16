package com.example.mealapp.Acceptances;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealapp.R;

import java.util.ArrayList;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.MyviewHolder> {
    private Context context;
    private ArrayList<ChatViewObject> chatViewObjectArrayList;
    private final ChatRecyclerInterface chatRecyclerInterface;

    public ChatViewAdapter(Context context, ArrayList<ChatViewObject> chatViewObjectArrayList , ChatRecyclerInterface chatRecyclerInterface) {
        this.chatViewObjectArrayList = chatViewObjectArrayList;
        this.context = context;
        this.chatRecyclerInterface = chatRecyclerInterface;

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View lv = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_view,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lv.setLayoutParams(lp);
        MyviewHolder chatviewHolders = new MyviewHolder(lv,chatRecyclerInterface);
         return chatviewHolders;

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewAdapter.MyviewHolder holder, int position) {

        if(chatViewObjectArrayList.get(position).getNumber() != null){
            holder.mNumber.setVisibility(View.VISIBLE);
        }

        holder.mName.setText("Name: " + chatViewObjectArrayList.get(position).getName());
        holder.mNumber.setText("Contact No: " + chatViewObjectArrayList.get(position).getNumber());
        holder.mId.setText("Id: " + chatViewObjectArrayList.get(position).getId());

        holder.mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatRecyclerInterface.onNameClick(position);
            }
        });

        holder.mNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatRecyclerInterface.onChatClick(position);

            }
        });



    }

    @Override
    public int getItemCount() {
        return chatViewObjectArrayList.size();
    }

    public static class MyviewHolder  extends RecyclerView.ViewHolder{
        public TextView mNumber, mName , mId, mChatButton;

        public MyviewHolder(@NonNull View itemView , ChatRecyclerInterface chatRecyclerInterface) {
            super(itemView);

            mNumber = itemView.findViewById(R.id.number_for_chat);
            mName = itemView.findViewById(R.id.name_for_chat);
            mId = itemView.findViewById(R.id.id_for_chat);
            mChatButton = itemView.findViewById(R.id.chatnowbutton);

//            mNumber.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(chatRecyclerInterface != null){
//                        int pos = getAdapterPosition();
//
//                        if(pos != RecyclerView.NO_POSITION){
//                            chatRecyclerInterface.onChatClick(pos);
//                        }
//
//                    }
//                }
//            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(chatRecyclerInterface != null){
//
//                        int pos = getAdapterPosition();
//
//                        if(pos != RecyclerView.NO_POSITION){
//
//                            chatRecyclerInterface.onChatClick(pos);
//
//                        }
//                    }
//                }
//            });

        }


    }
}
