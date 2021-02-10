package com.example.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.model.Chatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class message_adapter extends RecyclerView.Adapter<message_adapter.ViewHolder> {


    private List<Chatting> myChat;
    private Context context;

    private String image_url;

    public static final int msq_right=1;
    public static final int msg_left=0;

    //using Firebase
    FirebaseUser fuse;

    //calling contructor


    public message_adapter(List<Chatting> myChat, Context context,String image_url) {
        this.myChat = myChat;
        this.context = context;
        this.image_url=image_url;
    }


    @NonNull
    @Override
    public message_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==msg_left)
        {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_right,parent,false);

        return new message_adapter.ViewHolder(view);
        }

    else
    {
        View view=LayoutInflater.from(context).inflate(R.layout.chat_left,parent,false);

        return new message_adapter.ViewHolder(view);
    }}

    @Override
    public void onBindViewHolder(@NonNull message_adapter.ViewHolder holder, int position) {

        Chatting chatting=myChat.get(position);
        holder.show_msg.setText(chatting.getMessag());
        if(image_url.equals("defolt"))
        {
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(context).load(image_url).into(holder.profile_img);
        }

        if(position==myChat.size()-1)
        {
            if(chatting.isIs_seen())
            {
                holder.seen_text.setText("seen");
            }
            else
            {
                holder.seen_text.setText("delivered");
            }
        }
        else
        {
            holder.seen_text.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return myChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView profile_img;
        public TextView show_msg;
        public TextView seen_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img= itemView.findViewById(R.id.image_profile);
            show_msg=itemView.findViewById(R.id.show_msg);
            seen_text=itemView.findViewById(R.id.tv_seen_status);
        }
    }
    @Override
    public int getItemViewType(int position)
    {
        fuse= FirebaseAuth.getInstance().getCurrentUser();

        if(myChat.get(position).getSendr().equals(fuse.getUid()))
        {
            return msq_right;
        }
        else
        {
            return msg_left;
        }
    }
}
