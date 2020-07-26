package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project.R;
import com.example.project.adapter.MyProductAdapter;
import com.example.project.adapter.SoldProductAdapter;
import com.example.project.common.Constants;
import com.example.project.model.SoldProduct;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellingManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellingManagerFragment extends Fragment {

    String idUser;
    ArrayList<SoldProduct> listProduct = new ArrayList<>();
    SoldProductAdapter adapter;
    RecyclerView recyclerView;
    ImageView btnBack;

    public SellingManagerFragment() {
        // Required empty public constructor
    }


    public static SellingManagerFragment newInstance(String idUser) {
        SellingManagerFragment fragment = new SellingManagerFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USER_ID, idUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUser = getArguments().getString(Constants.USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selling_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initWidget();
        initView();
        initData();
        initAction();
    }

    void initWidget() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        btnBack = getView().findViewById(R.id.btnBack);
    }

    void initAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SoldProductAdapter(listProduct);
        recyclerView.setAdapter(adapter);
    }

    void initData() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("SoldProduct");
        dataRef.orderByChild("idSeller").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SoldProduct soldProduct = snapshot.getValue(SoldProduct.class);
                for (int i = 0; i < listProduct.size(); i++) {
                    SoldProduct product = listProduct.get(i);
                    if (soldProduct.getIdProduct().equals(product.getIdProduct())) {
                        soldProduct.setQuantity(soldProduct.getQuantity() + product.getQuantity());
                        listProduct.remove(i);
                    }
                }
                listProduct.add(soldProduct);
                adapter.notifyDataSetChanged();
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
    }
}