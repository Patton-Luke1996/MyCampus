package com.example.mycampus_application.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycampus_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    Uri imageUri;
    ImageView profilePic;
    private static final int PICK_IMAGE =1;

    private ProfileViewModel toolsViewModel;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private EditText displayNametv;
    private EditText firstNametv;
    private EditText lastNametv;
    private EditText phoneNumbertv;

    private Button saveChangesBTN;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePic = root.findViewById(R.id.profile_image);
        displayNametv = root.findViewById(R.id.username_et);
        firstNametv = root.findViewById(R.id.fname_et);
        lastNametv = root.findViewById(R.id.lname_et);
        phoneNumbertv = root.findViewById(R.id.phone_et);
        saveChangesBTN = root.findViewById(R.id.save_btn);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        String currentUser = user.getUid();

        Log.d("ProfileFragment", currentUser);

        DocumentReference docRef = db.collection("user_profiles").document(currentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        displayNametv.setText(document.get("display_name").toString());
                        firstNametv.setText(document.get("first_name").toString());
                        lastNametv.setText(document.get("last_name").toString());
                    }
                } else {
                    Log.d("LoginActivity", "Get failed with", task.getException());
                }
            }
        });




        profilePic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }

        });


        return root;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
        }
    }

}