package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mAuth = FirebaseAuth.getInstance();
    }

    public void startPasswordReset(View view) {
        sendPasswordReset();
        nextActivity = new Intent(this, LoginActivity.class);
        startActivity(nextActivity);
    }

    private void sendPasswordReset() {
        EditText emailField = findViewById(R.id.passwordReset_email);

        mAuth.sendPasswordResetEmail(emailField.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Successful Email Link
                            Toast.makeText(PasswordResetActivity.this, "Verification email sent to ",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PasswordResetActivity.this, "Password reset email failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
