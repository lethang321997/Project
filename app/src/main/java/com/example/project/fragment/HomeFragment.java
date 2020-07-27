package com.example.project.fragment;

import android.graphics.Color;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtMoney;
    private EditText txtSearch;
    private Spinner spinner;
    private Button btnSearch;
    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
    boolean firstLoad = true;

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
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initSpinner();
        initAction();
        initData();
    }

    void initWidget() {
        recyclerView = getView().findViewById(R.id.recyclerView_listProduct);
        txtSearch = getView().findViewById(R.id.txtSearch);
        txtMoney = getView().findViewById(R.id.txtMoney_home);
        spinner = getView().findViewById(R.id.spinnerType);
        btnSearch = getView().findViewById(R.id.btnSearch);
    }

    void initSpinner() {
        String[] listType = getResources().getStringArray(R.array.product_type);
        ArrayList<String> listTypes = new ArrayList<>(Arrays.asList(listType));
        listTypes.add(0, "All");
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listTypes);
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterType);

    }

    void initAction() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ArrayList<Product> newList = new ArrayList<>();
                    String type = parent.getItemAtPosition(position).toString();
                    for (Product product : productList) {
                        if (product.getType().equals(type)) {
                            newList.add(product);
                        }
                    }
                    ListProductAdapter listProductAdapter = new ListProductAdapter(newList, getActivity());
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                            StaggeredGridLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    recyclerView.setAdapter(listProductAdapter);
                    firstLoad = false;
                } else {
                    if (!firstLoad) {
                        ListProductAdapter listProductAdapter = new ListProductAdapter(productList, getActivity());
                        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                                StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                        recyclerView.setAdapter(listProductAdapter);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = txtSearch.getText().toString().toLowerCase();
                ArrayList<Product> searchList = new ArrayList<>();
                ListProductAdapter listProductAdapter = new ListProductAdapter(searchList, getActivity());
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(listProductAdapter);
                for (Product product : productList) {
                    if (product.getName().toLowerCase().contains(searchText)) {
                        searchList.add(product);
                        listProductAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    void initData() {
        productList = new ArrayList<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final User[] user = {MainActivity.user};
        databaseReference.child("User").orderByChild("id").equalTo(user[0].getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user[0] = snapshot.getValue(User.class);
                txtMoney.setText(String.format("%,d", user[0].getMoney()) + " VND ");
                final ListProductAdapter listProductAdapter = new ListProductAdapter(productList, getActivity());
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.setAdapter(listProductAdapter);
                databaseReference.child("Product").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);

                        if (!product.getIdUser().equals(user[0].getId())) {
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
                        productList.clear();
                        Product product = snapshot.getValue(Product.class);

                        if (!product.getIdUser().equals(user[0].getId())) {
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
