package com.cctpl.hospoclear.DoctorRegister.RegisterActivity;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;

public class DoctorLoginActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mBtnNext;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mBtnNext = findViewById(R.id.btnNext);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mEmail.getText().toString();
                String Password = mPassword.getText().toString();

                if (TextUtils.isEmpty(Email)){
                    mEmail.setError("fill up email");
                }else if (TextUtils.isEmpty(Password) && Password.length()<6){
                    mPassword.setError("Wrong password");
                }else {
                    ProgressBar progressBar = findViewById(R.id.loader);
                    progressBar.setVisibility(View.VISIBLE);
                    mBtnNext.setVisibility(View.INVISIBLE);
                    LoginUser(Email,Password);
                }
            }
        });
    }

    private void LoginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(DoctorLoginActivity.this, DoctorMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    |Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProgressBar progressBar = findViewById(R.id.loader);
                progressBar.setVisibility(View.INVISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
                mEmail.setError("Please enter correct information");
                mEmail.setText("");
                mPassword.setText("");
            }
        });
    }
}