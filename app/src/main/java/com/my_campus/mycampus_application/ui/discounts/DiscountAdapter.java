package com.my_campus.mycampus_application.ui.discounts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my_campus.mycampus_application.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {

    //array that holds names and images
    private ArrayList<String> mDiscountImage;
    private ArrayList<String> mDiscountName;
    private Context mDiscountContext;

    public DiscountAdapter(ArrayList<String> mDiscountImage, ArrayList<String> mDiscountName, Context mDiscountContext) {
        this.mDiscountImage = mDiscountImage;
        this.mDiscountName = mDiscountName;
        this.mDiscountContext = mDiscountContext;
    }

    @NonNull
    @Override
    public DiscountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_discount,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //Method to append image and name to RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Glide API for fethcing images

        Glide.with(mDiscountContext)
                .asBitmap()
                .load(mDiscountImage.get(position))
                .into(holder.discount_image);

        holder.discount_name.setText(mDiscountName.get(position));
        //opens link for that position
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://www.finance.umich.edu/treasury/mcard/discounts";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mDiscountContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDiscountName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView discount_image;
        TextView discount_name;
        RelativeLayout parent_layout;


        public ViewHolder(View itemView) {
            super(itemView);
            discount_image = itemView.findViewById(R.id.discount_image);
            discount_name = itemView.findViewById(R.id.discount_name);
            parent_layout = itemView.findViewById(R.id.discount_parent_layout);

        }
    }



}
