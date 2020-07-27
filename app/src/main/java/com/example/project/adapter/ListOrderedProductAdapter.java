package com.example.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.fragment.CartFragment;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListOrderedProductAdapter extends RecyclerView.Adapter<ListOrderedProductAdapter.ViewHolder> {

    FragmentActivity fragmentActivity;
    List<Order> orderList;
    List<Product> checkedOrderedProductList = new ArrayList<>();

    public ListOrderedProductAdapter(FragmentActivity fragmentActivity, List<Order> orderList) {
        this.fragmentActivity = fragmentActivity;
        this.orderList = orderList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox_orderedProduct;
        ImageView imageProduct_orderedProduct;
        TextView txtProductName_orderedProduct;
        TextView txtOrderedQuantity_orderedProduct;
        TextView txtProductPrice_orderedProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox_orderedProduct = itemView.findViewById(R.id.ckBox_orderedProduct);
            imageProduct_orderedProduct = itemView.findViewById(R.id.imgProduct_orderedProduct);
            txtProductName_orderedProduct = itemView.findViewById(R.id.txtProductName_orderedProduct);
            txtOrderedQuantity_orderedProduct = itemView.findViewById(R.id.txtOrderedQuantity_orderedProduct);
            txtProductPrice_orderedProduct = itemView.findViewById(R.id.txtProductPrice_orderedProduct);
        }

        void bindData(Product product, Order order) {
            Glide.with(itemView.getContext()).load(product.getMainImage()).into(imageProduct_orderedProduct);
            txtProductName_orderedProduct.setText(product.getName());
            txtOrderedQuantity_orderedProduct.setText(String.valueOf(order.getOrderedQuantity()));
            txtProductPrice_orderedProduct.setText(String.valueOf(product.getPrice()) + " VND ");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_layout_ordered_product, parent, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Order order = orderList.get(position);
        final Product[] product = {null};
        //get product of order -> bindData
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product indexPoduct = snapshot.getValue(Product.class);
                if (indexPoduct.getId().equals(order.getProductId())) {
                    product[0] = indexPoduct;
                    holder.bindData(product[0], order);
                }

                holder.checkBox_orderedProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            CartFragment.selectedOrderList.add(order);
                            CartFragment.setTotalPrice();
                        } else {
                            CartFragment.selectedOrderList.remove(order);
                            CartFragment.setTotalPrice();
                        }
                    }
                });
                holder.checkBox_orderedProduct.setChecked(true);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product indexPoduct = snapshot.getValue(Product.class);
                if (indexPoduct.getId().equals(order.getProductId())) {
                    product[0] = indexPoduct;
                    holder.bindData(product[0], order);
                }

                holder.checkBox_orderedProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                        }
                    }
                });
                holder.checkBox_orderedProduct.setChecked(true);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public List<Product> getCheckedOrderedProductList() {
        return checkedOrderedProductList;
    }
}
