package com.example.project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.MainActivity;
import com.example.project.adapter.ListImageAdapter;
import com.example.project.common.Constants;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {

    EditText editName;
    EditText editBrand;
    EditText editQuantity;
    EditText editPrice;
    Spinner spinnerColor;
    Spinner spinnerType;
    Button btnSelectImage;
    Button btnAddProduct;
    TextView takePhoto;
    TextView selectFromLibrary;
    RecyclerView recyclerViewImage;
    ArrayList<Uri> listImage = new ArrayList<>();
    ArrayList<String> listImageUrl = new ArrayList<>();
    ListImageAdapter adapter;
    User user;
    String id;
    boolean firstImage = true;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
        initSpinner();
        initAction();
        initAdapter();
    }

    void initWidget() {
        spinnerColor = getView().findViewById(R.id.spinnerColor);
        spinnerType = getView().findViewById(R.id.spinnerType);
        btnSelectImage = getView().findViewById(R.id.btnSelectImage);
        recyclerViewImage = getView().findViewById(R.id.recyclerViewImage);
        btnAddProduct = getView().findViewById(R.id.btnAddProduct);
        editName = getView().findViewById(R.id.editProductName);
        editBrand = getView().findViewById(R.id.editBrand);
        editQuantity = getView().findViewById(R.id.editQuantity);
        editPrice = getView().findViewById(R.id.editProductPrice);
    }

    void initData() {
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
    }

    void initSpinner() {
        String[] listColor = getResources().getStringArray(R.array.colors);
        Arrays.sort(listColor);
        ArrayList<String> listColors = new ArrayList<>(Arrays.asList(listColor));
        listColors.add(0, "Select Color");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listColors) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerColor.setAdapter(adapter);

        String[] listType = getResources().getStringArray(R.array.product_type);
        ArrayList<String> listTypes = new ArrayList<>(Arrays.asList(listType));
        listTypes.add(0, "Select Type");
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listTypes) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapterType.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
    }

    private void initAction() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectImage();
            }
        });
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    void initAdapter() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImage.setLayoutManager(layoutManager);
        adapter = new ListImageAdapter(listImage);
        recyclerViewImage.setAdapter(adapter);
    }

    private void dialogSelectImage() {
        final Dialog dialog = new Dialog(getContext());
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
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    if (listImage.size() < 6) {
                        listImage.add(imageUri);
                    } else {
                        Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                if (listImage.size() < 6) {
                    listImage.add(imageUri);
                } else {
                    Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
                }
            }

        }
        if (requestCode == Constants.TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), imageBitmap, null, null);
            Uri imageUri = Uri.parse(path);
            if (listImage.size() < 6) {
                listImage.add(imageUri);
            } else {
                Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
            }
        }
        adapter.notifyDataSetChanged();
    }

    void addProduct() {
        storeImage();
        String name = editName.getText().toString();
        String brand = editBrand.getText().toString();
        int quantity = Integer.parseInt(editQuantity.getText().toString());
        String color = spinnerColor.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();
        int price = Integer.parseInt(editPrice.getText().toString());
        String idUser = user.getId();
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
        id = data.push().getKey();
        Product product = new Product(id, idUser, name, brand, quantity, color, type, price);
        data.child(id).setValue(product);

    }

    void storeImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        for (int i = 0; i < listImage.size(); i++) {
            final Uri imageUri = listImage.get(i);
            final StorageReference imageName = storageRef.child("image" + imageUri.getLastPathSegment());
            imageName.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
                            String imageUrl = String.valueOf(uri);
                            if (firstImage) {
                                data.child(id).child("mainImage").setValue(imageUrl);
                                firstImage = false;
                            }
                            data.child(id).child("listImage").push().child("imageUrl").setValue(imageUrl);
                            listImageUrl.add(imageUrl);
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.root_add_fragment, new StallFragment());
                            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            transaction.commit();
                        }
                    });
                }
            });
        }
    }
}
