package com.example.project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.activity.buyer.DetailProductActivity;
import com.example.project.fragment.AddProductFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FragmentActivity fragmentActivity;

    public ListProductAdapter(List<Product> productList, FragmentActivity fragmentActivity) {
        this.productList = productList;
        this.fragmentActivity = fragmentActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageProduct;
        TextView txtProductName;
        TextView txtProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView_productList);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
        }

        void bindData(Product product) {
            Glide.with(itemView.getContext()).load(product.getMainImage()).into(imageProduct);
            txtProductName.setText(product.getName());
            txtProductPrice.setText(String.format("%,d",product.getPrice()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_layout_product_list,parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindData(productList.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailProductActivity.class);
                intent.putExtra("selectedProduct", productList.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
