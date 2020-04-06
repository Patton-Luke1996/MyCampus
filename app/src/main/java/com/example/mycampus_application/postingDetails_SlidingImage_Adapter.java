package com.example.mycampus_application;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postingDetails_SlidingImage_Adapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;

    postingDetails_SlidingImage_Adapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View imageLayout = inflater.inflate(R.layout.layout_postingdeatils_imageslider, container, false);
        assert imageLayout != null;

        final ImageView imageView = imageLayout.findViewById(R.id.userImage_postingDetails);

        // set flag to catch category images and display drawable if needed in imageView
        if(IMAGES.get(position).matches("Tutoring")) {
            Picasso.get().load(R.drawable.tutoring_thumbnail).into(imageView);
        } else if (!IMAGES.get(position).matches("")){
            Picasso.get().load(IMAGES.get(position)).into(imageView);
        } else {
            return imageLayout;
        }

        container.addView(imageLayout);

        return imageLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
