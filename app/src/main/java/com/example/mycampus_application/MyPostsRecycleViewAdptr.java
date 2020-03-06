package com.example.mycampus_application;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostsRecycleViewAdptr extends RecyclerView.Adapter<MyPostsRecycleViewAdptr.ViewHolder>{

    private static final String TAG = "MyPostsRecycleViewAdptr";
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mCategory = new ArrayList<>();
    private Context mContext;

    public MyPostsRecycleViewAdptr(ArrayList<String> mImageNames, ArrayList<String> mImages,
                                   ArrayList<String> mQty, ArrayList<String> mPrice,
                                   ArrayList<String> mDescription, ArrayList<String> mCategory,
                                   Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mQty = mQty;
        this.mPrice = mPrice;
        this.mDescription = mDescription;
        this.mCategory = mCategory;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG,"onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.item_image );

        holder.item_name.setText(mImageNames.get(position));
        holder.qty.setText(mQty.get(position));
        holder.category.setText(mCategory.get(position));
        holder.price.setText(mPrice.get(position));
        holder.description.setText(mDescription.get(position));



        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG,"onCLick: clicked on: " +mImageNames.get(position));
            }

        });
    }

    @Override
    public int getItemCount() {


        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parentLayout;
        ImageView item_image;
        TextView category;
        TextView price;
        TextView description;
        TextView qty;
        TextView item_name;

        public ViewHolder(View itemView){
            super(itemView);
        }
    }

}
