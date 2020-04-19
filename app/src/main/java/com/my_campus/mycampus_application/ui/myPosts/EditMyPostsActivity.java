package com.my_campus.mycampus_application.ui.myPosts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.my_campus.mycampus_application.AppMainActivity;
import com.my_campus.mycampus_application.R;
import com.google.android.gms.tasks.Continuation;
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
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditMyPostsActivity extends AppCompatActivity {

    public static final int THUMBNAIL_IMAGE_REQUEST = 1;
    public static final int ADDITIONAL_IMAGE_REQUEST = 2;
    public static final int TOTAL_PICTURES_ALLOWED = 4;

    private FirebaseFirestore db;
    private DocumentReference docRef;

    private TextView itemName, priceText, quantityText, description, postingDuration;

    private Button thumbnailImageButton;
    private Button additionalImageButton;
    private Button saveChangesButton;
    private Button refreshDurationButton;
    private Button deleteListingButton;
    private ImageView deletePictureButton;

    private ProgressBar progressBar;

    private Spinner categorySpinner;

    private String docID = "";

    private String newThumbnailUrl = "";
    private String newPhotoUriHolder = "";

    private String oldThumbnailUrl = "";
    private String oldPhoto1_Url = "";
    private String oldPhoto2_Url = "";
    private String oldPhoto3_Url = "";
    private String oldPhoto4_Url = "";

    private String newPhoto1_Url = "";
    private String newPhoto2_Url = "";
    private String newPhoto3_Url = "";
    private String newPhoto4_Url = "";

    private String sellerName = "";
    private String userID = "";
    private String categoryString = "";

    private Intent intent;

    private ArrayList<String> ImageList = new ArrayList<String>();
    private ArrayList<String> DeletedImageList = new ArrayList<String>();

    private ImageView thumbnailImageView;

    private StorageReference thumbnailStorageReference;
    private StorageReference additionalPicturesStorageReference;
    private StorageReference globalTempReference;

    private Boolean initialDisplay = true;

    private ViewPager myViewPager;
    private CirclePageIndicator indicator;
    private int currentPage = 0;
    private int tempReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_posts);

        tempReference = 0;

        db = FirebaseFirestore.getInstance();

        itemName = findViewById(R.id.itemNameET);
        postingDuration = findViewById(R.id.listingDurationTV);
        priceText = findViewById(R.id.priceET);
        quantityText = findViewById(R.id.quantityET);
        description = findViewById(R.id.descriptionET);
        thumbnailImageView = findViewById(R.id.editPostsThumbnail);
        categorySpinner =findViewById(R.id.categorySpinnerEditPosts);

        thumbnailImageButton = findViewById(R.id.changeThumbnailBTN);
        additionalImageButton = findViewById(R.id.changeAdditionalPhotoBTN);
        saveChangesButton = findViewById(R.id.saveChangesBTN);
        deletePictureButton = findViewById(R.id.deletePhoto_EMP_BTN);
        refreshDurationButton = findViewById(R.id.refreshListingBTN);
        deleteListingButton = findViewById(R.id.deleteListingBTN);

        thumbnailStorageReference = FirebaseStorage.getInstance().getReference("postingThumbnails");
        additionalPicturesStorageReference = FirebaseStorage.getInstance().getReference("postingPictures");

        progressBar = findViewById(R.id.progressInfo_EMP);


        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.array_category2,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        myViewPager = findViewById(R.id.editPostsViewPager);
        indicator = findViewById(R.id.viewIndicator_editPosts);

        intent = getIntent();
        docID = intent.getStringExtra("docID");

        if (ImageList.isEmpty()) {
            deletePictureButton.setVisibility(View.INVISIBLE);
        }

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
                        description.setText(document.getString("description"));
                        postingDuration.setText(document.getLong("postDuration").toString());
                        sellerName = document.getString("sellerName");
                        userID = document.getString("userID");

                        if(document.getString("category") != null) {
                            int spinnerPosition = adapter.getPosition(document.getString("category"));
                            categorySpinner.setSelection(spinnerPosition);
                        }

                        if(!document.getString("thumbnailUrl").matches("Tutoring")) {
                            oldThumbnailUrl = document.getString("thumbnailUrl");
                            Picasso.get().load(oldThumbnailUrl).into(thumbnailImageView);

                        } else {
                            oldThumbnailUrl = "Tutoring";
                            Picasso.get().load(R.drawable.tutoring_thumbnail).into(thumbnailImageView);
                        }

                        if(!document.getString("additionalPhoto1_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto1_Url"));
                            oldPhoto1_Url = document.getString("additionalPhoto1_Url");
                            Log.d("postingDetails", ImageList.get(0));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto2_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto2_Url"));
                            oldPhoto2_Url = document.getString("additionalPhoto2_Url");
                            Log.d("postingDetails", ImageList.get(1));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto3_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto3_Url"));
                            oldPhoto3_Url = document.getString("additionalPhoto3_Url");
                            Log.d("postingDetails", ImageList.get(2));
                            instantiateImages();
                        }

                        if(!document.getString("additionalPhoto4_Url").matches("")) {
                            ImageList.add(document.getString("additionalPhoto4_Url"));
                            oldPhoto4_Url = document.getString("additionalPhoto4_Url");
                            Log.d("postingDetails", ImageList.get(3));
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

        thumbnailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThumbnailFileChooser();

            }
        });

        deletePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ImageList.isEmpty()) {
                    ImageList.remove(currentPage);

                    if(ImageList.isEmpty()) {
                        deletePictureButton.setVisibility(View.INVISIBLE);
                    }
                    instantiateImages();

                } else {
                    Toast.makeText(EditMyPostsActivity.this, "You need at least one picture to delete!", Toast.LENGTH_LONG).show();
                }
            }
        });

        additionalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageList.size() < TOTAL_PICTURES_ALLOWED){
                    openAdditionalFileChooser();
                } else {
                    Toast.makeText(EditMyPostsActivity.this, "You can only add up to four additional photos!", Toast.LENGTH_LONG).show();
                }

            }
        });

        refreshDurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                docRef.update("postDuration", 7).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditMyPostsActivity.this, "Listing duration refreshed to 7 days.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditMyPostsActivity.this, "Failure to refresh post duration. Please try again later.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        deleteListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                fullDeleteThumbnail();

            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                determineThumbnailChanges();

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
                        quantityText.setEnabled(false);
                        quantityText.setText("N/A");
                    } else {
                        quantityText.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


    }

    private void determineThumbnailChanges() {

        Log.d("EditMyPostsActivity", "determineThumbnailChangesBeginning()");
        Log.d("EditMyPostsActivity", "oldThumbnailUrl = " + oldThumbnailUrl);
        // Logic problem here, it's not enterting either if statement so oops

        if (!oldThumbnailUrl.matches("Tutoring")) {
            if(newThumbnailUrl.matches("")) {
                // move on to determine additional photo changes
                newThumbnailUrl = oldThumbnailUrl;
                determineChangeInAdditionalImages();
            } else {
                // upload new thumbnail image
                uploadNewThumbnailImage();
            }
        } else {
            if(newThumbnailUrl.matches("")){
                // move on to determine additional photo changes
                newThumbnailUrl = oldThumbnailUrl;
                determineChangeInAdditionalImages();
            } else {
                // upload new thumbnail image
                uploadNewThumbnailImage();
            }
        }

        Log.d("EditMyPostsActivity", "determineThumbnailChangesEnd()");
    }

    private void uploadNewThumbnailImage() {
        Log.d("EditMyPostsActivity", "uploadNewThumbnailImage_Breakpoint_1()");

        if (newThumbnailUrl != null) {
            final StorageReference fileReference = thumbnailStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(Uri.parse(newThumbnailUrl)));

            UploadTask uploadTask = fileReference.putFile(Uri.parse(newThumbnailUrl));

            progressBar.setVisibility(View.VISIBLE);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {

                        Log.d("EditMyPostsActivity", "uploadNewThumbnailImage_Breakpoint_2()");
                        newThumbnailUrl = task.getResult().toString();

                        if (oldThumbnailUrl.startsWith("https")) {
                            DeletedImageList.add(oldThumbnailUrl);
                        }

                        determineChangeInAdditionalImages();
                        Toast.makeText(EditMyPostsActivity.this, "New thumbnail uploaded.", Toast.LENGTH_SHORT).show();

                    } else {
                        // Handle Failures
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                    Log.d("EditMyPostsActivity", "Upload thumbnail failed with", e);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void determineChangeInAdditionalImages() {

        Log.d("EditMyPostsActivity", "determineChangeInAdditionalImagesStart()");

        if (ImageList.isEmpty()) {
            Log.d("EditMyPostsActivity", "ImageList size = " + ImageList.size());
            if (oldPhoto1_Url.matches("")) {
                Log.d("EditMyPostsActivity", "oldPhoto1_Url doesn't exist=" + oldPhoto1_Url);
                updateDocument();
            } else {
                DeletedImageList.add(oldPhoto1_Url);
                Log.d("EditMyPostsActivity", "oldPhoto1_Url exists and =" + oldPhoto1_Url);

                if (oldPhoto2_Url.matches("")) {
                    Log.d("EditMyPostsActivity", "oldPhoto2_Url doesn't exist=" + oldPhoto2_Url);
                    updateDocument();
                } else {
                    DeletedImageList.add(oldPhoto2_Url);
                    Log.d("EditMyPostsActivity", "oldPhoto2_Url exists and =" + oldPhoto2_Url);

                    if (oldPhoto3_Url.matches("")) {
                        updateDocument();
                    } else {
                        DeletedImageList.add(oldPhoto3_Url);

                        if (oldPhoto4_Url.matches("")) {
                            updateDocument();
                        } else {
                            DeletedImageList.add(oldPhoto4_Url);
                            updateDocument();
                        }
                    }
                }
            }

        } else {
            Log.d("EditMyPostsActivity", "determineChangeInAdditionalImages_Images Exist");
            for (int i = 0; i < ImageList.size(); i ++) {
                Log.d("EditMyPostsActivity", "Inside for loop and i = " + i);
                if (i == 0) {
                    if (oldPhoto1_Url.matches(ImageList.get(i))) {
                        // no change
                        newPhoto1_Url = oldPhoto1_Url;
                    } else {
                        if (ImageList.get(i).startsWith("https")) {

                            if(oldPhoto1_Url.startsWith("https")) {
                                DeletedImageList.add(oldPhoto1_Url);
                            }
                            newPhoto1_Url = ImageList.get(i);
                        } else {
                            // New uri to be uploaded
                            if(oldPhoto1_Url.startsWith("https")) {
                                DeletedImageList.add(oldPhoto1_Url);
                            }
                        }
                    }

                } else if (i == 1) {
                    if (oldPhoto2_Url.matches(ImageList.get(i))) {
                        newPhoto2_Url = oldPhoto2_Url;
                    } else {
                        if (ImageList.get(i).startsWith("https")) {
                            // was it deleted?
                            // or was it moved down 1 index
                            if (oldPhoto2_Url.matches(ImageList.get(i-1))) {
                                // photo 1 was deleted, reference moved down. No need for deletion, just reassign
                                newPhoto2_Url = ImageList.get(i);
                            } else {
                                // photo 2 was deleted
                                // new photo is already uploaded, delete old photo, reassign http to reference this photo
                                if(oldPhoto2_Url.startsWith("https")) {
                                    DeletedImageList.add(oldPhoto2_Url);
                                }

                                newPhoto2_Url = ImageList.get(i);
                            }
                        } else {
                            // New uri to be uploaded, image 2 was still deleted
                            if(oldPhoto2_Url.startsWith("https")) {
                                DeletedImageList.add(oldPhoto2_Url);
                            }
                        }
                    }

                } else if (i == 2) {
                    if (oldPhoto3_Url.matches(ImageList.get(i))) {
                        newPhoto3_Url = oldPhoto3_Url;
                    } else {
                        if (ImageList.get(i).startsWith("https")) {
                            // was it deleted?
                            // or was it moved down 1 index
                            if (oldPhoto3_Url.matches(ImageList.get(i-1))) {
                                // photo 2 was deleted, reference moved down. No need for deletion, just reassign
                                newPhoto3_Url = ImageList.get(i);
                            } else {
                                // photo 3 was deleted
                                // new photo is already uploaded, delete old photo, reassign http to reference this photo
                                if(oldPhoto3_Url.startsWith("https")) {
                                    DeletedImageList.add(oldPhoto3_Url);
                                }

                                newPhoto3_Url = ImageList.get(i);
                            }
                        } else {
                            // New uri to be uploaded, image 3 was still deleted
                            if(oldPhoto3_Url.startsWith("https")) {
                                DeletedImageList.add(oldPhoto3_Url);
                            }
                        }
                    }

                } else if (i == 3) {
                    if (oldPhoto4_Url.matches(ImageList.get(i))) {
                        newPhoto4_Url = oldPhoto4_Url;
                    } else {
                        if (ImageList.get(i).startsWith("https")) {
                            // was it deleted?
                            // or was it moved down 1 index
                            if (oldPhoto4_Url.matches(ImageList.get(i-1))) {
                                // photo 3 was deleted, reference moved down. No need for deletion, just reassign
                                newPhoto4_Url = ImageList.get(i);
                            } else {
                                // photo 4 was deleted
                                // new photo is already uploaded, delete old photo, reassign http to reference this photo
                                if(oldPhoto4_Url.startsWith("https")) {
                                    DeletedImageList.add(oldPhoto4_Url);
                                }

                                newPhoto4_Url = ImageList.get(i);
                            }
                        } else {
                            // New uri to be uploaded, image 4 was still deleted
                            if(oldPhoto4_Url.startsWith("https")) {
                                DeletedImageList.add(oldPhoto4_Url);
                            }
                        }
                    }

                } 
            }

            Log.d("EditMyPostsActivity", "determineChangeInAdditionalImages (end)");
            uploadNewAdditionalImages();

        }

    }

    private void uploadNewAdditionalImages() {

        if(!ImageList.isEmpty()) {
            if (ImageList.get(tempReference).startsWith("https")){
                Log.d("EditMyPostsActivity", "uploadNewImages: Image " + (tempReference +1) + "starts with https" );
                //do not upload new image
                // iterate counter
                // newPhotoUrls are already taken care of in previous activity
                if (tempReference == ImageList.size() - 1) {
                    updateDocument();
                } else {
                    tempReference++;
                    uploadNewAdditionalImages();
                }
            } else {
                Log.d("EditMyPostsActivity", "uploadNewImages: Image " + (tempReference +1) + "starting upload sequence" );
                globalTempReference = additionalPicturesStorageReference.child(System.currentTimeMillis()
                        + "." + getFileExtension(Uri.parse(ImageList.get(tempReference))));

                Log.d("EditMyPostsActivity", "uploadNewImages: Image " + (tempReference +1) + "starting upload sequence" );

                UploadTask uploadTask = globalTempReference.putFile(Uri.parse(ImageList.get(tempReference)));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        globalTempReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("AddPhotosActivity", "Inside addOnCompleteListener.isSuccessful");
                                Log.d("AddPhotosActivity", "tempReference == " + tempReference);

                                progressBar.setVisibility(View.INVISIBLE);

                                if (tempReference == 0) {
                                    newPhoto1_Url = uri.toString();
                                    Toast.makeText(EditMyPostsActivity.this, "Picture 1 Uploaded.", Toast.LENGTH_SHORT).show();

                                    if (tempReference == ImageList.size() - 1) {
                                        updateDocument();
                                    } else {
                                        tempReference++;
                                        uploadNewAdditionalImages();
                                    }


                                } else if (tempReference == 1) {
                                    newPhoto2_Url = uri.toString();
                                    Toast.makeText(EditMyPostsActivity.this, "Picture 2 Uploaded.", Toast.LENGTH_SHORT).show();

                                    if (tempReference == ImageList.size() - 1) {
                                        updateDocument();
                                    } else {
                                        tempReference++;
                                        uploadNewAdditionalImages();
                                    }


                                } else if (tempReference == 2) {
                                    newPhoto3_Url = uri.toString();
                                    Toast.makeText(EditMyPostsActivity.this, "Picture 3 Uploaded.", Toast.LENGTH_SHORT).show();

                                    if (tempReference == ImageList.size() - 1) {
                                        updateDocument();
                                    } else {
                                        tempReference++;
                                        uploadNewAdditionalImages();
                                    }


                                } else if (tempReference == 3) {
                                    newPhoto4_Url = uri.toString();
                                    Toast.makeText(EditMyPostsActivity.this, "Picture 4 Uploaded.", Toast.LENGTH_SHORT).show();

                                    if (tempReference == ImageList.size() - 1) {
                                        updateDocument();
                                    } else {
                                        tempReference++;
                                        uploadNewAdditionalImages();
                                    }


                                } else {
                                    Log.d("EditMyPostsActivity", "IsSuccessful If statement chain failed... ");
                                }
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

            }

        } else {
            updateDocument();
        }

    }

    private void updateDocument() {
        final Map<String, Object> newPost = new HashMap<>();
        newPost.put("userID", userID);
        newPost.put("sellerName", sellerName);
        newPost.put("itemName", itemName.getText().toString());
        newPost.put("description", description.getText().toString());
        newPost.put("price", priceText.getText().toString());
        newPost.put("category", categoryString);
        newPost.put("quantity", quantityText.getText().toString());
        newPost.put("validPosting", true);
        newPost.put("postDuration", 7);
        newPost.put("thumbnailUrl", newThumbnailUrl);
        newPost.put("additionalPhoto1_Url", newPhoto1_Url);
        newPost.put("additionalPhoto2_Url", newPhoto2_Url);
        newPost.put("additionalPhoto3_Url", newPhoto3_Url);
        newPost.put("additionalPhoto4_Url", newPhoto4_Url);


        progressBar.setVisibility(View.VISIBLE);

        db.collection("postings").document(docID).set(newPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid ) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(EditMyPostsActivity.this, "Posting updated. Cleaning up old data...", Toast.LENGTH_SHORT).show();
                        tempReference = 0;
                        deleteOldFiles();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(EditMyPostsActivity.this, "Error: Update document failed at final step. Please try again later.", Toast.LENGTH_SHORT).show();
                        Log.w("EditMyPostsActivity", "Error adding document", e);
                    }
                });

        // Add on progress listener probably since the user needs feedback.
    }

    private void deleteOldFiles() {

        for (int i = 0; i < DeletedImageList.size(); i++) {
            Log.d("EditMyPhotosActivity", "DeleteImageList contains at: " + i + " data: " + DeletedImageList.get(i));
        }

        if (!DeletedImageList.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(DeletedImageList.get(tempReference));

            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    int counter = tempReference;
                    counter++;

                    Toast.makeText(EditMyPostsActivity.this, "Old photo " + (counter) + " deleted from cloud storage.", Toast.LENGTH_SHORT).show();

                    if (tempReference == DeletedImageList.size() - 1) {
                        moveToMainActivity();
                        progressBar.setVisibility(View.INVISIBLE);
                    } else {
                        tempReference++;
                        deleteOldFiles();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("firebasestorage", "OnFailure: Did not delete file", e);

                    finish();
                    startActivity(getIntent());
                }
            });
        } else {
            moveToMainActivity();
        }

    }

    private void fullDeleteThumbnail() {
        if (!oldThumbnailUrl.matches("Tutoring")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailUrl);

            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditMyPostsActivity.this, "Thumbnail deleted from cloud storage.", Toast.LENGTH_SHORT).show();

                    fullDeleteAdditionalPhotos();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("firebasestorage", "OnFailure: Did not delete file", e);

                    finish();
                    startActivity(getIntent());
                }
            });
        } else {
            fullDeleteAdditionalPhotos();
        }


    }

    private void fullDeleteAdditionalPhotos() {
        if (oldPhoto1_Url.matches("") && oldPhoto2_Url.matches("") && oldPhoto3_Url.matches("")
        && oldPhoto4_Url.matches("")) {
            // move to delete document, no additional pictures stored anywhere
            fullDeleteDocument();
        } else {
            // one or more of the above urls are not blank "", thus start deleting those that start with https, and skip any that are still "" or blank
            // iterate through this using tempReference and recall the function
            if (tempReference == 0) {
                if(!oldPhoto1_Url.matches("")) {
                    if(oldPhoto1_Url.startsWith("https")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldPhoto1_Url);

                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditMyPostsActivity.this, "Additional Photo 1 deleted from cloud storage.", Toast.LENGTH_SHORT).show();
                                tempReference++;
                                fullDeleteAdditionalPhotos();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                                Log.e("firebasestorage", "OnFailure: Did not delete file", e);
                                progressBar.setVisibility(View.INVISIBLE);

                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                } else {
                    fullDeleteDocument();
                }

            } else if (tempReference == 1) {
                if(!oldPhoto2_Url.matches("")) {
                    if(oldPhoto2_Url.startsWith("https")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldPhoto2_Url);

                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditMyPostsActivity.this, "Additional Photo 2 deleted from cloud storage.", Toast.LENGTH_SHORT).show();
                                tempReference++;
                                fullDeleteAdditionalPhotos();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                                Log.e("firebasestorage", "OnFailure: Did not delete file", e);
                                progressBar.setVisibility(View.INVISIBLE);

                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                } else {
                    fullDeleteDocument();
                }

            } else if (tempReference == 2) {
                if(!oldPhoto3_Url.matches("")) {
                    if(oldPhoto3_Url.startsWith("https")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldPhoto3_Url);

                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditMyPostsActivity.this, "Additional Photo 3 deleted from cloud storage.", Toast.LENGTH_SHORT).show();
                                tempReference++;
                                fullDeleteAdditionalPhotos();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                                Log.e("firebasestorage", "OnFailure: Did not delete file", e);
                                progressBar.setVisibility(View.INVISIBLE);

                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                } else {
                    fullDeleteDocument();
                }

            } else if (tempReference == 3) {
                if(!oldPhoto4_Url.matches("")) {
                    if(oldPhoto4_Url.startsWith("https")) {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(oldPhoto4_Url);

                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditMyPostsActivity.this, "Additional Photo 4 deleted from cloud storage.", Toast.LENGTH_SHORT).show();
                                tempReference++;
                                fullDeleteAdditionalPhotos();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                                Log.e("firebasestorage", "OnFailure: Did not delete file", e);
                                progressBar.setVisibility(View.INVISIBLE);

                                finish();
                                startActivity(getIntent());
                            }
                        });
                    }
                } else {
                    fullDeleteDocument();
                }

            } else {
                Log.d("EditMyPostsActivity", "Out of bounds error for full deletion of additional photos. tempReference =" + tempReference);
            }
        }
    }

    private void fullDeleteDocument() {
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditMyPostsActivity.this, "Listing sucessfully deleted.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                moveToMainActivity();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditMyPostsActivity.this, "Error: Delete failure. Please try again later.", Toast.LENGTH_SHORT).show();
                Log.e("firestore", "OnFailure: Did not delete file", e);
                moveToMainActivity();
            }
        });

    }

    private void moveToMainActivity() {
        Intent nextActivity = new Intent(EditMyPostsActivity.this, AppMainActivity.class);
        startActivity(nextActivity);
    }

    private void openThumbnailFileChooser() {
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


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            // switch statement for different result calls
            switch(requestCode){
                case THUMBNAIL_IMAGE_REQUEST:
                    newThumbnailUrl = data.getData().toString();
                    Picasso.get().load(newThumbnailUrl).into(thumbnailImageView);
                    break;
                case ADDITIONAL_IMAGE_REQUEST:
                    newPhotoUriHolder = data.getData().toString();
                    ImageList.add(newPhotoUriHolder);
                    deletePictureButton.setVisibility(View.VISIBLE);
                    instantiateImages();
                    break;
                default:
                    break;
            }

        }
    }

    private void instantiateImages() {
        myViewPager.setAdapter(new EditMyPosts_SlidingImage_Adapter(EditMyPostsActivity.this, ImageList));
        indicator.setViewPager(myViewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        // Ensures that currentPage always has the current item and prevents index errors if indicator doesn't have to run/update the view
        currentPage = myViewPager.getCurrentItem();
        myViewPager.getAdapter().notifyDataSetChanged();

        if (ImageList.isEmpty()) {
            deletePictureButton.setVisibility(View.INVISIBLE);
        }

        Log.d("postingDetails", "instantiateImages()");
    }
}
