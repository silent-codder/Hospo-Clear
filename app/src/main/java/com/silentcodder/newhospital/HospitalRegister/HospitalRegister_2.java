package com.silentcodder.newhospital.HospitalRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;

import java.util.HashMap;

public class HospitalRegister_2 extends AppCompatActivity {

    EditText mHospitalContactNumber;
    CheckBox mCheckBox;
    Button mBtnNext;
    String UserId,checkBox;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_register_2);
        mHospitalContactNumber = findViewById(R.id.hospitalContactNumber);
        mCheckBox = findViewById(R.id.checkbox);
        mBtnNext = findViewById(R.id.btnNext);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkBox = "Yes";
                }else {
                    checkBox = "No";
                }
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNumber = mHospitalContactNumber.getText().toString();
                if (TextUtils.isEmpty(contactNumber)){
                    mHospitalContactNumber.setError("Contact Number");
                }else {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("ContactNumber",contactNumber);
                    map.put("Ambulance",checkBox);
                    firebaseFirestore.collection("Hospitals").document(UserId).update(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(HospitalRegister_2.this,DoctorRegister.class));
                                        finish();
                                        Toast.makeText(HospitalRegister_2.this, "Upload", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HospitalRegister_2.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}