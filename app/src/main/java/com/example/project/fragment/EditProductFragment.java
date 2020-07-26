package com.example.project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.activity.MainActivity;

import com.example.project.adapter.EditImageAdapter;
import com.example.project.adapter.ListImageAdapter;
import com.example.project.common.Constants;
import com.example.project.model.Product;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.params.HttpConnectionParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProductFragment extends Fragment {

    Product product;
    EditText editName;
    EditText editBrand;
    EditText editQuantity;
    EditText editPrice;
    Spinner spinnerColor;
    Spinner spinnerType;
    Button btnSelectImage;
    Button btnSave;
    ImageView btnBack;
    TextView takePhoto;
    TextView selectFromLibrary;
    ProgressBar progressBar;
    RecyclerView recyclerViewImage;
    static ArrayList<String> listImageUrl = new ArrayList<>();
    static ArrayList<String> listImageDelete = new ArrayList<>();
    ArrayList<Bitmap> listImageBitmap = new ArrayList<>();
    EditImageAdapter adapter;
    User user;
    String id;
    public static int firstSize;
    Product newProduct;

    public EditProductFragment() {
        // Required empty public constructor
    }


    public static EditProductFragment newInstance(Product product) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PRODUCT_DETAILS, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(Constants.PRODUCT_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidget();
        initData();
        initView();
        initSpinner();
        initAction();
    }

    void initData() {
        MainActivity activity = (MainActivity) getActivity();
        user = activity.getUser();
        id = product.getId();
        listImageUrl = product.getImages();
        new ConvertUrlToUri().execute(listImageUrl);
    }

    void initWidget() {
        spinnerColor = getView().findViewById(R.id.spinnerColor);
        spinnerType = getView().findViewById(R.id.spinnerType);
        btnSelectImage = getView().findViewById(R.id.btnSelectImage);
        recyclerViewImage = getView().findViewById(R.id.recyclerViewImage);
        editName = getView().findViewById(R.id.editProductName);
        editBrand = getView().findViewById(R.id.editBrand);
        editQuantity = getView().findViewById(R.id.editQuantity);
        editPrice = getView().findViewById(R.id.editProductPrice);
        btnBack = getView().findViewById(R.id.btnBack);
        btnSave = getView().findViewById(R.id.btnSave);
        progressBar = getView().findViewById(R.id.progressBar);
    }

    void initView() {
        editName.setText(product.getName());
        editBrand.setText(product.getBrand());
        editQuantity.setText(String.valueOf(product.getQuantity()));
        editPrice.setText(String.valueOf(product.getPrice()));
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
        spinnerColor.setSelection(adapter.getPosition(product.getColor()));
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
        spinnerType.setSelection(adapterType.getPosition(product.getType()));
    }

    private void initAction() {
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectImage();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
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
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                        if (listImageBitmap.size() < 6) {
                            listImageBitmap.add(imageBitmap);
                        } else {
                            Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Uri imageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    if (listImageBitmap.size() < 6) {
                        listImageBitmap.add(imageBitmap);
                    } else {
                        Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
        if (requestCode == Constants.TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (listImageBitmap.size() < 6) {
                listImageBitmap.add(imageBitmap);
            } else {
                Toast.makeText(getContext(), "Please select maximum 6 images", Toast.LENGTH_LONG).show();
            }
        }
        adapter.notifyDataSetChanged();
    }


    void updateImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("ImageFolder");
        for (int i = 0; i < listImageDelete.size(); i++) {
            final DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
            data.child(id).child("listImage").orderByChild("imageUrl").equalTo(listImageDelete.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        data.child(id).child("listImage").child(key).removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (firstSize < listImageBitmap.size()) {
            progressBar.setVisibility(View.VISIBLE);
            for (int i = firstSize; i < listImageBitmap.size(); i++) {
                Bitmap bitmap = listImageBitmap.get(i);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] dataImage = outputStream.toByteArray();
                final StorageReference imageName = storageRef.child("image" + bitmap.getGenerationId());
                final int count = i;
                imageName.putBytes(dataImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
                                String imageUrl = String.valueOf(uri);
                                if (EditImageAdapter.deletedMainImage && listImageUrl.size() == 0) {
                                    data.child(id).child("mainImage").setValue(imageUrl);
                                }
                                listImageUrl.add(imageUrl);
                                if (count == listImageBitmap.size() - 1) {
                                    data.child(id).child("listImage").push().child("imageUrl").setValue(imageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            newProduct.setImages(listImageUrl);
                                            DetailMyProductFragment.product = newProduct;
                                            getActivity().getSupportFragmentManager().popBackStack();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                                } else {
                                    data.child(id).child("listImage").push().child("imageUrl").setValue(imageUrl);
                                }
                            }
                        });
                    }
                });
            }
        } else {
            newProduct.setImages(listImageUrl);
            DetailMyProductFragment.product = newProduct;
            getActivity().getSupportFragmentManager().popBackStack();
        }

    }

    void saveProduct() {
        String name = editName.getText().toString();
        String brand = editBrand.getText().toString();
        String quantity = editQuantity.getText().toString();
        String color = spinnerColor.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();
        String price = editPrice.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Please input name", Toast.LENGTH_LONG).show();
        } else if (brand.isEmpty()) {
            Toast.makeText(getContext(), "Please input brand", Toast.LENGTH_LONG).show();
        } else if (quantity.isEmpty()) {
            Toast.makeText(getContext(), "Please input quantity", Toast.LENGTH_LONG).show();
        } else if (listImageBitmap.size() == 0) {
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_LONG).show();
        } else if (price.isEmpty()) {
            Toast.makeText(getContext(), "Please input price", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Product");
            if (EditImageAdapter.deletedMainImage && listImageUrl.size() > 0) {
                data.child(id).child("mainImage").setValue(listImageUrl.get(0));
            }
            String idUser = user.getId();
            Map<String, Object> newValue = new HashMap<>();
            newValue.put(Constants.PRODUCT_NAME, name);
            newValue.put(Constants.PRODUCT_BRAND, brand);
            newValue.put(Constants.PRODUCT_QUANTITY, Integer.parseInt(quantity));
            newValue.put(Constants.PRODUCT_COLOR, color);
            newValue.put(Constants.PRODUCT_TYPE, type);
            newValue.put(Constants.PRODUCT_PRICE, Integer.parseInt(price));
            data.child(id).updateChildren(newValue);
            newProduct = new Product(id, idUser, name, brand, Integer.parseInt(quantity), color, type, Integer.parseInt(price));
            newProduct.setMainImage(product.getMainImage());
            updateImage();
        }
    }

    public static void addDeletedImage(int position) {
        listImageDelete.add(listImageUrl.get(position));
        listImageUrl.remove(position);
    }

    class ConvertUrlToUri extends AsyncTask<ArrayList<String>, Void, ArrayList<Bitmap>> {
        ArrayList<Bitmap> listBitmap = new ArrayList<>();

        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> list = arrayLists[0];
            for (String url : list) {
                Bitmap bitmap;
                InputStream inputStream = null;
                try {
                    inputStream = new URL(url).openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(inputStream);
                listBitmap.add(bitmap);
            }
            return listBitmap;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            listImageBitmap = bitmaps;
            firstSize = listBitmap.size();
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            recyclerViewImage.setLayoutManager(layoutManager);
            adapter = new EditImageAdapter(listImageBitmap);
            recyclerViewImage.setAdapter(adapter);
        }
    }
}