package com.example.mycampus_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class NewListingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Button submit, thumbnailButton, additionalPicturesButton;
    private EditText itemName, itemPrice, itemDescription, itemQuantity;
    private TextView posterLocation;
    private Spinner categorySpinner;
    private ImageView thumbnail_ImageView;


    public NewListingActivity() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        categorySpinner =findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.array_category2,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        submit =  findViewById(R.id.submitButton);
        thumbnailButton = findViewById(R.id.thumbnail_Button);
        additionalPicturesButton = findViewById(R.id.additionalPics_Button);
        itemName = findViewById(R.id.etItem);
        itemPrice = findViewById(R.id.etPrice);
        posterLocation = findViewById(R.id.locationText);
        itemDescription = findViewById(R.id.etDescription);
        itemQuantity = findViewById(R.id.etQty);
        thumbnail_ImageView = findViewById(R.id.thumbnail_ImageView);


        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                // Check for empty Thumbnail image
                // If it's a service, grey out
                // If it's anything else and it's empty, throw error, make them upload an before moving on

                Intent myIntent = new Intent(getBaseContext(),   AppMainActivity.class);
                startActivity(myIntent);
            }
        });




    }


}
