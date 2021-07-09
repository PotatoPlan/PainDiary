package com.example.paindiaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paindiaryapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    private EditText name, email, password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

// --------------------Initialise the mAuth variable--------------------
        mAuth = FirebaseAuth.getInstance();

        name = binding.name;
        email = binding.email;
        password = binding.password;
        register = binding.register;

// --------------------Call the registerValidation function once the click event happened on the button--------------------
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerValidation();
            }
        });
    }

    private void registerValidation() {
// --------------------Get user's input data, and convert them into Strings.--------------------
        String inputName = name.getText().toString().trim();
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();

// --------------------Validate the user input.--------------------
// --------------------No empty value, match Email format and password is at least 6 digits.--------------------
        if (inputName.isEmpty()) {
            name.setError("Please Enter Your Name");
            name.requestFocus();
            return;
        }

        if (inputEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }

        if (inputPassword.isEmpty() || inputPassword.length() < 6) {
            password.setError("Please Enter a Valid Password (Minimum 6 digits)");
            password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(inputName, inputEmail);

                    String userID = mAuth.getCurrentUser().getUid();

                    FirebaseDatabase.getInstance().getReference("Users").child(userID).setValue(user);
                    Toast.makeText(SignUp.this, "User has been registered.", Toast.LENGTH_LONG).show();

                    // --------------------Once the registration is done, get the fragment, load the activity xml and call the fragment by using getSupportFragmentManager--------------------
                    getSupportFragmentManager().beginTransaction().add(R.id.registration, new RegistrationFinishedFragment()).commit();

                    // --------------------Set original activity's UI elements invisible (This is due to a bad design)--------------------
                    binding.register.setVisibility(View.GONE);
                    binding.name.setVisibility(View.GONE);
                    binding.email.setVisibility(View.GONE);
                    binding.password.setVisibility(View.GONE);
                } else {
                    Toast.makeText(SignUp.this, "User Registration Failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}