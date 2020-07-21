package com.example.project.activity.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.project.R;
import com.example.project.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity {

    private ImageSlider imageSlider;
    private Button btnBack;
    private TextView txtProductName;
    private TextView txtUserName;
    private TextView txtCurrentQuantity;
    private TextView txtProductQuantity;
    private Button btnPlus;
    private Button btnMinus;
    private TextView txtColor;
    private TextView txtBrand;
    private TextView txtProductPrice;
    private Button btnAddCart;
    private Button btnBuyNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        imageSlider = findViewById(R.id.imageSlider_detailProduct);
        btnBack = findViewById(R.id.btnBack_detailProduct);
        txtProductName = findViewById(R.id.txtProductName_productDetail);
        txtUserName = findViewById(R.id.txtUsername_productDetail);
        txtCurrentQuantity = findViewById(R.id.txtCurrentQuantity_productDetail);
        txtProductQuantity = findViewById(R.id.txtProductQuantity_productDetail);
        btnPlus = findViewById(R.id.btnPlus_productDetail);
        btnMinus = findViewById(R.id.btnMinus_productDetail);
        txtColor = findViewById(R.id.txtColor_productDetail);
        txtBrand = findViewById(R.id.txtBrand_productDetail);
        txtProductPrice = findViewById(R.id.txtProductPrice_productDetail);
        btnAddCart = findViewById(R.id.btnAddCart_productDetail);
        btnBuyNow = findViewById(R.id.btnBuyNow_productDetail);

        //init
        Intent intent = getIntent();
        Product selectedProduct = (Product) intent.getSerializableExtra("selectedProduct");
        addImageToViewFlipper(selectedProduct.getImages());

        txtProductName.setText(selectedProduct.getName());
        //get username by userid
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(selectedProduct.getIdUser()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtUserName.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txtCurrentQuantity.setText(selectedProduct.getQuantity() == 0 ? "Not available" : "1");
        txtProductQuantity.setText(String.valueOf(selectedProduct.getQuantity()) + " products available");
        txtColor.setText(selectedProduct.getColor());
        txtBrand.setText(selectedProduct.getBrand());
        txtProductPrice.setText(String.format("%,d",selectedProduct.getPrice()));

        //Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   finish();
            }
        });

        final int productQuantity = selectedProduct.getQuantity();
        //Plus product's quantity
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(txtCurrentQuantity.getText().toString());
                if (currentQuantity < productQuantity) {
                    currentQuantity += 1;
                    txtCurrentQuantity.setText(String.valueOf(currentQuantity));
                }
            }
        });

        //Minus product's quantity
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(txtCurrentQuantity.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity -= 1;
                    txtCurrentQuantity.setText(String.valueOf(currentQuantity));
                }
            }
        });

        //Add to cart
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void addImageToViewFlipper(ArrayList<String> imageUrlList) {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
        }
        imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }

    public String getValueFromTextView(TextView textView) {
        return textView.getText().toString();
    }

    public void addToCart() {

    }

    public void insertOrder(String status) {

    }

}
