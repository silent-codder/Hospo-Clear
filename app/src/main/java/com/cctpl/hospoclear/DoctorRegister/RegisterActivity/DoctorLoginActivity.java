package com.cctpl.hospoclear.DoctorRegister.RegisterActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cctpl.hospoclear.DoctorRegister.DoctorMainActivity;
import com.cctpl.hospoclear.HospitalRegister.HospitalLogin;
import com.cctpl.hospoclear.HospitalRegister.HospitalLoginOtp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

public class DoctorLoginActivity extends AppCompatActivity {

    EditText mMobileNumber;
    CountryCodePicker mCpp;
    Button mBtnContinue;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        mCpp = findViewById(R.id.cpp);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnContinue = findViewById(R.id.btnContinue);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mCpp.registerCarrierNumberEditText(mMobileNumber);
        ProgressBar progressBar = findViewById(R.id.loader);
        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnContinue.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                firebaseFirestore.collection("Hospitals").whereEqualTo("MobileNumber",mCpp.getFullNumberWithPlus())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value.isEmpty()){
                                    mMobileNumber.setError("Mobile number not register");
                                    mBtnContinue.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }else {
                                    mBtnContinue.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(DoctorLoginActivity.this, DoctorLoginOtpActivity.class);
                                    intent.putExtra("MobileNumber",mCpp.getFullNumberWithPlus().replace(" ",""));
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

}