package com.example.project.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.adapter.ListOrderedProductAdapter;
import com.example.project.common.Constants;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.model.SoldProduct;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtTotalPrice;
    private Button btnBuyNow;
    User user;
    int totalPrice = 0;
    Context context;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        user = mainActivity.getUser();
        context = this.getContext();

        recyclerView = view.findViewById(R.id.recyclerView_orderedProduct);
        txtTotalPrice = view.findViewById(R.id.txtTotalPrice_orderedProduct);
        btnBuyNow = view.findViewById(R.id.btnBuyNow_orderedProduct);

    }

    @Override
    public void onStart() {
        super.onStart();
        //set recycler view
        totalPrice = 0;
        final List<Order> orderList = new ArrayList<>();
        final ListOrderedProductAdapter listOrderedProductAdapter = new ListOrderedProductAdapter((MainActivity) getActivity(), orderList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setAdapter(listOrderedProductAdapter);
        databaseReference.child("Order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                final Order order = snapshot.getValue(Order.class);
                if (order.getUserId().equals(user.getId()) && order.getStatus().equals("confirming")) {
                    orderList.add(order);
                    int orderedQuantity = order.getOrderedQuantity();
                    int orderedPrice = order.getOrderedPrice();
                    totalPrice += orderedPrice * orderedQuantity;
                    txtTotalPrice.setText(String.format("%,d", totalPrice) + " VND ");
                    listOrderedProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

        //Buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            TextView txtAddress;
            Button btnConfirm;
            Button btnCancel;

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_insert_address);
                txtAddress = dialog.findViewById(R.id.txtAddress_orderedProduct);
                btnConfirm = dialog.findViewById(R.id.btnConfirmAddress_orderedProduct);
                btnCancel = dialog.findViewById(R.id.btnCancelAddress_orderedProduct);
                txtAddress.setText(user.getAddress());

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Update address and status
                        String address = txtAddress.getText().toString();
                        String status = "paid";
                        dialog.dismiss();
                        updateOrder(orderList, address, status);
                        addSoldProduct(orderList);
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void updateOrder(List<Order> orderList, String address, String status) {
        //check money
        if (user.getMoney() >= totalPrice) {
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            for (int i = 0; i < orderList.size(); i++) {
                final Order indexOrder = orderList.get(i);
                databaseReference.child("Order").child(indexOrder.getId()).child("orderedAddress").setValue(address);
                databaseReference.child("Order").child(indexOrder.getId()).child("status").setValue(status);
                //update seller's money
                databaseReference.child("Product").child(indexOrder.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final Product product = snapshot.getValue(Product.class);
                        databaseReference.child("User").child(product.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                int orderedQuantity = indexOrder.getOrderedQuantity();
                                int orderedPrice = indexOrder.getOrderedPrice();
                                int paidMoney = orderedPrice * orderedQuantity;
                                int currentMoneyOfUser = user.getMoney();
                                databaseReference.child("User").child(product.getIdUser()).child("money").setValue(paidMoney + currentMoneyOfUser);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            //update user's money
            //buyer
            int userMoney = user.getMoney();
            int updatedMoney = userMoney - totalPrice;
            databaseReference.child("User").child(user.getId()).child("money").setValue(updatedMoney);
            Toast.makeText(context, "Buy products successfully! Thank you!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "You're not enough money to buy products", Toast.LENGTH_SHORT).show();
        }
    }

    void addSoldProduct(List<Order> orderList) {
        for (Order order : orderList) {
            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
            final String idProduct = order.getProductId();
            final String idBuyer = order.getUserId();
            final int quantity = order.getOrderedQuantity();
            final int price = order.getOrderedPrice();
            data.child(idProduct).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("SoldProduct");
                    Product product = snapshot.getValue(Product.class);
                    String idSeller = product.getIdUser();
                    String productName = product.getName();
                    String image = product.getMainImage();
                    String id = dataRef.push().getKey();
                    SoldProduct soldProduct = new SoldProduct(id, idProduct, productName, idSeller, idBuyer, quantity, price, image);
                    dataRef.child(id).setValue(soldProduct);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }
}
