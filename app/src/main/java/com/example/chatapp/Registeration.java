package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registeration extends AppCompatActivity {

    Button registerbtn;
    EditText emailet,passet,useret;

    FirebaseAuth auth;
    DatabaseReference myreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        auth= FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_t=useret.getText().toString();
                String pass_t=passet.getText().toString();
                String email_t=emailet.getText().toString();
                if(TextUtils.isEmpty(username_t)||TextUtils.isEmpty(pass_t)||TextUtils.isEmpty(email_t))
                {
                    Toast.makeText(Registeration.this,"fill all options",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registernow(username_t,pass_t,email_t);
                }


            }
        });
    }
    private void registernow(final String username,String password, String email)
    {
        auth.createUserWithEmailAndPassword(password,email).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser=auth.getCurrentUser();
                    String userid=firebaseUser.getUid();

                    myreference= FirebaseDatabase.getInstance().getReference("myuserd").child(userid);

                    HashMap<String,String> hashMap=new HashMap<>();

                    hashMap.put("id",userid);

                    hashMap.put("username",username);

                    hashMap.put("imageurl","defolt");

                    hashMap.put("Status","Off_line");
                    //main activity opening afer register

                    myreference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent i=new Intent(Registeration.this,MainActivity.class);

                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(i);

                                finish();
                            }
                        }
                    });
                }

                else
                {
                    Toast.makeText(Registeration.this,"incorrect Password & Email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}