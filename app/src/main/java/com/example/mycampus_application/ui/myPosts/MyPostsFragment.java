package com.example.mycampus_application.ui.myPosts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.MyPostsRecycleViewAdptr;
import com.example.mycampus_application.R;

import java.util.ArrayList;


public class MyPostsFragment extends Fragment {
    private static final String TAG ="MyPostsFragment";
    private ArrayList<String> mItemName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mCategory = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.fragment_myposts, container, false);


        initRecyclerView();

       recyclerView = rootView.findViewById(R.id.mypostsRecycler);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new MyPostsRecycleViewAdptr(mItemName,mImages,mQty,mPrice,
                                                    mDescription, mCategory, this.getContext());
        recyclerView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

    }

    //loads into recyclerview
    private void initRecyclerView()
    {
        mItemName.add("MCard Discounts");
        mCategory.add("MCard Discounts");
        mDescription.add("MCard Discounts");
        mPrice.add("MCard Discounts");
        mQty.add("MCard Discounts");
        mImages.add("https://calvin.edu/contentAsset/raw-data/e554071f-d535-4339-aa57-17c07812ca43/uploadableImage");

    }




}