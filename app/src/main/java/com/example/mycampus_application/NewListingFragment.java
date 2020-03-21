package com.example.mycampus_application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


public class NewListingFragment extends AppCompatActivity {

    int code;
    Button submit;
    EditText price;
    EditText category;
    EditText quantity;
    EditText item;
    EditText description;
    Uri imageUri;
    Uri imageUri2;
    Uri imageUri3;
    ImageView itemPic;
    ImageView itemPic2;
    ImageView itemPic3;
    private static final int PICK_IMAGE =1;
    private static final int PICK_IMAGE2 =2;
    private static final int PICK_IMAGE3 =3;

    private String mParam1;
    private String mParam2;



    public NewListingFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_listing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Spinner categorySpinner =findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.array_category2,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        itemPic =  findViewById(R.id.itemPic);
        itemPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }

        });

        itemPic2 =  findViewById(R.id.itemPic2);
        itemPic2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE2);

            }

        });

        itemPic3 =  findViewById(R.id.itemPic3);
        itemPic3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE3);

            }

        });

        submit =  findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {

                Intent myIntent = new Intent(getBaseContext(),   AppMainActivity.class);
                startActivity(myIntent);
            }
        });




    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            itemPic.setImageURI(imageUri);
            return;
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE2)
        {
            imageUri2 = data.getData();
            itemPic2.setImageURI(imageUri2);
            return;
        }
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE3)
        {
            imageUri3 = data.getData();
            itemPic3.setImageURI(imageUri3);
            return;
        }

    }


}
