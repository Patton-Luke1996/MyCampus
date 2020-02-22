package com.example.mycampus_application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import static android.app.Activity.RESULT_OK;


public class NewListingFragment extends Fragment{

    int code;
    Button submit;
    EditText price;
    EditText category;
    EditText quantity;
    EditText item;
    EditText description;
    Uri imageUri;
    ImageView itemPic;
    private static final int PICK_IMAGE =1;

    private String mParam1;
    private String mParam2;



    public NewListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_listing, container, false);



        Spinner categorySpinner =view.findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this.getActivity(), R.array.array_category2,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        itemPic = view.findViewById(R.id.itemPic);
        itemPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }

        });

        submit = view.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Navigation.findNavController(view).navigate(R.id.action_newListingFragment_to_nav_myPosts);
            }
        });


        return view;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            itemPic.setImageURI(imageUri);
        }
    }


}
