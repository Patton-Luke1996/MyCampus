package com.example.mycampus_application.ui.myPosts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mycampus_application.NewListingActivity;
import com.example.mycampus_application.R;



public class MyPostsFragment extends Fragment implements View.OnClickListener{

    public MyPostsFragment() {
        // Required empty public constructor
    }

    private MyPostsViewModel myPostsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.fragment_myposts, container, false);


       ImageButton newPost = rootView.findViewById(R.id.addListing);

        newPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                moveToNewListing();
            }
        });


return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }

    public void moveToNewListing() {
        Intent i = new Intent(getActivity(), NewListingActivity.class);
        startActivity(i);
        //((Activity) getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View root) {

    }
}