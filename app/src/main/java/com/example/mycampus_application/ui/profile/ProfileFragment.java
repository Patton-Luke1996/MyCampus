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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycampus_application.AppMainActivity;
import com.example.mycampus_application.CreateProfileActivity;
import com.example.mycampus_application.R;
import com.example.mycampus_application.VerifyPhoneNumberActivity;
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
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private EditText displayNametv;
    private EditText firstNametv;
    private EditText lastNametv;
    private EditText phoneNumbertv;

    private Button saveChangesBTN;
    private Button verifyPhoneNumbBTN;

    private String currentUser = "";
    private String institution = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        displayNametv = root.findViewById(R.id.username_et);
        firstNametv = root.findViewById(R.id.fname_et);
        lastNametv = root.findViewById(R.id.lname_et);
        phoneNumbertv = root.findViewById(R.id.phone_et);
        saveChangesBTN = root.findViewById(R.id.save_btn);

        saveChangesBTN = root.findViewById(R.id.save_btn);
        verifyPhoneNumbBTN = root.findViewById(R.id.verifyPhoneNumber);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        currentUser = user.getUid();

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
                        phoneNumbertv.setText(document.get("phoneNumber").toString());
                        institution = document.getString("institution");
                    }
                } else {
                    Log.d("LoginActivity", "Get failed with", task.getException());
                }
            }
        });

        saveChangesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overwriteProfile();
            }
        });

        verifyPhoneNumbBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifyPhoneNumberActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }

    private void overwriteProfile() {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("display_name", displayNametv.getText().toString());
        userProfile.put("first_name", firstNametv.getText().toString());
        userProfile.put("last_name", lastNametv.getText().toString());
        userProfile.put("institution", institution);
        userProfile.put("phoneNumber", phoneNumbertv.getText().toString());

        db.collection("user_profiles").document(currentUser)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Profile Fragment", "Document overwritten!");
                        Toast.makeText(getActivity(), "Changes saved successfully.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getActivity(), AppMainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Profile Fragment", "Error Writing Document", e);
                        Toast.makeText(getActivity(), "Error overwriting profile data. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}