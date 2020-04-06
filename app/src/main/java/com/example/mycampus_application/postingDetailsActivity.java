package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class postingDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference docRef;

    private TextView itemName, priceText, quantityText, sellerDisplayName, description;

    private String docID = "";

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

        itemName = findViewById(R.id.itemName);
        priceText = findViewById(R.id.priceText);
        quantityText = findViewById(R.id.quantityText);
        sellerDisplayName = findViewById(R.id.sellerDisplayName);
        description = findViewById(R.id.description);

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

                        if(!document.getString("thumbnailUrl").matches("Tutoring")) {
                            ImageList.add(document.getString("thumbnailUrl"));
                            Log.d("postingDetails", ImageList.get(0));
                            instantiateImages();
                        } else {
                            ImageList.add("Tutoring");
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
