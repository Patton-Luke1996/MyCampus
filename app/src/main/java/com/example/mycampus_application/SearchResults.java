package com.example.mycampus_application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycampus_application.ui.home.HomePostingModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


public class SearchResults extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("postings");

    private FirestoreRecyclerAdapter<SearchResultsModel, SearchResultsViewHolder> adapter;

    private RecyclerView recycler;

    private String previousUserSelection = "";

    private Query query;



    public SearchResults() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        recycler = rootView.findViewById(R.id.searchResultsRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        previousUserSelection = getArguments().getString("Category");

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        query = notebookRef.whereEqualTo("category", previousUserSelection);

        FirestoreRecyclerOptions<SearchResultsModel> options = new FirestoreRecyclerOptions.Builder<SearchResultsModel>()
                .setQuery(query, SearchResultsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<SearchResultsModel, SearchResultsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SearchResultsViewHolder searchResultsViewHolder, final int i, @NonNull SearchResultsModel searchResultsModel) {
                searchResultsViewHolder.textview_itemName.setText(searchResultsModel.getItemName());
                searchResultsViewHolder.textview_itemCategory.setText(searchResultsModel.getCategory());
                searchResultsViewHolder.textview_itemDescription.setText(searchResultsModel.getDescription());
                searchResultsViewHolder.textview_itemQuantity.setText(searchResultsModel.getQuantity());
                searchResultsViewHolder.textview_itemPrice.setText(searchResultsModel.getPrice());

                if ((searchResultsModel.getThumbnailUrl()).matches("Tutoring")) {
                    Picasso.get().load(R.drawable.tutoring_thumbnail).into(searchResultsViewHolder.imageview_thumbnailImage);
                } else {
                    Picasso.get().load(searchResultsModel.getThumbnailUrl()).into(searchResultsViewHolder.imageview_thumbnailImage);
                }

                searchResultsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String docID = getSnapshots().getSnapshot(i).getId();

                        Intent postingInfo = new Intent(getActivity(), postingDetailsActivity.class);
                        postingInfo.putExtra("docID", docID);
                        startActivity(postingInfo);
                    }
                });

            }

            @NonNull
            @Override
            public SearchResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,
                        parent, false);
                return new SearchResultsViewHolder(view);

            }
        };

        recycler.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class SearchResultsViewHolder extends RecyclerView.ViewHolder{

        TextView textview_itemName;
        TextView textview_itemCategory;
        TextView textview_itemDescription;
        TextView textview_itemQuantity;
        TextView textview_itemPrice;
        ImageView imageview_thumbnailImage;

        public SearchResultsViewHolder(@NonNull View itemView) {
            super(itemView);

            textview_itemName = itemView.findViewById(R.id.item_name);
            textview_itemCategory = itemView.findViewById(R.id.item_category);
            textview_itemDescription = itemView.findViewById(R.id.item_description);
            textview_itemQuantity = itemView.findViewById(R.id.item_quantity);
            textview_itemPrice = itemView.findViewById(R.id.item_price);
            imageview_thumbnailImage = itemView.findViewById(R.id.item_image);
        }
    }


}
