package com.example.mycampus_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

public class AddPhotosActivity extends AppCompatActivity {

    public static final int THUMBNAIL_IMAGE_REQUEST = 1;
    public static final int ADDITIONAL_IMAGE_REQUEST = 2;

    private Intent nextActivity;

    private Button thumbnailImageButton;
    private Button additionalImageButton;
    private Button submitListingButton;

    private ImageView thumbnailImageView;

    private Uri thumbnailImageUri;
    private String thumbnailUrl;

    private String displayName;

    private StorageReference thumbnailStorageReference;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        thumbnailImageButton = findViewById(R.id.thumbnailImageBTN);
        submitListingButton = findViewById(R.id.publishListingBTN);
        additionalImageButton = findViewById(R.id.additionalPhotosBTN);

        thumbnailImageView = findViewById(R.id.thumbnailImageView);


        thumbnailStorageReference = FirebaseStorage.getInstance().getReference("postingThumbnails");
        db = FirebaseFirestore.getInstance();

        thumbnailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThumbnailFileChooser();

            }
        });

        additionalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdditionalFileChooser();
            }
        });

        submitListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn off buttons to prevent multiple uploads
                // start animation somewhere
                grabAdditionalInfo();
            }
        });


    }

    private void grabAdditionalInfo() {

        DocumentReference docRef = db.collection("user_profiles").document(getIntent().getStringExtra("USERID"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        displayName = (String) document.get("display_name");
                        Log.d("AddPhotosActivity", "Document Found with uid: " + getIntent().getStringExtra("USERID"));
                        uploadThumbnailImage();
                    } else {

                        Log.d("AddPhotosActivity", "No such document with uid: " + getIntent().getStringExtra("USERID"));
                    }
                } else {
                    Log.d("AddPhotosActivity", "Get failed with", task.getException());
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadThumbnailImage() {
        if (thumbnailImageUri != null) {
            final StorageReference fileReference = thumbnailStorageReference.child(System.currentTimeMillis()
            + "." + getFileExtension(thumbnailImageUri));

            fileReference.putFile(thumbnailImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Set progress of animation/bar back to zero for user feedback.
                                }
                            }, 1000);

                            Toast.makeText(AddPhotosActivity.this, "Thumbnail image upload sucessful!", Toast.LENGTH_LONG).show();
                            thumbnailUrl = fileReference.getDownloadUrl().toString();

                            // Handle additional files
                            uploadAdditionalImages();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPhotosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            //update progress animation/taskbar when I get to it
                        }
                    });
        } else {
            Toast.makeText(this, "No thumbnail file selected.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAdditionalImages() {
        // Handle imageViewer logic....
        createListing();
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
        newPost.put("thumbnailUrl", thumbnailUrl);

        db.collection("postings").add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddPhotosActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                        nextActivity = new Intent(AddPhotosActivity.this, AppMainActivity.class);
                        startActivity(nextActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddPhotosActivity", "Error adding document", e);
                    }
                });

                // Add on progress listener probably since the user needs feedback.
    }

    public void openThumbnailFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, THUMBNAIL_IMAGE_REQUEST);
    }

    public void openAdditionalFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ADDITIONAL_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            // switch statement for different result calls
            switch(requestCode){
                case THUMBNAIL_IMAGE_REQUEST:
                    thumbnailImageUri = data.getData();
                    Picasso.get().load(thumbnailImageUri).into(thumbnailImageView);
                    break;
                case ADDITIONAL_IMAGE_REQUEST:
                    //handle multiple images with ViewPager
                    break;
                default:
                    break;
            }

        }
    }


}
