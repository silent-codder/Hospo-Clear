package com.cctpl.hospoclear.DoctorRegister.RegisterActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.$Gson$Preconditions;
import com.cctpl.hospoclear.R;

import java.util.ArrayList;
import java.util.List;

public class SelectHospitalActivity extends AppCompatActivity {

    Button mBtnContinue;
    FirebaseFirestore firebaseFirestore;
    String mHospitalName;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hospital);
        mBtnContinue = findViewById(R.id.btnContinue);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        Spinner HospitalName = findViewById(R.id.hospitalNames);
        List<String> subjects = new ArrayList<>();
        HospitalName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mHospitalName = subjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HospitalName.setAdapter(adapter);


        firebaseFirestore.collection("Hospitals").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("HospitalName");
                        subjects.add(subject);
                        progressDialog.dismiss();
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        mBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SelectHospitalActivity.this, mHospitalName, Toast.LENGTH_SHORT).show();

                firebaseFirestore.collection("Hospitals").whereEqualTo("HospitalName", mHospitalName)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isComplete()) {
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            if (list != null && list.size() > 0) {
                                for (DocumentSnapshot doc : list) {
                                    HospitalData hospitalData = new HospitalData();
                                    String HospitalId = hospitalData.setUserId(doc.getString("UserId"));
                                    Intent intent = new Intent(SelectHospitalActivity.this,AddDoctorActivity.class);
                                    intent.putExtra("HospitalId",HospitalId);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });

            }

        });
    }
}