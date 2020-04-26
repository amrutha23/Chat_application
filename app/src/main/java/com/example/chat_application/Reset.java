package com.example.chat_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;


public class Reset extends AppCompatActivity {
    MaterialEditText sent_mail;
    Button breset;
    FirebaseAuth firebaseAuth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sent_mail = findViewById(R.id.sent_mail);
        breset = findViewById(R.id.Button_reset);
        firebaseAuth = FirebaseAuth.getInstance();
        breset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = sent_mail.getText().toString();
                if(email.equals("")){
                    Toast.makeText(Reset.this, "Enter your email id", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Reset.this, "Please check your email id", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Reset.this,Login.class));
                            }
                            else{
                                String error = task.getException().getMessage();
                                Toast.makeText(Reset.this, "error", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }
}
