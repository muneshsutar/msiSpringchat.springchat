package com.msiSpringchat.springchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.msiSpringchat.springchat.R;

public class ForgotPassword extends AppCompatActivity {

    TextView alertmessages;
    ImageView backArrow;

    EditText inputGmail;
    ImageButton forgotbtn;

    ProgressBar progress;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputGmail = findViewById(R.id.inputGmail);
        forgotbtn = findViewById(R.id.forgotbtn);
        progress = findViewById(R.id.progress);
        alertmessages = findViewById(R.id.alertmessages);
        backArrow = findViewById(R.id.backArrow);
        firebaseAuth = FirebaseAuth.getInstance();

        backArrow.setOnClickListener(v -> {
            Intent backi = new Intent(ForgotPassword.this, LoginActivity.class);
            startActivity(backi);
            finish();
        });


        forgotbtn.setOnClickListener(v -> {



            progress.setVisibility(View.VISIBLE);
            String email = inputGmail.getText().toString();
            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){



                        alertmessages.setText("We have sent you instructions to reset your password!");
                        progress.setVisibility(View.GONE);
                        alertmessages.setTextColor(Color.RED);

                    }else {



                        alertmessages.setText("Failed to send reset email! And Try Again");
                        progress.setVisibility(View.GONE);
                        alertmessages.setTextColor(Color.RED);


                    }

                });



            }else if (email.isEmpty()){
                progress.setVisibility(View.GONE);
                inputGmail.setError("Empty Fields Are not Allowed");

            }else {
                progress.setVisibility(View.GONE);
                inputGmail.setError("Please Enter Correct Gmail");

            }
        });

    }
}