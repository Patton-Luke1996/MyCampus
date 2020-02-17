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
        arrayList.add("Select Institution");
        arrayList.add("University of Michigan - Dearborn");
        arrayList.add("Other");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        // Will need to grab all information eventually

    }

    public void startProfileCreation(View view) {
        grabAllInfo();

        // Needs boolean logic to handle if everything went through/or if it failed
        // nextActivity = new Intent(this, AppMainActivity.class);
        // startActivity(nextActivity);
    }

    public void grabAllInfo() {

        //institutionName = parent.getItemAtPosition(position).toString();

        firstName = firstName_ET.getText().toString();
        lastName = lastName_ET.getText().toString();
        displayName = displayName_ET.getText().toString();

        uid = user.getUid();

        /*Toast.makeText(parent.getContext(), "Selected: " + institutionName,
                Toast.LENGTH_LONG).show();*/

        makeNewDoc();

        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });*/

    }

    private void makeNewDoc() {


        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("display_name", displayName);
        userProfile.put("first_name", firstName);
        userProfile.put("last_name", lastName);
        userProfile.put("institution", institutionName);

        db.collection("user_profiles").document(uid)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CreateProfileActivity", "Document created!");
                        nextActivity = new Intent(CreateProfileActivity.this, AppMainActivity.class);
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
