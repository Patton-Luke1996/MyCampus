package com.example.mycampus_application.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycampus_application.R;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    Uri imageUri;
    ImageView profilePic;
    private static final int PICK_IMAGE =1;

    private ProfileViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragement_profile, container, false);

      /*  Spinner daysSpinner =root.findViewById(R.id.renewSpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this.getActivity(), R.array.days,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(adapter);*/

        profilePic = root.findViewById(R.id.profile_image);
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