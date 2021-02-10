package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText userlog;

    EditText passlog;

    Button btnlogin;

    Button btnregister;

    FirebaseAuth auth;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        //Check that User Exist or Not
        if(firebaseUser!=null)
        {
            Intent i=new Intent(Login.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userlog=findViewById(R.id.etuserlogin);

        passlog=findViewById(R.id.etuserpass);

        btnlogin=findViewById(R.id.btnlogin);

        btnregister=findViewById(R.id.btnregister);

        //Using Firebase
        auth=FirebaseAuth.getInstance();



        //SignUp Button

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Login.this,Registeration.class);
                startActivity(i);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext= userlog.getText().toString();

                String passtext=passlog.getText().toString();

                //check that empty or not

                if (TextUtils.isEmpty(passtext)||TextUtils.isEmpty(emailtext))
                {
                    Toast.makeText(Login.this,"Please complete all Options",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.signInWithEmailAndPassword(emailtext,passtext).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent i=new Intent(Login.this,MainActivity.class);

                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(i);

                                finish();
                            }
                            else
                            {
                                Toast.makeText(Login.this,"Login Fail",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}