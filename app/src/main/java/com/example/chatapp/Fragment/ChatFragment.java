package com.example.chatapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.adapter.user_adapter;
import com.example.chatapp.model.Users;
import com.example.chatapp.model.chat_list;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    FirebaseUser fuse;
    DatabaseReference reference;
    RecyclerView recyclerView;
    private List<Users> my_users;
    private user_adapter user_Adapter;
    private List<chat_list> userlist;


    public ChatFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerView=view.findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuse= FirebaseAuth.getInstance().getCurrentUser();
        userlist=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chat's List").child(fuse.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userlist.clear();

                //loop of user
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    chat_list chatList=snapshot.getValue(chat_list.class);
                    userlist.add(chatList);
                }
                chatlist();
            }

            private void chatlist()
            {
                // get all previous chats
                my_users=new ArrayList<>();

                reference=FirebaseDatabase.getInstance().getReference("My User's");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                    my_users.clear();

                    for(DataSnapshot snapshot1 :snapshot.getChildren())
                    {
                        Users users=snapshot1.getValue(Users.class);

                        for(chat_list chatList:userlist)
                        {
                            if(users.getId().equals(chatList.getId_u()))
                            {
                                my_users.add(users);
                            }
                        }
                    }
                    user_Adapter=new user_adapter(true, my_users, getContext());

                    recyclerView.setAdapter(user_Adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
        // Inflate the layout for this fragment


    }
}