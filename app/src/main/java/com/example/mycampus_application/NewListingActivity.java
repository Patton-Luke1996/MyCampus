package com.example.mycampus_application;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;


public class NewListingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;


    private Button nextStep;
    private EditText itemName, itemPrice, itemDescription, itemQuantity;
    private TextView posterLocation;
    private Spinner categorySpinner;
    private NotificationHelper mNotificationHelper;

    private String currentUserID ="", itemNameString = "", itemPriceString = "",
            itemDescriptionString = "", itemQuantityString = "", locationString = "", categoryString = "";

    private Boolean initialDisplay = true;


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

        currentUserID = user.getUid();

        db = FirebaseFirestore.getInstance();

        mNotificationHelper = new NotificationHelper(this);

        categorySpinner =findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.array_category2,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        nextStep =  findViewById(R.id.nextStepButton);
        itemName = findViewById(R.id.etItem);
        itemPrice = findViewById(R.id.etPrice);
        posterLocation = findViewById(R.id.locationText);
        itemDescription = findViewById(R.id.etDescription);
        itemQuantity = findViewById(R.id.etQty);

        DocumentReference docRef = db.collection("user_profiles").document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        posterLocation.setText(Objects.requireNonNull(document.get("institution")).toString());
                    }
                } else {
                    Log.d("LoginActivity", "Get failed with", task.getException());
                }
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(initialDisplay) {
                    initialDisplay = false;
                } else {
                    categoryString = parent.getItemAtPosition(position).toString();
                    if(categoryString.matches("Housing") || categoryString.matches("Sub-Leasings") ||
                            categoryString.matches("Tutoring")) {
                        itemQuantity.setEnabled(false);
                        itemQuantity.setText("N/A");
                    } else {
                        itemQuantity.setEnabled(true);
                        itemQuantity.setText("");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });



        nextStep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
               checkFields();
            }
        });

    }

    private void checkFields() {
        Boolean fieldsComplete = false, photoBypass = false;

        locationString = posterLocation.getText().toString();

        itemNameString = itemName.getText().toString();

        if(itemNameString.matches("")) {
            Toast.makeText(this, "You did not enter a name for this listing!", Toast.LENGTH_LONG).show();
        } else {
            itemPriceString = itemPrice.getText().toString();

            if (itemPriceString.matches("")) {
                Toast.makeText(this, "You did not enter a price for this listing!", Toast.LENGTH_LONG).show();
            } else {
                itemDescriptionString = itemDescription.getText().toString();

                if (itemDescriptionString.matches("")) {
                    Toast.makeText(this, "You did not enter a description for this listing!", Toast.LENGTH_LONG).show();
                } else {
                    categoryString = categorySpinner.getSelectedItem().toString();

                    if(categoryString.matches("Housing") || categoryString.matches("Sub-Leasings") ||
                        categoryString.matches("Tutoring")) {
                            itemQuantityString = "N/A";
                            if (categoryString.matches("Tutoring")) {
                                photoBypass = true;
                                fieldsComplete = true;
                                determineNextActivity(fieldsComplete, photoBypass);
                            } else {
                                fieldsComplete = true;
                                determineNextActivity(fieldsComplete, photoBypass);
                            }
                    } else {
                        itemQuantityString = itemQuantity.getText().toString();

                        if(itemQuantityString.matches("")) {
                            Toast.makeText(this, "You did not enter a quantity for this listing!", Toast.LENGTH_LONG).show();
                        } else {
                            fieldsComplete = true;
                            determineNextActivity(fieldsComplete, photoBypass);
                        }
                    }

                }
            }
        }

    }

    private void determineNextActivity(Boolean fieldsComplete, Boolean photoBypass) {
        Intent nextActivity;

        if (fieldsComplete) {
            if(photoBypass) {
                nextActivity = new Intent(this, PhotoBypassActivity.class);
            } else {
                nextActivity = new Intent(this, AddPhotosActivity.class);
            }

            nextActivity.putExtra("USERID", currentUserID);
            nextActivity.putExtra("ITEMNAME", itemNameString);
            nextActivity.putExtra("ITEMPRICE", itemPriceString);
            nextActivity.putExtra("ITEMDESCRIPTION", itemDescriptionString);
            nextActivity.putExtra("ITEMQUANTITY", itemQuantityString);
            nextActivity.putExtra("LOCATION", locationString);
            nextActivity.putExtra("CATEGORY", categoryString);
            startActivity(nextActivity);
        }
    }

    public void sendOnChannel1()
    {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification();
        mNotificationHelper.getManager().notify(1, nb.build());

    }

}


               /*Intent myIntent = new Intent(getBaseContext(),   AppMainActivity.class);
                startActivity(myIntent);

                int millis = 10000;//259200000;

                new Timer().schedule(new TimerTask()
                {
                    @Override
                    public void run()
                    {
                        //code that runs when timer is done
                        sendOnChannel1();
                    }
                }, millis);*/