package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText displayName;

    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mAuth = FirebaseAuth.getInstance();

        displayName = findViewById(R.id.createProfile_displayName);

        // Will need to grab all information eventually

    }

    public void startProfileCreation(View view) {
        updateProfile(displayName.getText().toString());

        // Needs boolean logic to handle if everything went through/or if it failed
        nextActivity = new Intent(this, DrawerManager.class);
        startActivity(nextActivity);
    }

    public void updateProfile(String displayName) {
        FirebaseUser user = mAuth.getCurrentUser();

        // Will need to grab all information and add it to a Firebase db with uid = current user as PK
        // Will need to add picture eventually

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateProfileActivity.this, "Profile Updated.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Update Failed, try again
                            // Might need boolean logic here to handle this exception and keep it on the page.
                        }
                    }
                });

    }
}
