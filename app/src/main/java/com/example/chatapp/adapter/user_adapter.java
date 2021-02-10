package com.example.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.model.Users;

import java.util.List;

public class user_adapter extends RecyclerView.Adapter<user_adapter.ViewHolder> {


    private boolean is_chat;

    private List<Users> myUsers;
    private Context context;

    //calling contructor


    public user_adapter(boolean is_chat,List<Users> myUsers, Context context) {
        this.is_chat=is_chat;
        this.myUsers = myUsers;

        this.context = context;
    }

    public user_adapter(List<Users> myUsers, Context context, List<Users> myUsers1, boolean b) {

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);

        return new user_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Users users=myUsers.get(position);

        holder.username.setText(users.getUsername());
         if(users.getImageurl().equals("defolt"))
         {
             holder.imageView.setImageResource(R.mipmap.ic_launcher);
         }
         else
         {
             Glide.with(context).load(users.getImageurl()).into(holder.imageView);
         }

         //checking status
        if(is_chat) {
            if (users.getV_status().equals("on_line")) {
                holder.image_on.setVisibility(View.VISIBLE);
                holder.image_off.setVisibility(View.GONE);
            } else {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.VISIBLE);
            }
        }
            else
            {
                holder.image_on.setVisibility(View.GONE);
                holder.image_off.setVisibility(View.GONE);
            }


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent j= new Intent(context,Message.class);
                 j.putExtra("User Id",users.getId());
                 context.startActivity(j);
             }
         });

    }

    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public TextView username;
        //for showing user status online/offline
        public ImageView image_on;
        public ImageView  image_off;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.ivimage);
            username=itemView.findViewById(R.id.etname);

            image_on=itemView.findViewById(R.id.iv_status_on);
            image_off=itemView.findViewById(R.id.iv_status_off);
        }
    }
}
