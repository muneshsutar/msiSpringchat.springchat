package com.msiSpringchat.springchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.msiSpringchat.springchat.R;

public class ReagesterActivity extends AppCompatActivity {

    AppCompatButton login;
    ImageButton reagbtn;

    EditText reageemail,reagepassword;

    ProgressBar RegProgressbar;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reagester);

        login = findViewById(R.id.login);
        reagbtn = findViewById(R.id.reagbtn);

        reageemail = findViewById(R.id.reageemail);
        reagepassword = findViewById(R.id.reagepassword);
        RegProgressbar  = findViewById(R.id.RegProgressbar);


        firebaseAuth = FirebaseAuth.getInstance();

        reagbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userreagestration();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReagesterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();


            }
        });

    }

    private void userreagestration() {
        RegProgressbar.setVisibility(View.VISIBLE);
        String email = reageemail.getText().toString();
        String password = reagepassword.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!password.isEmpty() && !(password.length() <6)){
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Registration Successfully, Please Update Your Details ", Toast.LENGTH_SHORT).show();
                                    RegProgressbar.setVisibility(View.GONE);
                                    Intent userreagestrationi = new Intent(ReagesterActivity.this,UpdateDeitlsActivity.class);
                                    startActivity(userreagestrationi);
                                    finish();

                                }else {
                                    Toast.makeText(getApplicationContext(), "Please Use Different Gmail Account ", Toast.LENGTH_SHORT).show();
                                    RegProgressbar.setVisibility(View.GONE);


                                }

                            }
                        });

            }else if (password.isEmpty()){
                RegProgressbar.setVisibility(View.GONE);
                reagepassword.setError("Empty Fields Are not Allowed");
            }else {
                RegProgressbar.setVisibility(View.GONE);
                reagepassword.setError("Please Provide 6 Digit Password");
            }
        }else if (email.isEmpty()){
            RegProgressbar.setVisibility(View.GONE);
            reageemail.setError("Empty Fields Are not Allowed");
        }else {
            RegProgressbar.setVisibility(View.GONE);
            reageemail.setError("Please Provide Correct Gmail");
        }
    }
}