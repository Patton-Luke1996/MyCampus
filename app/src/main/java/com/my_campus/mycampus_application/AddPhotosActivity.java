package com.my_campus.mycampus_application;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class AddPhotosActivity extends AppCompatActivity {

    public static final int THUMBNAIL_IMAGE_REQUEST = 1;
    public static final int ADDITIONAL_IMAGE_REQUEST = 2;
    public static final int TOTAL_PICTURES_ALLOWED = 4;

    private Intent nextActivity;

    private Button thumbnailImageButton;
    private Button additionalImageButton;
    private Button submitListingButton;
    private ImageView deletePictureButton;

    private Boolean pictureUploading;

    private ImageView thumbnailImageView;
    private ViewPager myViewPager;

    private CirclePageIndicator indicator;
    private ProgressBar progressBar;

    private Uri thumbnailImageUri;
    private Uri additionalPhotoUri;

    private String thumbnailUrl;
    private String additionalPhoto1_Url = "";
    private String additionalPhoto2_Url = "";
    private String additionalPhoto3_Url = "";
    private String additionalPhoto4_Url = "";


    private int currentPage = 0;
    private int tempReference;

    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    private String displayName;

    private StorageReference thumbnailStorageReference;
    private StorageReference additionalPicturesStorageReference;
    private StorageReference globalTempReference;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photos);

        tempReference = 0;

        thumbnailImageButton = findViewById(R.id.thumbnailImageBTN);
        submitListingButton = findViewById(R.id.publishListingBTN);
        additionalImageButton = findViewById(R.id.additionalPhotosBTN);
        deletePictureButton = findViewById(R.id.deletePhotoBTN);

        thumbnailImageView = findViewById(R.id.thumbnailImageView);
        myViewPager = findViewById(R.id.addPhotosViewPager);

        indicator = findViewById(R.id.viewIndicator_addPhotos);
        progressBar = findViewById(R.id.progressInfo);

        thumbnailStorageReference = FirebaseStorage.getInstance().getReference("postingThumbnails");
        additionalPicturesStorageReference = FirebaseStorage.getInstance().getReference("postingPictures");
        db = FirebaseFirestore.getInstance();

        if (ImageList.isEmpty()) {
            deletePictureButton.setVisibility(View.INVISIBLE);
        }

        thumbnailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openThumbnailFileChooser();

            }
        });

        additionalImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageList.size() < TOTAL_PICTURES_ALLOWED){
                    openAdditionalFileChooser();
                } else {
                    Toast.makeText(AddPhotosActivity.this, "You can only add up to four additional photos!", Toast.LENGTH_LONG).show();
                }

            }
        });

        submitListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Turn off buttons to prevent multiple uploads
                // can check if something isInProgress instead of graying everything out.
                // start animation somewhere
                if (thumbnailImageView.getDrawable() != null) {

                    deletePictureButton.setEnabled(false);
                    additionalImageButton.setEnabled(false);
                    thumbnailImageButton.setEnabled(false);
                    submitListingButton.setEnabled(false);

                    grabAdditionalInfo();
                } else {
                    Toast.makeText(AddPhotosActivity.this, "You need to add at least a thumbnail image!", Toast.LENGTH_LONG).show();
                }

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
                      updateViewPager();

                } else {
                    Toast.makeText(AddPhotosActivity.this, "You need at least one picture to delete!", Toast.LENGTH_LONG).show();
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

                    deletePictureButton.setEnabled(true);
                    additionalImageButton.setEnabled(true);
                    thumbnailImageButton.setEnabled(true);
                    submitListingButton.setEnabled(true);
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

            UploadTask uploadTask = fileReference.putFile(thumbnailImageUri);

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
                        thumbnailUrl = task.getResult().toString();
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddPhotosActivity.this, "Thumbnail Uploaded.", Toast.LENGTH_SHORT).show();
                        uploadAdditionalImages();
                    } else {
                        // Handle Failures
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    deletePictureButton.setEnabled(true);
                    additionalImageButton.setEnabled(true);
                    thumbnailImageButton.setEnabled(true);
                    submitListingButton.setEnabled(true);

                    Log.d("AddPhotosActivity", "Upload thumbnail failed with", e);
                }
            }); // add on progress listener
        }
    }


    private void uploadAdditionalImages() {

        if(!ImageList.isEmpty()) {
            globalTempReference = additionalPicturesStorageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(ImageList.get(tempReference)));

            UploadTask uploadTask = globalTempReference.putFile(ImageList.get(tempReference));

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
                                additionalPhoto1_Url = uri.toString();
                                Toast.makeText(AddPhotosActivity.this, "Picture 1 Uploaded.", Toast.LENGTH_SHORT).show();

                                if (tempReference == ImageList.size() - 1) {
                                    createListing();
                                } else {
                                    tempReference++;
                                    uploadAdditionalImages();
                                }


                            } else if (tempReference == 1) {
                                additionalPhoto2_Url = uri.toString();
                                Toast.makeText(AddPhotosActivity.this, "Picture 2 Uploaded.", Toast.LENGTH_SHORT).show();

                                if (tempReference == ImageList.size() - 1) {
                                    createListing();
                                } else {
                                    tempReference++;
                                    uploadAdditionalImages();
                                }


                            } else if (tempReference == 2) {
                                additionalPhoto3_Url = uri.toString();
                                Toast.makeText(AddPhotosActivity.this, "Picture 3 Uploaded.", Toast.LENGTH_SHORT).show();

                                if (tempReference == ImageList.size() - 1) {
                                    createListing();
                                } else {
                                    tempReference++;
                                    uploadAdditionalImages();
                                }


                            } else if (tempReference == 3) {
                                additionalPhoto4_Url = uri.toString();
                                Toast.makeText(AddPhotosActivity.this, "Picture 4 Uploaded.", Toast.LENGTH_SHORT).show();

                                if (tempReference == ImageList.size() - 1) {
                                    createListing();
                                } else {
                                    tempReference++;
                                    uploadAdditionalImages();
                                }


                            } else {
                                Log.d("AddPhotosActivity", "IsSuccessful If statement chain failed... ");
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
        } else {
            createListing();
        }


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
        newPost.put("additionalPhoto1_Url", additionalPhoto1_Url);
        newPost.put("additionalPhoto2_Url", additionalPhoto2_Url);
        newPost.put("additionalPhoto3_Url", additionalPhoto3_Url);
        newPost.put("additionalPhoto4_Url", additionalPhoto4_Url);


        progressBar.setVisibility(View.VISIBLE);

        db.collection("postings").add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.INVISIBLE);

                        Log.d("AddPhotosActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                        nextActivity = new Intent(AddPhotosActivity.this, AppMainActivity.class);
                        startActivity(nextActivity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        deletePictureButton.setEnabled(true);
                        additionalImageButton.setEnabled(true);
                        thumbnailImageButton.setEnabled(true);
                        submitListingButton.setEnabled(true);

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
                    additionalPhotoUri = data.getData();
                    ImageList.add(additionalPhotoUri);
                    deletePictureButton.setVisibility(View.VISIBLE);
                    updateViewPager();
                    break;
                default:
                    break;
            }

        }
    }

    private void updateViewPager() {
        myViewPager.setAdapter(new AddPhotos_SlidingImage_Adapter(this, ImageList));
        indicator.setViewPager(myViewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        // Ensures that currentPage always has the current item and prevents index errors if indicator doesn't have to run/update the view
        currentPage = myViewPager.getCurrentItem();
        myViewPager.getAdapter().notifyDataSetChanged();
    }


}
