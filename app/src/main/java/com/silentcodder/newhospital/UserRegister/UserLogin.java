package com.silentcodder.newhospital.UserRegister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;
import com.silentcodder.newhospital.R;

public class UserLogin extends AppCompatActivity {

    CountryCodePicker mCpp;
    EditText mMobileNumber;
    Button mBtnContinue;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        firebaseFirestore = FirebaseFirestore.getInstance();

        findIDs();
        mCpp.registerCarrierNumberEditText(mMobileNumber);
        ProgressBar progressBar = findViewById(R.id.loader);
       mBtnContinue.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mBtnContinue.setVisibility(View.INVISIBLE);
               progressBar.setVisibility(View.VISIBLE);
               firebaseFirestore.collection("Users").whereEqualTo("MobileNumber",mCpp.getFullNumberWithPlus()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                   @Override
                   public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                       if (value.isEmpty()){
                           mMobileNumber.setError("mobile number not register");
                           mBtnContinue.setVisibility(View.VISIBLE);
                           progressBar.setVisibility(View.INVISIBLE);
                       }else {
                           Intent intent = new Intent(UserLogin.this, UserLoginOtp.class);
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