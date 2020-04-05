package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mycampus_application.ui.myPosts.MyPostsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PhotoBypassActivity extends AppCompatActivity {

    private Button addPhotoBTN, publishListingBTN;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Intent nextActivity;

    private String displayName = "";

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
                grabAdditionalInfo();
            }
        });
    }

    private void grabAdditionalInfo() {

        // Posting Schema

        // userID (string)
        // sellerName (string)
        // itemName (string)
        // description (string)
        // price (string)
        // category (string)
        // quantity (string)
        // validPosting (boolean)
        // thumbnailUri (set to the three exceptions), then grab resource drawer in listing activites (string)
        // postDuration (int)
        // postExpiredDuration (?) (int)

        // Get userID displayName

        DocumentReference docRef = db.collection("user_profiles").document(getIntent().getStringExtra("USERID"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            displayName = (String) document.get("display_name");
                            Log.d("PhotoBypassActivity", "Document Found with uid: " + getIntent().getStringExtra("USERID"));
                            createListing();

                        } else {

                            Log.d("PhotoBypassActivity", "No such document with uid: " + getIntent().getStringExtra("USERID"));
                        }
                    } else {
                        Log.d("PhotoBypassActivity", "Get failed with", task.getException());
                    }
                }
        });

    }

    private void createListing() {

        final Map<String, Object> newPost = new HashMap<>();
        newPost.put("userID", getIntent().getStringExtra("USERID"));
        newPost.put("sellerName", displayName);
        newPost.put("itemName", getIntent().getStringExtra("ITEMNAME"));
        newPost.put("description", getIntent().getStringExtra("ITEMDESCRIPTION"));
        newPost.put("price", getIntent().getStringExtra("ITEMPRICE"));
        newPost.put("category", getIntent().getStringExtra("CATEGORY"));
        newPost.put("quantity", getIntent().getStringExtra("ITEMQUANTITY"));
        newPost.put("validPosting", true);
        newPost.put("postDuration", 7);
        newPost.put("thumbnailUrl", getIntent().getStringExtra("CATEGORY"));
        newPost.put("additionalPhoto1_Url", "");
        newPost.put("additionalPhoto2_Url", "");
        newPost.put("additionalPhoto3_Url", "");
        newPost.put("additionalPhoto4_Url", "");

        db.collection("postings").add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("PhotoBypassActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                        nextActivity = new Intent(PhotoBypassActivity.this, AppMainActivity.class);
                        startActivity(nextActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("PhotoBypassActivity", "Error adding document", e);
                    }
                });

    }
}
