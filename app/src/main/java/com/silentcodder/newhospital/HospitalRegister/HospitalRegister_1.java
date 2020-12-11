package com.silentcodder.newhospital.HospitalRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;

import java.util.HashMap;

public class HospitalRegister_1 extends AppCompatActivity {

    EditText mHospitalName,mCity;
    Button mBtnNext;
    String UserId,MobileNumber,State;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String[] district = { "Select District","Ahmednagar","Akola","Amravati","Aurangabad","Beed","Bhandara","Buldhana","Chandrapur","Dhule","Gadchiroli","Gondia",
            "Hingoli","Jalgaon","Jalna","Kolhapur","Latur","Mumbai City"," Mumbai Suburban","Nagpur","Nanded","Nandurbar","Nashik","Osmanabad","Palghar","Parbhani",
            "Pune","Raigad","Ratnagiri","Sangli","Satara","Sindhudurg","Solapur","Thane","Wardha","Washim","Yavatmal"};
    String[] state = { "Select State","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand",
            "Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha","Punjab","Rajasthan","Sikkim","Tamil Nadu",
            "Telangana","Tripura","Uttar Pradesh","Uttarakhand","West Bengal"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_register_1);
        findIds();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        MobileNumber = getIntent().getStringExtra("Mobile");
        UserId = firebaseAuth.getCurrentUser().getUid();

        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State = state[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter states = new ArrayAdapter(this,android.R.layout.simple_spinner_item,state);
        states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(states);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HospitalName = mHospitalName.getText().toString();
                String City = mCity.getText().toString();

                editTextData(HospitalName,City,State,MobileNumber,UserId);
            }
        });
    }

    private void editTextData(String hospitalName, String city,String state,String mobileNumber,String userId) {
        if (TextUtils.isEmpty(hospitalName)){
            mHospitalName.setError("Hospital Name");
        }else if (TextUtils.isEmpty(city)){
            mCity.setError("City Name");
        }else if (state.equals("Select State")){
            Toast.makeText(this, "Select state", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Loading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            HashMap<String,Object> map = new HashMap<>();
            map.put("HospitalName",hospitalName);
            map.put("City",city);
            map.put("State",state);
            map.put("MobileNumber",mobileNumber);
            map.put("UserId",userId);
            map.put("isUser","1");
            map.put("TimeStamp",System.currentTimeMillis());

            firebaseFirestore.collection("Hospitals").document(userId)
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(HospitalRegister_1.this, "First step complete", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(HospitalRegister_1.this,HospitalRegister_2.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(HospitalRegister_1.this, "DataBase error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void findIds() {
        mHospitalName = findViewById(R.id.hospitalName);
        mCity = findViewById(R.id.city);
        mBtnNext= findViewById(R.id.btnNext);
    }
}
