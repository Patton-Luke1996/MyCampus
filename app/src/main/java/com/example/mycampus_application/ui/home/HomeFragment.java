package com.example.mycampus_application.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.R;
import com.example.mycampus_application.postingDetailsActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("postings");

    private FirestoreRecyclerAdapter<HomePostingModel, HomePostsViewHolder> adapter;

    private RecyclerView recycler;

    private Query query;

    public HomeFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recycler = rootView.findViewById(R.id.homeRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));        ;



        return rootView;
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();

        query = notebookRef.whereEqualTo("validPosting", true);


        FirestoreRecyclerOptions<HomePostingModel> options = new FirestoreRecyclerOptions.Builder<HomePostingModel>()
                .setQuery(query, HomePostingModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<HomePostingModel, HomePostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HomePostsViewHolder homePostsViewHolder, final int i, @NonNull HomePostingModel homePostingModel) {
                homePostsViewHolder.textview_itemName.setText(homePostingModel.getItemName());
                homePostsViewHolder.textview_itemCategory.setText(homePostingModel.getCategory());
                homePostsViewHolder.textview_itemDescription.setText(homePostingModel.getDescription());
                homePostsViewHolder.textview_itemQuantity.setText(homePostingModel.getQuantity());
                homePostsViewHolder.textview_itemPrice.setText(homePostingModel.getPrice());


                homePostsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
            public HomePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,
                        parent, false);
                return new HomePostsViewHolder(view);

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

   public static class HomePostsViewHolder extends RecyclerView.ViewHolder{

       TextView textview_itemName;
       TextView textview_itemCategory;
       TextView textview_itemDescription;
       TextView textview_itemQuantity;
       TextView textview_itemPrice;

       public HomePostsViewHolder(@NonNull View itemView) {
           super(itemView);

           textview_itemName = itemView.findViewById(R.id.item_name);
           textview_itemCategory = itemView.findViewById(R.id.item_category);
           textview_itemDescription = itemView.findViewById(R.id.item_description);
           textview_itemQuantity = itemView.findViewById(R.id.item_quantity);
           textview_itemPrice = itemView.findViewById(R.id.item_price);



       }
   }
}