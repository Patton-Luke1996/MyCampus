package com.example.mycampus_application.ui.myPosts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycampus_application.MyPostsRecycleViewAdptr;
import com.example.mycampus_application.R;
import com.example.mycampus_application.postingDetailsActivity;
import com.example.mycampus_application.ui.home.HomePostingModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyPostsFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("postings");

    private FirestoreRecyclerAdapter<MyPostsModel, MyPostsViewHolder> adapter;

    private RecyclerView recycler;

    private Query query;

    public MyPostsFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       View rootView = inflater.inflate(R.layout.fragment_myposts, container, false);

       recycler = rootView.findViewById(R.id.mypostsRecycler);
       recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

       return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

        query = notebookRef.whereEqualTo("validPosting", true);

        FirestoreRecyclerOptions<MyPostsModel> options = new FirestoreRecyclerOptions.Builder<MyPostsModel>()
                .setQuery(query, MyPostsModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MyPostsModel, MyPostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyPostsViewHolder myPostsViewHolder, final int i, @NonNull MyPostsModel myPostsModel) {
                myPostsViewHolder.textview_itemName.setText(myPostsModel.getItemName());
                myPostsViewHolder.textview_itemCategory.setText(myPostsModel.getCategory());
                myPostsViewHolder.textview_itemDescription.setText(myPostsModel.getDescription());
                myPostsViewHolder.textview_itemQuantity.setText(myPostsModel.getQuantity());
                myPostsViewHolder.textview_itemPrice.setText(myPostsModel.getPrice());

                if ((myPostsModel.getThumbnailUrl()).matches("Tutoring")) {
                    Picasso.get().load(R.drawable.tutoring_thumbnail).into(myPostsViewHolder.imageview_thumbnailImage);
                } else {
                    Picasso.get().load(myPostsModel.getThumbnailUrl()).into(myPostsViewHolder.imageview_thumbnailImage);
                }

                myPostsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
            public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item,
                        parent, false);
                return new MyPostsViewHolder(view);

            }
        };

        recycler.setAdapter(adapter);
        adapter.startListening();

    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class MyPostsViewHolder extends RecyclerView.ViewHolder{

        TextView textview_itemName;
        TextView textview_itemCategory;
        TextView textview_itemDescription;
        TextView textview_itemQuantity;
        TextView textview_itemPrice;
        ImageView imageview_thumbnailImage;

        public MyPostsViewHolder(@NonNull View itemView) {
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