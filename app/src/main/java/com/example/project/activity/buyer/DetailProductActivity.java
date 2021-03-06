package com.example.project.activity.buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.model.Order;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    Product selectedProduct;
    User loginedUser;

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

        loginedUser = MainActivity.user;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //init
        Intent intent = getIntent();
        selectedProduct = (Product) intent.getSerializableExtra("selectedProduct");

        //
        final int[] productQuantityAvailable = new int[1];
        databaseReference.child("Product").orderByChild("id").equalTo(selectedProduct.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);

                selectedProduct.setQuantity(product.getQuantity());
                addImageToViewFlipper(selectedProduct.getImages());
                txtProductName.setText(selectedProduct.getName());
                //get username by userid
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

                //get availble product's quantity
                productQuantityAvailable[0] = selectedProduct.getQuantity();
                String statusProductQuantity;
                if (productQuantityAvailable[0] == 0) {
                    statusProductQuantity = "Not available";
                    //disable button plus, minus and textview currentQuantity, btnAddToCart and btnBuyNow
                    btnPlus.setVisibility(View.GONE);
                    btnMinus.setVisibility(View.GONE);
                    txtCurrentQuantity.setVisibility(View.GONE);
                    btnAddCart.setVisibility(View.GONE);
                    btnBuyNow.setVisibility(View.GONE);
                } else {
                    statusProductQuantity = productQuantityAvailable[0] + " products available";
                }
                txtProductQuantity.setText(statusProductQuantity);

                txtColor.setText(selectedProduct.getColor());
                txtBrand.setText(selectedProduct.getBrand());
                txtProductPrice.setText(String.format("%,d", selectedProduct.getPrice()));
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



        //Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Plus product's quantity
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(txtCurrentQuantity.getText().toString());
                if (currentQuantity < productQuantityAvailable[0]) {
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
                addToCart(v);
            }
        });

        //Buy now
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(v);
                final TabLayout tabLayout = MainActivity.tabLayout;
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }
        });
    }

    public void addImageToViewFlipper(ArrayList<String> imageUrlList) {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String imageUrl : imageUrlList) {
            imageList.add(new SlideModel(imageUrl, ScaleTypes.FIT));
        }
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE);
    }

    public String getValueFromTextView(TextView textView) {
        return textView.getText().toString();
    }

    public void addToCart(View v) {
        insertOrderToDatabase(v, "confirming");
    }

    public void insertOrderToDatabase(final View v, String status) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final Order order = new Order();
        String id = databaseReference.push().getKey();
        order.setId(id);
        order.setUserId(loginedUser.getId());
        order.setProductId(selectedProduct.getId());
        order.setOrderedQuantity(Integer.parseInt(txtCurrentQuantity.getText().toString()));
        order.setOrderedPrice(selectedProduct.getPrice());
        order.setOrderedAddress("");
        order.setStatus(status);
        databaseReference.child("Order").child(id).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Product").orderByChild("id").equalTo(order.getProductId()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        databaseReference.child("Product")
                                .child(order.getProductId())
                                .child("quantity")
                                .setValue(product.getQuantity() - order.getOrderedQuantity())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(v.getContext(), "Make order successfully", Toast.LENGTH_LONG).show();
                                        finish();
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
        });


    }

}
