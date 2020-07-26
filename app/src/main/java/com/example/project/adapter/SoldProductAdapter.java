package com.example.project.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;

import com.example.project.model.SoldProduct;

import java.io.Serializable;
import java.util.ArrayList;

public class SoldProductAdapter extends RecyclerView.Adapter<SoldProductAdapter.ViewHolder> {
    ArrayList<SoldProduct> listProducts;

    public SoldProductAdapter(ArrayList<SoldProduct> listProducts) {
        this.listProducts = listProducts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_product, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bindData(listProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView nameProduct;
        TextView quantityProduct;
        TextView priceProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            quantityProduct = itemView.findViewById(R.id.quantityProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
        }

        void bindData(SoldProduct product) {
            nameProduct.setText(product.getNameProduct());
            priceProduct.setText(String.format("%,d", product.getPrice()));
            quantityProduct.setText(String.valueOf(product.getQuantity()));
            Glide.with(itemView.getContext())
                    .load(product.getImage())
                    .into(imageProduct);
        }
    }
}
