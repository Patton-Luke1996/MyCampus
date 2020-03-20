package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class postingDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference docRef;

    private TextView itemName, priceText, quantityText, sellerDisplayName, description;

    private String docID = "";

    private Intent intent;


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

                    } else {
                        Log.d("PostingDetailsActivity", "No such document found with ID: " + docID);
                    }

                } else{
                    Log.d("PostingDetailsActivity", "Get document failed with " + task.getException());
                }
            }
        });


    }
}
