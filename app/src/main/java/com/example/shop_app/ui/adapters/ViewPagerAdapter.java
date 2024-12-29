package com.example.shop_app.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.R;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerHolder> {
    private Uri[] imagesUri;
    private Context context;

    public ViewPagerAdapter(Uri[] imagesUri, Context context) {
        this.imagesUri = imagesUri;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.ViewPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_item, parent, false);
        ViewPagerHolder holder = new ViewPagerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.ViewPagerHolder holder, int position) {
        Uri imageUri = imagesUri[position];
        Glide.with(context).load(imageUri).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imagesUri.length;
    }

    public static class ViewPagerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewPagerHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPager);
        }
    }



}
