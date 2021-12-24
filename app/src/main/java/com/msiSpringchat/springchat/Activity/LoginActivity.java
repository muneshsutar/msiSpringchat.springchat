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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.msiSpringchat.springchat.R;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton registration;
    TextView forgotPassword;
    ProgressBar logProgressbar;
    ImageButton signin;

    EditText loginemail,loginpassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registration = findViewById(R.id.registration);

        forgotPassword = findViewById(R.id.forgotPassword);
        signin = findViewById(R.id.signin);
        logProgressbar = findViewById(R.id.logProgressbar);

        loginemail = findViewById(R.id.loginemail);
        loginpassword = findViewById(R.id.loginpassword);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() !=null){
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logProgressbar.setVisibility(View.VISIBLE);
                Setvalidation();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iForgotPassword = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(iForgotPassword);


            }
        });


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iReagesterActivity = new Intent(LoginActivity.this,ReagesterActivity.class);
                startActivity(iReagesterActivity);
                finish();
            }
        });


    }

    private void Setvalidation() {

        String email = loginemail.getText().toString().trim();
        String password = loginpassword.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if (!password.isEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Login SuccessesFully", Toast.LENGTH_SHORT).show();
                            logProgressbar.setVisibility(View.GONE);
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                            logProgressbar.setVisibility(View.GONE);
                        }



                    }
                });
            }else {
                logProgressbar.setVisibility(View.GONE);
                loginpassword.setError("Empty Fields Are not Allowed");
            }

        }else if (email.isEmpty()){
            logProgressbar.setVisibility(View.GONE);
            loginemail.setError("Empty Fields Are not Allowed");
        }else {
            logProgressbar.setVisibility(View.GONE);
            loginemail.setError("Please Enter Correct Gmail");
        }
    }
}