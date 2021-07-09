package com.example.paindiaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.paindiaryapp.databinding.ActivityAuthenticationBinding;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication extends AppCompatActivity {
    private ActivityAuthenticationBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

// --------------------Click on Sign-Up, lead to a new page (activity).--------------------
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Authentication.this, SignUp.class));
            }
        });

// --------------------If there is a user logged in, then we want to get back to the main activity.--------------------
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
// --------------------When the user clicked on the Login button, call the userSignIn method--------------------
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {userSignIn();}
        });
    }

    private void userSignIn() {
        String email = binding.editTextEmailAddress.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
// --------------------Validation on user's input email and password--------------------
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextEmailAddress.setError("You need to input a correct Email address");
            binding.editTextEmailAddress.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            binding.editTextPassword.setError("You need to input a correct password");
            binding.editTextPassword.requestFocus();
            return;
        }
// --------------------Sign the user in if the email and password pass the validation and it's a registered user--------------------
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                  Take the user to the main activity
                    startActivity(new Intent(Authentication.this, MainActivity.class));
                } else {
                    Toast.makeText(Authentication.this, "Email or password does not exist", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}