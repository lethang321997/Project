package com.example.project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.activity.LoginActivity;
import com.example.project.activity.buyer.ViewProfileActivity;
import com.example.project.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private TextView textName;
    private TextView textCash;
    private Button btnBrief;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textName = view.findViewById(R.id.txtName);
        textCash = view.findViewById(R.id.txtCash);
        btnBrief = view.findViewById(R.id.btnBrief);
        User loginUser = LoginActivity.user;

        //Setting Profile
        btnBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewProfileActivity.class);
                startActivity(intent);
            }
        });
    }


}
