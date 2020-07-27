package com.example.project.activity.buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.common.Constants;
import com.example.project.model.Commune;
import com.example.project.model.District;
import com.example.project.model.Province;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewProfileActivity extends AppCompatActivity {

    private ImageView imageProfile;
    private ImageButton btnBack;
    private ImageButton btnSave;
    private Button btnChangeImage;
    private TextView txtEmail;
    private EditText txtViewUserName;
    private RadioGroup rdGender;
    private TextView txtDob;
    private EditText txtPhone;
    private EditText txtLocation;
    TextView takePhoto;
    TextView selectFromLibrary;
    Uri imageUri = null;
    User loginedUser;
    Button btnGetDate;
    DatePicker datePicker;

    RequestQueue mQueue;
    ArrayList<Province> listProvince = new ArrayList<>();
    ArrayList<District> listDistrict = new ArrayList<>();
    ArrayList<Commune> listCommune = new ArrayList<>();

    //Location popup
    Spinner spinnerEditProvince;
    Spinner spinnerEditDistrict;
    Spinner spinnerEditCommune;
    EditText editAddress;
    Button btnConfirm;
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //initialize
        imageProfile = findViewById(R.id.imgProfile);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        txtEmail = findViewById(R.id.txtEmail);
        txtViewUserName = findViewById(R.id.txtViewUserName);
        rdGender = findViewById(R.id.rdGender);
        txtDob = findViewById(R.id.txtDOB);
        txtPhone = findViewById(R.id.txtPhone);
        txtLocation = findViewById(R.id.txtLocation);

        //get logined User
        loginedUser = MainActivity.user;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").equalTo(loginedUser.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loginedUser = snapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loginedUser = snapshot.getValue(User.class);
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

        //set value of logined to textview and editview
        if (!loginedUser.getImageUrl().equals("null")) {
            Glide.with(this).load(loginedUser.getImageUrl()).into(imageProfile);
        } else {
            Glide.with(this).load(R.drawable.profile_image).into(imageProfile);
        }

        txtEmail.setText(loginedUser.getEmail());
        txtViewUserName.setText(loginedUser.getName());
        for (int i = 0; i < rdGender.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) rdGender.getChildAt(i);
            if (radioButton.getText().equals(loginedUser.getGender())) {
                radioButton.setChecked(true);
                break;
            }
        }
        txtDob.setText(loginedUser.getDob());
        txtPhone.setText(loginedUser.getPhone());
        txtLocation.setText(loginedUser.getAddress());

        //catch event
        //Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        //Save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        //Change image
        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectImage(v);
            }
        });

        //Change dob
        txtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDOB();
            }
        });

    }

    private void dialogSelectImage(View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.dialog_seclect_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        takePhoto = dialog.findViewById(R.id.takePhoto);
        selectFromLibrary = dialog.findViewById(R.id.selectFromLibrary);
        selectFromLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(intent, Constants.SELECT_LIBRARY_CODE);
                dialog.dismiss();
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.TAKE_PHOTO_CODE);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_LIBRARY_CODE && resultCode == Activity.RESULT_OK) {
            assert data != null;
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUri = clipData.getItemAt(i).getUri();
                }
            } else {
                imageUri = data.getData();
            }
        }
        if (requestCode == Constants.TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, null, null);
            imageUri = Uri.parse(path);
        }
        imageProfile.setImageURI(imageUri);
    }

    public void updateUser() {
        if (imageUri!=null) {
            storeImage();
        }
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.child("User").child(loginedUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("name").setValue(txtViewUserName.getText().toString());
                snapshot.getRef().child("gender").setValue(((RadioButton) findViewById(rdGender.getCheckedRadioButtonId())).getText().toString());
                snapshot.getRef().child("dob").setValue(txtDob.getText().toString());
                snapshot.getRef().child("phone").setValue(txtPhone.getText().toString());
                snapshot.getRef().child("address").setValue(txtLocation.getText().toString());
                closeActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void storeImage() {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        final StorageReference imageName = storageReference.child("image" + Calendar.getInstance().getTimeInMillis());
        imageName.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("User");
                        String imageUrl = String.valueOf(uri);
                        data.child(loginedUser.getId()).child("imageUrl").setValue(imageUrl);
                    }
                });
            }
        });
    }

    void getDOB() {
        final Dialog dialog = new Dialog(ViewProfileActivity.this);
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        btnGetDate = dialog.findViewById(R.id.btnGetDate);
        datePicker = dialog.findViewById(R.id.datePicker);
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                txtDob.setText(dob);
                dialog.dismiss();
            }
        });
    }

    public void closeActivity() {
        finish();
    }
}
