package com.example.chatapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.adapter.message_adapter;
import com.example.chatapp.model.Chatting;
import com.example.chatapp.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {

    TextView username;

    ImageView imageView;


    //using firebase
    FirebaseUser fuse;
    Intent intent;
    DatabaseReference reference;

    //using recycler view

    RecyclerView recyclerView;
    ImageButton sendbtn;
    EditText msget;

    //for chats
    message_adapter messageadapter;
    List<Chatting> mychat;
    String user_id;
    RecyclerView recyclerViewc;

    ValueEventListener seen_listner;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        username=findViewById(R.id.username);
        imageView=findViewById(R.id.image_profile);
//sending messaegs
        sendbtn=findViewById(R.id.btnSend);
        msget=findViewById(R.id.send_text);

        recyclerViewc=findViewById(R.id.reclerview);

        recyclerViewc.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());

        linearLayoutManager.setStackFromEnd(true);

        recyclerViewc.setLayoutManager(linearLayoutManager);

        //using toolbar & sending messaegs

//        Toolbar toolbar=findViewById(R.id.toolbar);
 //       getSupportActionBar().setTitle("");
  //      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   //     setSupportActionBar(toolbar);
    //    toolbar.setNavigationOnClickListener(new View.OnClickListener() {

 //       });

        intent=getIntent();

        user_id=intent.getStringExtra("User Id");

        fuse= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("My Users").child(user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user=dataSnapshot.getValue(Users.class);

                username.setText(user.getUsername());

                if(user.getImageurl().equals("defolt"))
                {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {

                    Glide.with(Message.this).load(user.getImageurl()).into(imageView);
                }

                read_msg(fuse.getUid(),user_id,user.getImageurl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        sendbtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                String message=msget.getText().toString();

                if(!message.equals(""))
                {
                    sendMessage(fuse.getUid(),user_id,message);

                }
                else
                {
                    Toast.makeText(Message.this,"Don't send Empty Message",Toast.LENGTH_SHORT).show();

                }

                msget.setText("");

            }
        });

        seen_message(user_id);



    }
    //chat seen method
    private void seen_message(final String user_id)
    {
        reference=FirebaseDatabase.getInstance().getReference("Chat's");
        seen_listner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Chatting chatting=snapshot1.getValue(Chatting.class);

                    if(chatting.getSendr().equals(user_id)&&chatting.getReceivr().equals(fuse.getUid()))
                    {
                        HashMap<String,Object> hashMap=new HashMap<>();

                        hashMap.put("is_seen",true);

                        snapshot1.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
    //setting icon and action bar

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }
    private void sendMessage(String sendr, String receivr,String messag)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap
                .put("sender",sendr);
        hashMap
        .put("message",messag);
        hashMap
                .put("receiver",receivr);
        hashMap.put("is_seen",false);

        reference.child("Chats")
                .push()
                .setValue(hashMap);


        //add user to chat frag

        final DatabaseReference chat_ref=FirebaseDatabase.getInstance().getReference("Chat's List").child(fuse.getUid()).child(user_id);
        chat_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chat_ref.child("ID").setValue(user_id);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void read_msg(final String my_id, final String imgage_url, final String user_id)

    {
        mychat=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Chat's");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mychat.clear();

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                 Chatting chatting=snapshot1.getValue(Chatting.class);

                 if(chatting.getSendr().equals(my_id)&&chatting.getReceivr().equals(user_id)||chatting.getReceivr().equals(user_id) && chatting.getSendr().equals(my_id))
                 {
                     mychat.add(chatting);
                 }
                 messageadapter=new message_adapter(mychat, Message.this, imgage_url);
                recyclerViewc.setAdapter(messageadapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        reference.removeEventListener(seen_listner);
        check_status("Off_line");
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        check_status("On_line");

    }

    private void check_status(String st)
    {
        reference=FirebaseDatabase.getInstance().getReference("My_User's").child(fuse.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Status",st);

        reference.updateChildren(hashMap);
    }

}