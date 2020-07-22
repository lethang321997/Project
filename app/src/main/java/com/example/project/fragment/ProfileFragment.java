package com.example.project.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.activity.buyer.ViewProfileActivity;
import com.example.project.model.Order;
import com.example.project.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private Button btnCash;
    private Button btn_manageSelling;
    private ImageView imageProfile;
    private TextView textName;
    private TextView textCash;
    private Button btnBrief;
    private Button btnLogout;
    User loginedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCash = view.findViewById(R.id.btnInsertCash);
        btn_manageSelling = view.findViewById(R.id.btnManageSelling);
        imageProfile = view.findViewById(R.id.imageProfile);
        textName = view.findViewById(R.id.txtName);
        textCash = view.findViewById(R.id.txtCash);
        btnBrief = view.findViewById(R.id.btnBrief);
        btnLogout = view.findViewById(R.id.btnLogout);

        //get logined user
        MainActivity mainActivity = (MainActivity) getActivity();
        loginedUser = mainActivity.getUser();

        Glide.with(this).load(loginedUser.getImageUrl()).into(imageProfile);
        textName.setText(loginedUser.getName());
        textCash.setText(String.format("%,d",loginedUser.getMoney())+ " VND ");

        //Cash
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCash(v);
            }
        });

        //Manage selling
        btn_manageSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Setting Profile
        btnBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
                startActivity(intent);
            }
        });

        //Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.logOut();
            }
        });
    }

    public void insertCash(View v) {
        final EditText txtCash;
        Button btnConfirm;
        Button btnCancel;

        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.dialog_insert_cash);
        dialog.show();

        txtCash = dialog.findViewById(R.id.txtCash_insertCash);
        btnConfirm = dialog.findViewById(R.id.btnConfirm_insertCash);
        btnCancel = dialog.findViewById(R.id.btnCancel_insertCash);

        //Confirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cash = txtCash.getText().toString();
                String userId = loginedUser.getId();
                insertCashToDatabase(userId, cash);
                Toast.makeText(v.getContext(), "Insert cash successfully", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        //Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void insertCashToDatabase(String userId ,String cash) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        int updatedCash = loginedUser.getMoney() + Integer.parseInt(cash);
        databaseReference.child("User").child(userId).child("money").setValue(updatedCash);
    }
}
