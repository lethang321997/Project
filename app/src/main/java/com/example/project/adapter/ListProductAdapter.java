package com.example.project.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class ListProductAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Product> productList;
    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public ListProductAdapter(Context context, int layout, List<Product> productList) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageProduct = null;
        TextView txtProductName;
        TextView txtUserName;
        TextView txtProductPrice;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout_product_list, null);
            imageProduct = convertView.findViewById(R.id.imageProduct);
            txtProductName = convertView.findViewById(R.id.txtProductName);
            txtUserName = convertView.findViewById(R.id.txtUserName);
            txtProductPrice = convertView.findViewById(R.id.txtProductPrice);
            convertView.setTag(R.id.imageProduct, imageProduct);
            convertView.setTag(R.id.txtProductName, txtProductName);
            convertView.setTag(R.id.txtUserName, txtUserName);
            convertView.setTag(R.id.txtProductPrice, txtProductPrice);
        } else {
            imageProduct = (ImageView) convertView.getTag(R.id.imageProduct);
            txtProductName = (TextView) convertView.getTag(R.id.txtProductName);
            txtUserName = (TextView) convertView.getTag(R.id.txtUserName);
            txtProductPrice = (TextView) convertView.getTag(R.id.txtProductPrice);
        }

        Product indexProduct = (Product) getItem(position);

        final String[] imageUrl = {""};
        String image = indexProduct.getImages().get(0);
        databaseReference.child("Product").child("listImage").equalTo(image).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                imageUrl[0] = snapshot.getValue(String.class);
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

        Glide.with(convertView).load(imageUrl[0]).into(imageProduct);
        txtProductName.setText(indexProduct.getName());
        final User[] user = {new User()};

        databaseReference.child("User").equalTo(indexProduct.getIdUser()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user[0] = snapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                user[0] = snapshot.getValue(User.class);
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
        txtUserName.setText(user[0].getName());
        txtProductPrice.setText(String.valueOf(indexProduct.getPrice()));
        return convertView;
    }


}
