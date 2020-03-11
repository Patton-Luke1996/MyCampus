package com.example.mycampus_application.ui.home;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mycampus_application.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private ArrayList<String> mItemName;
    private ArrayList<String> mImages;
    private ArrayList<String> mQty;
    private ArrayList<String> mPrice;
    private ArrayList<String> mDescription;
    private ArrayList<String> mCategory;
    private Context mContext;


    public HomeRecyclerAdapter(ArrayList<String> mItemName, ArrayList<String> mImages,
                                   ArrayList<String> mQty, ArrayList<String> mPrice,
                                   ArrayList<String> mDescription, ArrayList<String> mCategory,
                                   Context mContext) {
        this.mItemName = mItemName;

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

        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.item_image );

        holder.item_name.setText(mItemName.get(position));
        holder.qty.setText(mQty.get(position));
        holder.category.setText(mCategory.get(position));
        holder.price.setText(mPrice.get(position));
        holder.description.setText(mDescription.get(position));



        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

            }

        });
    }

    @Override
    public int getItemCount() {


        return mItemName.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parentLayout;
        ImageView item_image;
        TextView category;
        TextView price;
        TextView description;
        TextView qty;
        TextView item_name;

        //temp
        public ViewHolder(View itemView){
            super(itemView);
            parentLayout = itemView.findViewById(R.id.listItem_parent_layout);
            item_image = itemView.findViewById(R.id.item_image);
            category = itemView.findViewById(R.id.item_category);
            price = itemView.findViewById(R.id.item_price);
            description = itemView.findViewById(R.id.item_description);
            qty = itemView.findViewById(R.id.item_quantity);
            item_name = itemView.findViewById(R.id.item_name);

        }
    }

}
