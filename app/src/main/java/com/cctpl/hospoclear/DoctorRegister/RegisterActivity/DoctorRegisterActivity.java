package com.cctpl.hospoclear.DoctorRegister.RegisterActivity;

import androidx.annotation.NonNull;
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

import com.cctpl.hospoclear.DoctorRegister.DoctorMainActivity;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorRegisterActivity extends AppCompatActivity {

    EditText mDoctorName,mQualification,mExperience;
    Button mBtnSubmit;
    String mSpecialty,UserId,Email,Password,HospitalId;

    List<HospitalData> hospitalData;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String[] Speciality = {"Select your speciality","Women's Health","Skin & Hair","Child Specialist","General Physician","Dental Surgeon","Ear, Nose, Throat(ENT)",
            "Homoeopathy","Bone & Joints","Sex Specialist","Eye Specialist","Digestive Issues","Mental Wellness","Physiotherapy","Diabetes Management",
            "Brain & Nerves","Urinary Issues","Kidney Issues","Ayurveda","General Surgery","Lungs & Breathing","Heart Specialist","Cancer Specialist"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register2);

        mDoctorName = findViewById(R.id.doctorName);
        mQualification = findViewById(R.id.doctorQualification);
        mExperience = findViewById(R.id.doctorExperience);
        mBtnSubmit = findViewById(R.id.btnSubmit);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        HospitalId = getIntent().getStringExtra("HospitalId");
        Email = getIntent().getStringExtra("Email");
        Password = getIntent().getStringExtra("Password");

        hospitalData = new ArrayList<>();
        Spinner speciality = findViewById(R.id.doctorSpeciality);
        Toast.makeText(this, "Current Id : " + UserId, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DoctorRegisterActivity.this, "Select speciality", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("DoctorName","Dr. " + DoctorName);
                    map.put("Qualification",Qualification);
                    map.put("Experience",Experience + " yr");
                    map.put("Speciality",mSpecialty);
                    map.put("HospitalId",HospitalId);
                    map.put("isUser","3");
                    map.put("Email",Email);
                    map.put("Password",Password);
                    //doctor user id '3'
                    map.put("TimeStamp",System.currentTimeMillis());

                    firebaseFirestore.collection("AppUsers").document(UserId).set(map);

                    firebaseFirestore.collection("Doctors").document(UserId).set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(DoctorRegisterActivity.this, "Create doctor login", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DoctorRegisterActivity.this, DoctorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DoctorRegisterActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}