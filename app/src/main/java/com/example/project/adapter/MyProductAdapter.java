package com.example.project.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.common.Constants;
import com.example.project.fragment.DetailMyProductFragment;
import com.example.project.model.Product;

import java.io.Serializable;
import java.util.ArrayList;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {
    FragmentActivity fragmentActivity;
    ArrayList<Product> listProducts;

    public MyProductAdapter(FragmentActivity fragmentActivity, ArrayList<Product> listProducts) {
        this.fragmentActivity = fragmentActivity;
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
        holder.myProductCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment detailMyProductFragment = DetailMyProductFragment.newInstance(listProducts.get(position));
                FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_add_fragment, detailMyProductFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView myProductCard;
        ImageView imageProduct;
        TextView nameProduct;
        TextView quantityProduct;
        TextView priceProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myProductCard = itemView.findViewById(R.id.myProductCard);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            nameProduct = itemView.findViewById(R.id.nameProduct);
            quantityProduct = itemView.findViewById(R.id.quantityProduct);
            priceProduct = itemView.findViewById(R.id.priceProduct);
        }

        void bindData(Product product) {
            nameProduct.setText(product.getName());
            priceProduct.setText(String.format("%,d", product.getPrice()));
            quantityProduct.setText(String.valueOf(product.getQuantity()));
            Glide.with(itemView.getContext())
                    .load(product.getMainImage())
                    .into(imageProduct);
        }
    }
}
