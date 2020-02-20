package com.example.mycampus_application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

 import com.example.mycampus_application.ui.myPosts.MyPostsFragment;

import androidx.appcompat.app.AppCompatActivity;

public class NewListingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_listing);

            populateSpinner();
            setItemPic();

            price = findViewById(R.id.etPrice);

           submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            goToMyPosts();
            }
        });

        }

        public void goToMyPosts()
        {
           /*Intent i = new Intent(getActivity(), NewListingActivity.class);
           startActivity(i);*/
        }

        public void setItemPic() {
            itemPic = findViewById(R.id.itemPic);
            itemPic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    openGallery();

                }

            }
            );
        }


       // @Override
       protected  void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
            {
                imageUri = data.getData();
                itemPic.setImageURI(imageUri);
            }
        }

        private void openGallery()
        {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);


        }

        public void populateSpinner() {

            Spinner categorySpinner =findViewById(R.id.categorySpinner);
            ArrayAdapter<CharSequence> adapter =
                    ArrayAdapter.createFromResource(this, R.array.array_category,
                            android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
            categorySpinner.setOnItemSelectedListener( this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

            String text = adapterView.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


