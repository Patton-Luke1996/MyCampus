package com.example.mycampus_application.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.R;
import com.example.mycampus_application.SearchResults;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public Button furniture_btn;
    public Button book_btn;
    public Button tutor_btn;
    public Button tech_btn;
    public Button housing_btn;
    public Button sublease_btn;
    public Button other_btn;
    public Button household_btn;
    public SearchView searchbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);



        furniture_btn = root.findViewById(R.id.furniture_btn);
        furniture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Furniture");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        book_btn = root.findViewById(R.id.books_btn);
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Textbooks");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        tutor_btn = root.findViewById(R.id.tutor_btn);
        tutor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Tutoring");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        tech_btn = root.findViewById(R.id.tech_btn);
        tech_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Technology");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        housing_btn = root.findViewById(R.id.housing_btn);
        housing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Housing");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        sublease_btn = root.findViewById(R.id.sublease_btn);
        sublease_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Sub-Leasings");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        other_btn = root.findViewById(R.id.other_btn);
        other_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Other");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });

        household_btn = root.findViewById(R.id.household_items_btn);
        household_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("Category", "Household Items");
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_searchResults, bundle);
            }
        });


        return root;



    }




}