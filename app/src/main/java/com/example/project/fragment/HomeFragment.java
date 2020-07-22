package com.example.project.fragment;

import android.graphics.ImageDecoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.adapter.DatabaseAdapter;
import com.example.project.adapter.ListProductAdapter;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtMoney;
    private EditText txtSearch;

    DatabaseAdapter databaseAdapter = new DatabaseAdapter();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    List<Product> productList;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        productList = new ArrayList<>();
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_listProduct);
        txtSearch = view.findViewById(R.id.txtSearch);
        txtMoney = view.findViewById(R.id.txtMoney_home);

        MainActivity mainActivity = (MainActivity) getActivity();
        final User user = mainActivity.getUser();
        txtMoney.setText(String.format("%,d", user.getMoney()) + "VND ");

        final ListProductAdapter listProductAdapter = new ListProductAdapter(productList, getActivity());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setAdapter(listProductAdapter);
        databaseReference.child("Product").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);

                if (!product.getIdUser().equals(user.getId())) {
                    //get list Image's url
                    ArrayList<String> listImageUrl = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("listImage")) {
                            listImageUrl = new ArrayList<>();
                            for (DataSnapshot dataSnapshot_image : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot_imageUrl : dataSnapshot_image.getChildren()) {
                                    String imageUrl = dataSnapshot_imageUrl.getValue(String.class);
                                    listImageUrl.add(imageUrl);
                                }
                            }
                        }
                    }
                    product.setImages(listImageUrl);
                    productList.add(product);
                    listProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);

                if (!product.getIdUser().equals(user.getId())) {
                    //get list Image's url
                    ArrayList<String> listImageUrl = null;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals("listImage")) {
                            listImageUrl = new ArrayList<>();
                            for (DataSnapshot dataSnapshot_image : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot_imageUrl : dataSnapshot_image.getChildren()) {
                                    String imageUrl = dataSnapshot_imageUrl.getValue(String.class);
                                    listImageUrl.add(imageUrl);
                                }
                            }
                        }
                    }
                    product.setImages(listImageUrl);
                    productList.add(product);
                    listProductAdapter.notifyDataSetChanged();
                }
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
