package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.chatapp.Fragment.ChatFragment;
import com.example.chatapp.Fragment.Profile_Fragment;
import com.example.chatapp.Fragment.UserFragment;
import com.example.chatapp.model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Using Firebase

    DatabaseReference myReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        myReference= FirebaseDatabase.getInstance().getReference("My_User's").child(firebaseUser.getUid());

        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //working on Viewpager ,TabLayout

        ViewPager viewPager=findViewById(R.id.viewpager);

        TabLayout tabLayout=findViewById(R.id.tablayout);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        
        viewPagerAdapter.add_fragment(new ChatFragment(),"Chat's");
        
        viewPagerAdapter.add_fragment(new UserFragment(),"User's");

        viewPagerAdapter.add_fragment(new Profile_Fragment(),"Profile's");
        
        viewPager.setAdapter(viewPagerAdapter);
        
        tabLayout.setupWithViewPager(viewPager);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.logout:

            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(MainActivity.this,Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));



            return true;
        }

        return false;
    }

    // Create ViewPager Adapter

    class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private ArrayList<String> titles;

        private ArrayList<Fragment> fragments;

        ViewPagerAdapter(FragmentManager f_m)
        {
            super(f_m);

            this.titles=new ArrayList<>();

            this.fragments=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            return fragments.get(position);

        }



        public void add_fragment(Fragment fragment,String title)
        {
            titles.add(title);

            fragments.add(fragment);

        }
        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }




    }

    @Override
    protected void onPause()
    {
        super.onPause();
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
        myReference=FirebaseDatabase.getInstance().getReference("My_User's").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Status",st);

        myReference.updateChildren(hashMap);
    }
}