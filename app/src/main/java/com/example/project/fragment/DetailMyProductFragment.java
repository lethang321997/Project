package com.example.project.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.project.R;
import com.example.project.common.Constants;
import com.example.project.model.GetUrl;
import com.example.project.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailMyProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailMyProductFragment extends Fragment {

    public static Product product;
    ImageView btnBack;
    TextView textViewName;
    TextView textViewBrand;
    TextView textViewQuantity;
    TextView textViewColor;
    TextView textViewType;
    TextView textViewPrice;
    ImageSlider imageSlider;
    Button btnEditProduct;
    Button btnDeleteProduct;

    List<SlideModel> listImages = new ArrayList<>();

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
        imageSlider = getView().findViewById(R.id.imageSlider);
        btnEditProduct = getView().findViewById(R.id.btnEditProduct);
        btnDeleteProduct = getView().findViewById(R.id.btnDeleteProduct);
    }


    void initView() {
        textViewName.setText(product.getName());
        textViewType.setText(product.getType());
        textViewBrand.setText(product.getBrand());
        textViewColor.setText(product.getColor());
        textViewQuantity.setText(String.valueOf(product.getQuantity()));
        textViewPrice.setText(String.format("%,d", product.getPrice()));
        imageSlider.setImageList(listImages, ScaleTypes.CENTER_INSIDE);
    }

    void initAction() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();

            }
        });
        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment editProductFragment = EditProductFragment.newInstance(product);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_add_fragment, editProductFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setMessage("Do you want to delete product")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
                                data.child(product.getId()).removeValue();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }


    void initData() {
        ArrayList<String> listImage;
        listImage = product.getImages();
        listImages.clear();
        for (String imageUrl : listImage) {
            listImages.add(new SlideModel(imageUrl, ScaleTypes.FIT));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}