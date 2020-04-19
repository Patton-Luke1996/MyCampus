package com.my_campus.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class postingDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference docRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    private TextView itemName, priceText, quantityText, sellerDisplayName, description;

    private Button messageSellerBTN;

    private String docID = "", sellerUserID = "", sellerPhoneNumber = "", sellerFirstName = "", smsbody = "", userFirstName = "";

    private Intent intent;

    private ArrayList<String> ImageList = new ArrayList<String>();

    private ViewPager myViewPager;
    private CirclePageIndicator indicator;
    private int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_details);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        itemName = findViewById(R.id.itemName);
        priceText = findViewById(R.id.priceText);
        quantityText = findViewById(R.id.quantityText);
        sellerDisplayName = findViewById(R.id.sellerDisplayName);
        description = findViewById(R.id.description);
        messageSellerBTN = findViewById(R.id.messageSellerBtn);

        myViewPager = findViewById(R.id.postingDetailsViewPager);
        indicator = findViewById(R.id.viewIndicator_postingDetails);

        intent = getIntent();
        docID = intent.getStringExtra("docID");

        docRef = db.collection("postings").document(docID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        itemName.setText(document.getString("itemName"));
                        priceText.setText(document.getString("price"));
                        quantityText.setText(document.getString("quantity"));
                        sellerDisplayName.setText(document.getString("sellerName"));
                        description.setText(document.getString("description"));
                        sellerUserID = document.getString("userID");


                        if(!document.getString("thumbnailUrl").matches("Tutoring")) {
                            ImageList.add(document.getString("thumbnailUrl"));
                            Log.d("postingDetails", ImageList.get(0));
                            instantiateImages();
                        } else {
                            ImageList.add("Tutoring");
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto1_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto1_Url"));
                            Log.d("postingDetails", ImageList.get(1));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto2_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto2_Url"));
                            Log.d("postingDetails", ImageList.get(2));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto3_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto3_Url"));
                            Log.d("postingDetails", ImageList.get(3));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto4_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto4_Url"));
                            Log.d("postingDetails", ImageList.get(4));
                            instantiateImages();
                        }




                    } else {
                        Log.d("PostingDetailsActivity", "No such document found with ID: " + docID);
                    }

                } else{
                    Log.d("PostingDetailsActivity", "Get document failed with " + task.getException());
                }
            }
        });

        messageSellerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageSellerBTN.setEnabled(false);
                getSellerPhoneNumber();
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Allows pictures to slide backwards logically after deletion
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //messageSellerListener, at some point


    }

    private void getSellerPhoneNumber() {
        docRef = db.collection("user_profiles").document(sellerUserID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        sellerPhoneNumber = document.getString("phoneNumber");
                        sellerFirstName = document.getString("first_name");
                        getCurrentUserName();
                    } else {
                        Log.d("PostingDetailsActivity", "No such document found with ID: " + sellerUserID);
                        messageSellerBTN.setEnabled(true);
                    }

                } else {
                    Log.d("PostingDetailsActivity", "Get document failed with " + task.getException());
                    messageSellerBTN.setEnabled(true);
                }
            }
        });
    }

    private void getCurrentUserName() {
        docRef = db.collection("user_profiles").document(user.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        userFirstName = document.getString("first_name");
                        createSMSBody();
                    } else {
                        Log.d("PostingDetailsActivity", "No such document found with ID: " + user.getUid());
                        messageSellerBTN.setEnabled(true);
                    }
                } else {
                    Log.d("PostingDetailsActivity", "Get document failed with " + task.getException());
                    messageSellerBTN.setEnabled(true);
                }
            }
        });
    }

    private void createSMSBody() {
        smsbody = "Hi " + sellerFirstName + ". My name is " + userFirstName + " and I'm interested in your " + itemName.getText().toString() + " listed for "
        + priceText.getText().toString() + " via MyCampus.";
        sendSMS();
    }

    private void sendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
            }

        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sellerPhoneNumber, null, smsbody, null, null);
            Toast.makeText(getApplicationContext(), "Message sent. Please check your default SMS app for further communication.", Toast.LENGTH_LONG).show();
            disableMessageSellerButton();
        }
    }

    private void disableMessageSellerButton() {
        messageSellerBTN.setEnabled(false);
        messageSellerBTN.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sellerPhoneNumber, null, smsbody, null, null);
                    Toast.makeText(getApplicationContext(), "Messsage sent. Please check your default SMS app for further communication.", Toast.LENGTH_LONG).show();
                    disableMessageSellerButton();
                } else {
                    Toast.makeText(getApplicationContext(),"SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    messageSellerBTN.setEnabled(true);
                    return;
                }
            }
        }
    }



    private void instantiateImages() {
        myViewPager.setAdapter(new postingDetails_SlidingImage_Adapter(postingDetailsActivity.this, ImageList));
        indicator.setViewPager(myViewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        // Ensures that currentPage always has the current item and prevents index errors if indicator doesn't have to run/update the view
        currentPage = myViewPager.getCurrentItem();
        myViewPager.getAdapter().notifyDataSetChanged();

        Log.d("postingDetails", "instantiateImages()");
    }
}
