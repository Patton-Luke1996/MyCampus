package com.example.mycampus_application.ui.discounts;

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

public class DiscountFragment extends Fragment {

    private DiscountViewModel discountViewModel;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        discountViewModel =
                ViewModelProviders.of(this).get(DiscountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discount, container, false);

        recyclerView = root.findViewById(R.id.discount_recycler);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
       // mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        final TextView textView = root.findViewById(R.id.text_discount);
        discountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}