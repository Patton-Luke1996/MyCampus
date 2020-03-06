package com.example.mycampus_application.ui.discounts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.R;

import java.util.ArrayList;

public class DiscountFragment extends Fragment {

    private DiscountViewModel discountViewModel;
private static final String TAG ="DiscountFragment";
    private ArrayList<String> mDiscountImage = new ArrayList<>();
    private ArrayList<String> mDiscountName = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discountViewModel =
                ViewModelProviders.of(this).get(DiscountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discount, container, false);

        final TextView textView = root.findViewById(R.id.text_discount);
        discountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        Log.d(TAG,"OnCreate Started");


         initImageBitmaps();


        RecyclerView recyclerView = root.findViewById(R.id.discount_recycler);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new DiscountAdapter(mDiscountImage, mDiscountName, this.getContext());
        recyclerView.setAdapter(mAdapter);

        return root;
    }

    //loads image and words onto Recyclerview
    private void initImageBitmaps()
    {

        mDiscountImage.add("https://calvin.edu/contentAsset/raw-data/e554071f-d535-4339-aa57-17c07812ca43/uploadableImage");
        mDiscountName.add("MCard Discounts");
    }
}