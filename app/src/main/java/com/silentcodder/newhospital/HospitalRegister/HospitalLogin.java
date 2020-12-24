package com.silentcodder.newhospital.HospitalRegister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;
import com.silentcodder.newhospital.R;

public class HospitalLogin extends AppCompatActivity {

    CountryCodePicker mCpp;
    EditText mMobileNumber;
    Button mBtnContinue;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospita_login);
        findIDs();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mCpp.registerCarrierNumberEditText(mMobileNumber);

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Hospitals").whereEqualTo("MobileNumber",mCpp.getFullNumberWithPlus())
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value.isEmpty()){
                                    Toast.makeText(HospitalLogin.this, "Mobile number not register", Toast.LENGTH_SHORT).show();
                                }else {
                                    Intent intent = new Intent(HospitalLogin.this,HospitalLoginOtp.class);
                                    intent.putExtra("MobileNumber",mCpp.getFullNumberWithPlus().replace(" ",""));
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void findIDs() {
        mCpp = findViewById(R.id.cpp);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnContinue = findViewById(R.id.btnContinue);
    }
}