package com.silentcodder.newhospital.HospitalRegister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorRegister extends AppCompatActivity {

    EditText mDoctorName,mQualification,mExperience;
    Button mBtnSubmit;
    String mSpecialty,UserId;

    List<HospitalData> hospitalData;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String[] Speciality = {"Select your speciality","Women's Health","Skin & Hair","Child Specialist","General Physician","Dental Surgeon","Ear, Nose, Throat(ENT)",
            "Homoeopathy","Bone & Joints","Sex Specialist","Eye Specialist","Digestive Issues","Mental Wellness","Physiotherapy","Diabetes Management",
            "Brain & Nerves","Urinary Issues","Kidney Issues","Ayurveda","General Surgery","Lungs & Breathing","Heart Specialist","Cancer Specialist"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        mDoctorName = findViewById(R.id.doctorName);
        mQualification = findViewById(R.id.doctorQualification);
        mExperience = findViewById(R.id.doctorExperience);
        mBtnSubmit = findViewById(R.id.btnSubmit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        hospitalData = new ArrayList<>();
        Spinner speciality = findViewById(R.id.doctorSpeciality);

        speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpecialty = Speciality[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter Special = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Speciality);
        Special.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciality.setAdapter(Special);
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DoctorName = mDoctorName.getText().toString();
                String Qualification = mQualification.getText().toString();
                String Experience = mExperience.getText().toString();

                if (TextUtils.isEmpty(DoctorName)){
                    mDoctorName.setError("Doctor Name");
                }else if (TextUtils.isEmpty(Qualification)){
                    mQualification.setError("Qualification");
                }else if (TextUtils.isEmpty(Experience)){
                    mExperience.setError("Experience");
                }else if (mSpecialty.equals("Select your speciality")){
                    Toast.makeText(DoctorRegister.this, "Select speciality", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("DoctorName","Dr. " + DoctorName);
                    map.put("Qualification",Qualification);
                    map.put("Experience",Experience + " yr");
                    map.put("Speciality",mSpecialty);
                    map.put("HospitalId",UserId);
                    map.put("isUser","3");
                    //doctor user id '3'
                    map.put("TimeStamp",System.currentTimeMillis());

                    firebaseFirestore.collection("Doctors").document(UserId).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DoctorRegister.this, "Upload", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DoctorRegister.this,HospitalMainActivity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DoctorRegister.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}