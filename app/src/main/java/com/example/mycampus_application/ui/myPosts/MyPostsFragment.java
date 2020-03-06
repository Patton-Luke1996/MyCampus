package com.example.mycampus_application.ui.myPosts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.MyPostsRecycleViewAdptr;
import com.example.mycampus_application.R;

import java.util.ArrayList;


public class MyPostsFragment extends Fragment implements View.OnClickListener{

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mCategory = new ArrayList<>();


    public MyPostsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.fragment_myposts, container, false);




        RecyclerView recyclerView = rootView.findViewById(R.id.mypostsRecycler);
        MyPostsRecycleViewAdptr adapter = new MyPostsRecycleViewAdptr(mImageNames,mImages,mQty,mPrice,
                                                    mDescription, mCategory, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }
    private void initRecyclerView()
    {

    }


    @Override
    public void onClick(View root) {

    }
}