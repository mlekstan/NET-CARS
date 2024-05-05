package com.example.shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyNewAdapter extends RecyclerView.Adapter<MyNewAdapter.MyNewViewHolder> {
    Context context;
    List<BasicCarInfo> listOfBasicCarInfo;

    public MyNewAdapter(Context context, List<BasicCarInfo> listOfBasicCarInfo) {
        this.context = context;
        this.listOfBasicCarInfo = listOfBasicCarInfo;
    }

    @NonNull
    @Override
    public MyNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        MyNewViewHolder holder = new MyNewViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyNewViewHolder holder, int position) {
        holder.brandAndModel.setText(listOfBasicCarInfo.get(position).getBrand() + " " + listOfBasicCarInfo.get(position).getModel());
        holder.yearOfProduction.setText(listOfBasicCarInfo.get(position).getYearOfProduction() + " yr.");
        holder.price.setText(listOfBasicCarInfo.get(position).getPrice() + " PLN");
        Glide.with(context).load(listOfBasicCarInfo.get(position).getMainImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listOfBasicCarInfo.size();
    }

    public static class MyNewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView brandAndModel, price, yearOfProduction;

        public MyNewViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCarMainPhoto);
            brandAndModel = itemView.findViewById(R.id.textViewBrandAndModel);
            price = itemView.findViewById(R.id.textViewPrice);
            yearOfProduction = itemView.findViewById(R.id.textViewYearOfProduction);

        }
    }

}
