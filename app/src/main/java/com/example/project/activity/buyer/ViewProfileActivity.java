package com.example.project.activity.buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.project.R;

public class ViewProfileActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton btnSave;
    private Button btnChangeImage;
    private TextView txtEmail;
    private EditText txtName;
    private RadioGroup rdGender;
    private EditText txtDob;
    private EditText txtPhone;
    private TextView txtLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //initialize
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtName);
        rdGender = findViewById(R.id.rdGender);
        txtDob = findViewById(R.id.txtDOB);
        txtPhone = findViewById(R.id.txtPhone);
        txtLocation = findViewById(R.id.txtLocation);

        //catch event
        //Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Change image
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Change location
        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationDialog(v);
            }
        });

    }

    public void openLocationDialog(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.dialog_layout_location);
        dialog.show();


    }
}
