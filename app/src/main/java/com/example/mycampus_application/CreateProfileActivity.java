package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    EditText displayName_ET;
    EditText firstName_ET;
    EditText lastName_ET;
    Spinner spinner;

    String institutionName = "";
    String firstName = "";
    String lastName = "";
    String displayName = "";
    String uid = "";

    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        displayName_ET = findViewById(R.id.createProfile_displayName);
        firstName_ET = findViewById(R.id.createProfile_firstName);
        lastName_ET = findViewById(R.id.createProfile_lastName);

        spinner = findViewById(R.id.createProfile_institutionDropDown);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("University of Michigan - Dearborn");
        arrayList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        // Will need to grab all information eventually

    }

    public void startProfileCreation(View view) {
        grabAllInfo();
    }

    public void grabAllInfo() {

        firstName = firstName_ET.getText().toString();
        lastName = lastName_ET.getText().toString();
        displayName = displayName_ET.getText().toString();
        institutionName = spinner.getSelectedItem().toString();

        //check if empty

        if(firstName.matches("")) {
            Toast.makeText(this, "You did not enter a first name!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (lastName.matches("")) {
                Toast.makeText(this, "You did not enter a last name!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (displayName.matches("")) {
                    Toast.makeText(this, "You did not enter a display name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (institutionName.matches("")) {
                        Toast.makeText(this, "You did not enter an institution!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        uid = user.getUid();
                        makeNewDoc();
                    }

                }
            }
        }


    }

    private void makeNewDoc() {


        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("display_name", displayName);
        userProfile.put("first_name", firstName);
        userProfile.put("last_name", lastName);
        userProfile.put("institution", institutionName);
        userProfile.put("phoneNumber", "");

        db.collection("user_profiles").document(uid)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CreateProfileActivity", "Document created!");
                        nextActivity = new Intent(CreateProfileActivity.this, VerifyPhoneNumberActivity.class);
                        startActivity(nextActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CreateProfileActivity", "Error Writing Document", e);
                    }
                });
    }
}
