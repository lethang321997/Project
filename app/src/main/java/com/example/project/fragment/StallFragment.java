package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.adapter.MyProductAdapter;
import com.example.project.common.Constants;
import com.example.project.model.GetUrl;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StallFragment extends Fragment {
    Button btnAddItem;
    RecyclerView recyclerView;
    ArrayList<Product> listProducts = new ArrayList<>();

    User user;
    MyProductAdapter adapter;

    public StallFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stall, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
        initView();
        initAction();
    }

    void initWidget() {
        btnAddItem = getView().findViewById(R.id.btnAddItem);
        recyclerView = getView().findViewById(R.id.recyclerViewMyItems);
    }

    void initAction() {
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_add_fragment, new AddProductFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyProductAdapter(getActivity(), listProducts);
        recyclerView.setAdapter(adapter);
    }

    void initData() {
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
        listProducts.clear();
        final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
        data.orderByChild(Constants.USER_ID).equalTo(user.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                ArrayList<String> listImagesUrl = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(Constants.LIST_IMAGES)) {
                        for (DataSnapshot dataGetImage : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataGetImageUrl : dataGetImage.getChildren()) {
                                String imageUrl = dataGetImageUrl.getValue(String.class);
                                listImagesUrl.add(imageUrl);
                            }
                        }
                    }
                }
                product.setImages(listImagesUrl);
                listProducts.add(product);
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
