package com.example.project.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private Button btn_manageSelling;
    private Button btnBuyingHistory;
    private Button btnCash;
    private ImageView imageProfile;
    private TextView textName;
    private TextView textCash;
    private Button btnBrief;
    private Button btnLogout;
    private Button btnChangePassword;
    private EditText editOldPass;
    private EditText editNewPass;
    private EditText editRePass;
    private Button btnCancel;
    private Button btnSave;
    User loginedUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        super.onViewCreated(view, savedInstanceState);
        btnCash = view.findViewById(R.id.btnInsertCash);
        btn_manageSelling = view.findViewById(R.id.btnSellingHistory);
        btnBuyingHistory = view.findViewById(R.id.btnBuyingHistory);
        imageProfile = view.findViewById(R.id.imageProfile);
        textName = view.findViewById(R.id.txtName);
        textCash = view.findViewById(R.id.txtCash);
        btnBrief = view.findViewById(R.id.btnBrief);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        //get logined user
        loginedUser = MainActivity.user;
        databaseReference.child("User").child(loginedUser.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loginedUser = snapshot.getValue(User.class);
                textName.setText(loginedUser.getName());
                textCash.setText(String.format("%,d", loginedUser.getMoney()) + " VND ");
                //Set image
                if (!loginedUser.getImageUrl().equals("null")) {
                    Glide.with(view).load(loginedUser.getImageUrl()).into(imageProfile);
                } else {
                    Glide.with(view).load(R.drawable.profile_image).into(imageProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Cash
        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCash(v);
            }
        });

        //Selling history
        btn_manageSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SellingManagerFragment.newInstance(loginedUser.getId());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_profile_fragment, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Buying history
        btnBuyingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = BuyingHistoryFragment.newInstance(loginedUser.getId());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.root_profile_fragment, fragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
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
                getActivity().finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    void changePassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_password);
        editNewPass = dialog.findViewById(R.id.editNewPass);
        editOldPass = dialog.findViewById(R.id.editOldPassword);
        editRePass = dialog.findViewById(R.id.editRePass);
        btnSave = dialog.findViewById(R.id.btnSave);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = editOldPass.getText().toString();
                final String newPass = editNewPass.getText().toString();
                String rePass = editRePass.getText().toString();
                if (oldPass.isEmpty()) {
                    Toast.makeText(getContext(), "Please input old password", Toast.LENGTH_LONG).show();
                } else if (newPass.isEmpty()) {
                    Toast.makeText(getContext(), "Please input new password", Toast.LENGTH_LONG).show();
                } else if (rePass.isEmpty()) {
                    Toast.makeText(getContext(), "Please re-input new password", Toast.LENGTH_LONG).show();
                } else if (!oldPass.equals(loginedUser.getPassword())) {
                    Toast.makeText(getContext(), "Please input exactly old password", Toast.LENGTH_LONG).show();
                }else if (newPass.length() <6) {
                    Toast.makeText(getContext(), "Please input password at least 6 characters", Toast.LENGTH_LONG).show();
                } else if (!newPass.equals(rePass)) {
                    Toast.makeText(getContext(), "Please input exactly re-password", Toast.LENGTH_LONG).show();
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(loginedUser.getEmail(), oldPass);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("User");
                                            data.child(loginedUser.getId()).child("password").setValue(newPass);
                                            Toast.makeText(getContext(), "Change password successful", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Change password failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Change password failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        dialog.show();
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
                if (!txtCash.getText().toString().equals("")) {
                    String cash = txtCash.getText().toString();
                    String userId = loginedUser.getId();
                    insertCashToDatabase(userId, cash);
                    Toast.makeText(v.getContext(), "Insert cash successfully.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(v.getContext(), "Wrong input type of number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Cancel
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void insertCashToDatabase(String userId, String cash) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        int updatedCash = loginedUser.getMoney() + Integer.parseInt(cash);
        databaseReference.child("User").child(userId).child("money").setValue(updatedCash);
    }
}
