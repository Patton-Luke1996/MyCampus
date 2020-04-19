package com.my_campus.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    Boolean formValidity = false;

    private int success = 1, failure = -1, invalid = 0;
    private int myResult = 0;
    Boolean hide = true;
    EditText emailField;
    EditText passwordField;

    ImageButton showhide;

    Intent resetActivity;
    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // TextViews
        emailField = findViewById(R.id.signin_email);
        passwordField = findViewById(R.id.signin_password);


        showhide = findViewById(R.id.showhide_ib);
        showhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hide == true)
                {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide =false;
                }

                else
                {

                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide = true;
                }

            }
        });

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
                            user = mAuth.getCurrentUser();
                            // Change from isNewUser to database lookup to see if a user exits with said uid
                            determineNextActivity();

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

    private void determineNextActivity() {
       if (!user.isEmailVerified()) {
           Toast.makeText(this, "Please verify your email before logging in.", Toast.LENGTH_SHORT).show();
           return;
       }

       final String uid = user.getUid();

        DocumentReference docRef = db.collection("user_profiles").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getString("phoneNumber").matches("")) {
                            myResult = 2;
                            activateNextActivity();
                        } else {
                            myResult = 1;
                            Log.d("LoginActivity", "Document Found with uid: " + uid);
                            activateNextActivity();
                        }

                    } else {
                        myResult = -1;
                        Log.d("LoginActivity", "No such document with uid: " + uid);
                        activateNextActivity();
                    }
                } else {
                    Log.d("LoginActivity", "Get failed with", task.getException());
                    activateNextActivity();
                }

            }
        });


    }

    private void activateNextActivity() {
        if (myResult == -1) {
            nextActivity = new Intent(LoginActivity.this, CreateProfileActivity.class);
            startActivity(nextActivity);
        } else if (myResult == 1){
            nextActivity = new Intent(LoginActivity.this, AppMainActivity.class);
            startActivity(nextActivity);
        } else if (myResult == 0) {
            Toast.makeText(LoginActivity.this, "Something went wrong. Try again.",
                    Toast.LENGTH_SHORT).show();
        } else if (myResult == 2) {
            nextActivity = new Intent(LoginActivity.this, VerifyPhoneNumberActivity.class);
            startActivity(nextActivity);
        }
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
