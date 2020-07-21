package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.adapter.ImagesDetailAdapter;
import com.example.project.adapter.ListImageAdapter;
import com.example.project.common.Constants;
import com.example.project.model.GetUrl;
import com.example.project.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailMyProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailMyProductFragment extends Fragment {

    Product product;
    ImageView btnBack;
    TextView textViewName;
    TextView textViewBrand;
    TextView textViewQuantity;
    TextView textViewColor;
    TextView textViewType;
    TextView textViewPrice;
    RecyclerView recyclerView;
    ImagesDetailAdapter adapter;
    ArrayList<String> listImage = new ArrayList<>();

    public DetailMyProductFragment() {
        // Required empty public constructor
    }


    public static DetailMyProductFragment newInstance(Product product) {
        DetailMyProductFragment fragment = new DetailMyProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PRODUCT_DETAILS, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(Constants.PRODUCT_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_my_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initAdapter();
        initData();
        initView();
        initAction();
    }

    void initWidget() {
        btnBack = getView().findViewById(R.id.btnBack);
        textViewBrand = getView().findViewById(R.id.textViewBrand);
        textViewName = getView().findViewById(R.id.textViewName);
        textViewQuantity = getView().findViewById(R.id.textViewQuantity);
        textViewColor = getView().findViewById(R.id.textViewColor);
        textViewPrice = getView().findViewById(R.id.textViewPrice);
        textViewType = getView().findViewById(R.id.textViewType);
        recyclerView = getView().findViewById(R.id.recyclerViewImageDetails);
    }

    void initView() {
        textViewName.setText(product.getName());
        textViewType.setText(product.getType());
        textViewBrand.setText(product.getBrand());
        textViewColor.setText(product.getColor());
        textViewQuantity.setText(String.valueOf(product.getQuantity()));
        textViewPrice.setText(String.format("%,d", product.getPrice()));
    }

    void initAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImagesDetailAdapter(listImage);
        recyclerView.setAdapter(adapter);
    }

    void initData() {
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
        data.child(product.getId()).child(Constants.LIST_IMAGES).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GetUrl getUrl = snapshot.getValue(GetUrl.class);
                listImage.add(getUrl.getImageUrl());
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