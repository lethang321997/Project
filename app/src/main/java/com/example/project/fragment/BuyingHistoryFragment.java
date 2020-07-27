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
import com.example.project.adapter.SoldProductAdapter;
import com.example.project.common.Constants;
import com.example.project.model.Order;
import com.example.project.model.SoldProduct;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyingHistoryFragment extends Fragment {

    public BuyingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buying_history, container, false);
    }

    String idUser;
    ArrayList<SoldProduct> listPaidOrder = new ArrayList<>();
    SoldProductAdapter adapter;
    RecyclerView recyclerView;
    Button btnBack;


    public static BuyingHistoryFragment newInstance(String idUser) {
        BuyingHistoryFragment fragment = new BuyingHistoryFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initView();
        initData();
        initAction();
    }

    void initWidget() {
        recyclerView = getView().findViewById(R.id.recyclerView_buyingHistory);
        btnBack = getView().findViewById(R.id.btnBack_buyingHistory);
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
        adapter = new SoldProductAdapter(listPaidOrder);
        recyclerView.setAdapter(adapter);
    }

    void initData() {
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("SoldProduct");
        dataRef.orderByChild("idBuyer").equalTo(idUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SoldProduct soldProduct = snapshot.getValue(SoldProduct.class);
                for (int i = 0; i < listPaidOrder.size(); i++) {
                    SoldProduct product = listPaidOrder.get(i);
                    if (soldProduct.getIdProduct().equals(product.getIdProduct())) {
                        soldProduct.setQuantity(soldProduct.getQuantity() + product.getQuantity());
                        listPaidOrder.remove(i);
                    }
                }
                listPaidOrder.add(soldProduct);
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
