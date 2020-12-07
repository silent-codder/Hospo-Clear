package com.silentcodder.newhospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class HospitalRegister_1 extends AppCompatActivity {

    EditText mHospitalName,mCity,mDist,mState;
    Button mBtnNext;
    String UserId,MobileNumber;
    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
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

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HospitalName = mHospitalName.getText().toString();
                String City = mCity.getText().toString();
                String Dist = mDist.getText().toString();
                String State = mState.getText().toString();

                editTextData(HospitalName,City,Dist,State,MobileNumber,UserId);
            }
        });
    }

    private void editTextData(String hospitalName, String city, String dist, String state,String mobileNumber,String userId) {
        if (TextUtils.isEmpty(hospitalName)){
            mHospitalName.setError("Hospital Name");
        }else if (TextUtils.isEmpty(city)){
            mCity.setError("City Name");
        }else if (TextUtils.isEmpty(dist)){
            mDist.setError("District Name");
        }else if (TextUtils.isEmpty(state)){
            mState.setError("State Name");
        }else {
            progressDialog.setMessage("Loading..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            HashMap<String,Object> map = new HashMap<>();
            map.put("HospitalName",hospitalName);
            map.put("City",city);
            map.put("District",dist);
            map.put("State",state);
            map.put("MobileNumber",mobileNumber);
            map.put("UserId",userId);
            map.put("TimeStamp",System.currentTimeMillis());

            firebaseFirestore.collection("Hospitals").document(userId)
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(HospitalRegister_1.this, "First step complete", Toast.LENGTH_SHORT).show();
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
        mDist = findViewById(R.id.dist);
        mState = findViewById(R.id.state);
        mBtnNext= findViewById(R.id.btnNext);
    }
}
