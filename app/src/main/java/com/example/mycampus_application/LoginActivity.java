package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Boolean formValidity = false;
    Boolean isNewUser = false;

    EditText emailField;
    EditText passwordField;

    Intent resetActivity;
    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // TextViews
        emailField = findViewById(R.id.signin_email);
        passwordField = findViewById(R.id.signin_password);


    }

    private void signIn(String email, String password) {

        //Validate Email
        if (!validateForm()) {
            formValidity = false;
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //sign in successful
                        // detect if user is old or new
                        if (task.isSuccessful()) {

                            // Change from isNewUser to database lookup to see if a user exits with said uid
                            if (isNewUser) {
                                nextActivity = new Intent(LoginActivity.this, CreateProfileActivity.class);
                                startActivity(nextActivity);
                            } else {
                                nextActivity = new Intent(LoginActivity.this, DrawerManager.class);
                                startActivity(nextActivity);
                            }

                        }
                        else {
                            //sign in failed
                            //must add logic to handle this and similar events
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    public void startSignIn (View view) {
        signIn(emailField.getText().toString(), passwordField.getText().toString());

    }

    public void startPasswordReset (View view) {
        resetActivity = new Intent(this, PasswordResetActivity.class);
        startActivity(resetActivity);
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            if (email.endsWith("@umich.edu")) {
                emailField.setError(null);
            } else {
                emailField.setError("Enter a valid umich email address.");
                valid = false;
            }
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }


}
