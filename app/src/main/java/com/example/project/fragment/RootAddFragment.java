package com.example.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RootAddFragment extends Fragment {


    public RootAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.root_add_fragment, new StallFragment());
        transaction.commitAllowingStateLoss();
        return inflater.inflate(R.layout.fragment_root_add, container, false);
    }
}
