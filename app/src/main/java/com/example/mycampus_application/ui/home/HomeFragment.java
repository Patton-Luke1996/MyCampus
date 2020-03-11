package com.example.mycampus_application.ui.home;

import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ArrayList<String> mItemName = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mCategory = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private HomeViewModel homeViewModel;

    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        initRecyclerView();

        recyclerView = root.findViewById(R.id.homeRecycler);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mAdapter = new HomeRecyclerAdapter(mItemName,mImages,mQty,mPrice,
                mDescription, mCategory, this.getContext());
        recyclerView.setAdapter(mAdapter);

        db = FirebaseFirestore.getInstance();

        loadAllPostings();

        return root;
    }

    private void loadAllPostings() {
        db.collection("postings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                //add to viewlist to display

                            }
                        } else {

                        }
                    }
                });
    }


    private void initRecyclerView()
    {
        mItemName.add("Purple Couch");
        mCategory.add("Furniture");
        mDescription.add("A purple couch that is in decent condition");
        mPrice.add("150");
        mQty.add("1");
        mImages.add("https://5.imimg.com/data5/BC/SG/MY-24423473/sofa-set-500x500.jpg");

    }
}