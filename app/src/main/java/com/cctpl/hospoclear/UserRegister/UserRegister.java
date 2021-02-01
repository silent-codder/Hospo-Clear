package com.cctpl.hospoclear.UserRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.cctpl.hospoclear.HospitalRegister.HospitalOtp;
import com.cctpl.hospoclear.HospitalRegister.HospitalRegister;
import com.cctpl.hospoclear.R;

public class UserRegister extends AppCompatActivity {

    CountryCodePicker mCpp;
    EditText mMobileNumber;
    Button mBtnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        findIDs();
        mCpp.registerCarrierNumberEditText(mMobileNumber);

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegister.this, UserOtp.class);
                intent.putExtra("MobileNumber",mCpp.getFullNumberWithPlus().replace(" ",""));
                startActivity(intent);
                finish();
            }
        });
    }

    private void findIDs() {
        mCpp = findViewById(R.id.cpp);
        mMobileNumber = findViewById(R.id.mobileNumber);
        mBtnContinue = findViewById(R.id.btnContinue);
    }
}