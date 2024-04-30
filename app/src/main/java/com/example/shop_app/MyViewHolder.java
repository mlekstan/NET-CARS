package com.example.shop_app;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView model, price, yearOfProduction;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        model = itemView.findViewById(R.id.textViewModel);
        price = itemView.findViewById(R.id.textViewPrice);
        yearOfProduction = itemView.findViewById(R.id.textViewYear);

    }
}
