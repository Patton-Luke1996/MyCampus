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

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText emailField;
    EditText passwordField;
    EditText verifyPasswordField;

    Intent nextActivity;

    Boolean formValidity = false;

    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.signup_email);
        passwordField = findViewById(R.id.signup_password);
        verifyPasswordField = findViewById(R.id.signup_confirmpassword);

    }

    public void signUp(String email, String password) {

        //Validate Email
        if (!validateForm()) {
            formValidity = false;
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            sendEmailVerification();

                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void startSignUp(View view) {
        signUp(emailField.getText().toString(), passwordField.getText().toString());
        if(!formValidity){
            nextActivity = new Intent(this, LoginActivity.class);
            startActivity(nextActivity);
        }

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
        String verifyPassword = verifyPasswordField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            if(!password.equals(verifyPassword)) {
                passwordField.setError("Passwords must match!");
                valid = false;
            } else {
                passwordField.setError(null);
            }
        }

        return valid;
    }

    private void sendEmailVerification() {
        findViewById(R.id.send_passwordreset_btn).setEnabled(false);

        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d("SigUpActivity", "Verification email send to " + user.getEmail());
                            Toast.makeText(SignUpActivity.this, "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
