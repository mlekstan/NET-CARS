package com.example.shop_app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shop_app.MyNewRecyclerViewInterface;
import com.example.shop_app.R;
import com.example.shop_app.BasicCarInfo;

import java.util.List;

public class MyNewAdapter extends RecyclerView.Adapter<MyNewAdapter.MyNewViewHolder> {
    Context context;
    List<BasicCarInfo> listOfBasicCarInfo;
    private final MyNewRecyclerViewInterface classImplementingInterface;

    public MyNewAdapter(Context context, List<BasicCarInfo> listOfBasicCarInfo, MyNewRecyclerViewInterface classImplementingInterface) {
        this.context = context;
        this.listOfBasicCarInfo = listOfBasicCarInfo;
        this.classImplementingInterface = classImplementingInterface;
    }

    @NonNull
    @Override
    public MyNewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        MyNewViewHolder holder = new MyNewViewHolder(view, classImplementingInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyNewViewHolder holder, int position) {
        holder.brandAndModel.setText(listOfBasicCarInfo.get(position).getBrand() + " " + listOfBasicCarInfo.get(position).getModel());
        holder.yearOfProduction.setText("Year: " + listOfBasicCarInfo.get(position).getYearOfProduction() + " yr.");
        holder.price.setText("Price: " + listOfBasicCarInfo.get(position).getPrice() + " PLN");
        Glide.with(context).load(listOfBasicCarInfo.get(position).getMainImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return listOfBasicCarInfo.size();
    }

    public static class MyNewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView brandAndModel, price, yearOfProduction, mileage;

        public MyNewViewHolder(@NonNull View itemView, MyNewRecyclerViewInterface classImplementingInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewCarMainPhoto);
            brandAndModel = itemView.findViewById(R.id.textViewBrandAndModel);
            price = itemView.findViewById(R.id.textViewPrice);
            yearOfProduction = itemView.findViewById(R.id.textViewYearOfProduction);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (classImplementingInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            classImplementingInterface.onItemViewClick(position);
                        }
                    }
                }
            });

        }
    }

}
