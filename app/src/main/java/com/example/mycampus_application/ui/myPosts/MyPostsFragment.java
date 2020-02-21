package com.example.mycampus_application.ui.myPosts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
                Navigation.findNavController(view).navigate(R.id.action_nav_myPosts_to_newListingFragment);
            }
        });


return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }



    @Override
    public void onClick(View root) {

    }
}