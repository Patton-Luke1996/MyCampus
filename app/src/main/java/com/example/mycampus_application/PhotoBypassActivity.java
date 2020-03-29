package com.example.mycampus_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PhotoBypassActivity extends AppCompatActivity {

    private Button addPhotoBTN, publishListingBTN;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_bypass);

        addPhotoBTN = findViewById(R.id.addPhotosBTN);
        publishListingBTN = findViewById(R.id.publishListingBTN);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        addPhotoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity = new Intent(PhotoBypassActivity.this, AddPhotosActivity.class);
                nextActivity.putExtra("USERID", getIntent().getStringExtra("USERID"));
                nextActivity.putExtra("ITEMNAME", getIntent().getStringExtra("ITEMNAME"));
                nextActivity.putExtra("ITEMPRICE", getIntent().getStringExtra("ITEMPRICE"));
                nextActivity.putExtra("ITEMDESCRIPTION", getIntent().getStringExtra("ITEMDESCRIPTION"));
                nextActivity.putExtra("ITEMQUANTITY", getIntent().getStringExtra("ITEMQUANTITY"));
                nextActivity.putExtra("LOCATION", getIntent().getStringExtra("LOCATION"));
                nextActivity.putExtra("CATEGORY", getIntent().getStringExtra("CATEGORY"));
                startActivity(nextActivity);
            }
        });

        publishListingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListingDoc();
            }
        });
    }

    private void createListingDoc() {

        // Posting Schema

        // userID
        // sellerName
        // itemName
        // category
        // quantity
        // validPosting
        // thumbnailUri (default for tutoring, subleasings, housing

        /*Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("display_name", displayName);
        userProfile.put("first_name", firstName);
        userProfile.put("last_name", lastName);
        userProfile.put("institution", institutionName);*/
    }
}
