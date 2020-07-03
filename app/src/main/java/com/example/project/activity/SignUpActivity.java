package com.example.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.example.project.model.Commune;
import com.example.project.model.District;
import com.example.project.model.Province;
import com.example.project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    EditText registerName;
    EditText registerEmail;
    EditText registerPhone;
    EditText registerAddress;
    EditText registerPass;
    EditText registerRePass;
    CheckBox checkBoxTerm;
    Button btnSignUpAccount;
    Spinner spinnerProvince;
    Spinner spinnerDistrict;
    Spinner spinnerCommune;
    RequestQueue mQueue;
    ArrayList<Province> listProvince = new ArrayList<>();
    ArrayList<District> listDistrict = new ArrayList<>();
    ArrayList<Commune> listCommune = new ArrayList<>();
    public static final Pattern EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initWidget();
        initSpinner();
        initAction();
    }

    void initWidget() {
        checkBoxTerm = findViewById(R.id.checkBoxTerm);
        btnSignUpAccount = findViewById(R.id.btnSignUpAccount);
        spinnerProvince = findViewById(R.id.spinnerProvince);
        spinnerDistrict = findViewById(R.id.spinnerDistrict);
        spinnerCommune = findViewById(R.id.spinnerCommune);
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPhone = findViewById(R.id.registerPhone);
        registerAddress = findViewById(R.id.registerAddress);
        registerPass = findViewById(R.id.registerPass);
        registerRePass = findViewById(R.id.registerRePass);
    }


    void initAction() {
        checkBoxTerm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSignUpAccount.setEnabled(true);
                } else {
                    btnSignUpAccount.setEnabled(false);
                }
            }
        });
        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setDataSpinnerDistrict(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setDataSpinnerCommune(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSignUpAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });
    }

    void initSpinner() {
        initSpinnerProvince();
        initSpinnerDistrict();
        initSpinnerCommune();
    }

    void initSpinnerDistrict() {
        listDistrict.add(new District(0, "Select District"));
        ArrayAdapter<District> adapter = new ArrayAdapter<District>(getApplicationContext(), android.R.layout.simple_spinner_item, listDistrict) {
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
        spinnerDistrict.setAdapter(adapter);
    }

    void initSpinnerCommune() {
        listCommune.add(new Commune(0, "Select Commune"));
        ArrayAdapter<Commune> adapter = new ArrayAdapter<Commune>(getApplicationContext(), android.R.layout.simple_spinner_item, listCommune) {
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
        spinnerCommune.setAdapter(adapter);
    }

    void initSpinnerProvince() {
        mQueue = Volley.newRequestQueue(this);
        String provinceUrl = "https://thongtindoanhnghiep.co/api/city";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, provinceUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("LtsItem");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("ID");
                        String name = object.getString("Title");
                        Province province = new Province(id, name);
                        listProvince.add(province);
                    }
                    Collections.sort(listProvince, new Comparator<Province>() {
                        @Override
                        public int compare(Province o1, Province o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                    listProvince.add(0, new Province(0, "Select Province"));
                    ArrayAdapter<Province> adapter = new ArrayAdapter<Province>(getApplicationContext(), android.R.layout.simple_spinner_item, listProvince) {
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
                    spinnerProvince.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    void setDataSpinnerDistrict(int position) {
        listDistrict.clear();
        Province province = (Province) spinnerProvince.getItemAtPosition(position);
        int provinceId = province.getId();
        String districtUrl = "https://thongtindoanhnghiep.co/api/city/" + provinceId + "/district";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, districtUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                int id = object.getInt("ID");
                                String name = object.getString("Title");
                                District district = new District(id, name);
                                listDistrict.add(district);
                            }
                            Collections.sort(listDistrict, new Comparator<District>() {
                                @Override
                                public int compare(District o1, District o2) {
                                    return o1.getName().compareToIgnoreCase(o2.getName());
                                }
                            });
                            listDistrict.add(0, new District(0, "Select District"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    void setDataSpinnerCommune(int position) {
        listCommune.clear();
        District district = (District) spinnerDistrict.getItemAtPosition(position);
        int districtId = district.getId();
        String communeUrl = "https://thongtindoanhnghiep.co/api/district/" + districtId + "/ward";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, communeUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                int id = object.getInt("ID");
                                String name = object.getString("Title");
                                Commune commune = new Commune(id, name);
                                listCommune.add(commune);
                            }
                            Collections.sort(listCommune, new Comparator<Commune>() {
                                @Override
                                public int compare(Commune o1, Commune o2) {
                                    return o1.getName().compareToIgnoreCase(o2.getName());
                                }
                            });
                            listCommune.add(0, new Commune(0, "Select Commune"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    void registerAccount() {
        final String name = registerName.getText().toString();
        final String email = registerEmail.getText().toString();
        final String phone = registerPhone.getText().toString();
        final String address = registerAddress.getText().toString() + " - " + spinnerCommune.getSelectedItem().toString()
                + " - " + spinnerDistrict.getSelectedItem().toString() + " - " + spinnerProvince.getSelectedItem().toString();
        final String pass = registerPass.getText().toString();
        String rePass = registerRePass.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Please input email", Toast.LENGTH_LONG).show();
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Please input name", Toast.LENGTH_LONG).show();
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Please input phone number", Toast.LENGTH_LONG).show();
        } else if (spinnerProvince.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select province", Toast.LENGTH_LONG).show();
        } else if (spinnerDistrict.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select district", Toast.LENGTH_LONG).show();
        } else if (spinnerCommune.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select commune", Toast.LENGTH_LONG).show();
        } else if (registerAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please input street name", Toast.LENGTH_LONG).show();
        } else if (pass.isEmpty()) {
            Toast.makeText(this, "Please input password", Toast.LENGTH_LONG).show();
        } else if (rePass.isEmpty()) {
            Toast.makeText(this, "Please re-input password", Toast.LENGTH_LONG).show();
        } else if (checkFormatEmail(email)) {
            Toast.makeText(this, "Please input exactly email", Toast.LENGTH_LONG).show();
        } else if (phone.length() != 10 && phone.length() != 11) {
            Toast.makeText(this, "Please input phone number", Toast.LENGTH_LONG).show();
        } else if (!pass.equals(rePass)) {
            Toast.makeText(this, "Please input exactly re-password", Toast.LENGTH_LONG).show();
        } else {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseUser getUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert getUser != null;
                        String id = getUser.getUid();
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("User");
                        User user = new User(id, email, name, phone, address, pass);
                        data.child(id).setValue(user);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    boolean checkFormatEmail(String email) {
        Matcher matcher = SignUpActivity.EMAIL_ADDRESS_REGEX.matcher(email);
        return !matcher.find();
    }
}
