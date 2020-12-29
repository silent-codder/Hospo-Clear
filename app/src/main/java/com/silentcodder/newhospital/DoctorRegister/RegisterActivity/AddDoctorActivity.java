package com.silentcodder.newhospital.DoctorRegister.RegisterActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;

public class AddDoctorActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mBtnNext;
    String HospitalId;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        mEmail = findViewById(R.id.emailAddress);
        mPassword = findViewById(R.id.password);
        mBtnNext = findViewById(R.id.btnNext);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        HospitalId = getIntent().getStringExtra("HospitalId");
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mEmail.getText().toString();
                String Password = mPassword.getText().toString();

                if (TextUtils.isEmpty(Email)){
                    mEmail.setError("Email");
                }else if (TextUtils.isEmpty(Password) && Password.length()<6){
                    mPassword.setError(" Create password is greater than 6 digit");
                }else {
                    RegisterUser(Email,Password);
                }
            }
        });
    }

    private void RegisterUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AddDoctorActivity.this,DoctorRegisterActivity.class);
                    intent.putExtra("Email",email);
                    intent.putExtra("Password",password);
                    intent.putExtra("HospitalId",HospitalId);
                    Toast.makeText(AddDoctorActivity.this, HospitalId, Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddDoctorActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}