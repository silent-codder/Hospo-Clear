package com.cctpl.hospoclear.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalProfileFragment extends Fragment {

    CircleImageView mHospitalImg;
    TextView mHospitalName,mHospitalAddress,mHospitalContactNumber,mHospitalBio;
    Button mBtnEditProfile;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_profile, container, false);

        mHospitalImg = view.findViewById(R.id.hospitalImg);
        mHospitalName = view.findViewById(R.id.hospitalName);
        mHospitalAddress = view.findViewById(R.id.hospitalAddress);
        mHospitalContactNumber = view.findViewById(R.id.hospitalContactNumber);
        mHospitalBio = view.findViewById(R.id.hospitalBio);
        mBtnEditProfile = view.findViewById(R.id.btnEditProfile);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();


        firebaseFirestore.collection("Hospitals").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            String HospitalName = task.getResult().getString("HospitalName");
                            String City = task.getResult().getString("City");
                            String State = task.getResult().getString("State");
                            String ContactNumber = task.getResult().getString("ContactNumber");
                            String Bio = task.getResult().getString("HospitalBio");
                            String ProfileUrl =  task.getResult().getString("ProfileImgUrl");

                            mHospitalName.setText(HospitalName);
                            mHospitalAddress.setText(City+", "+State);
                            mHospitalContactNumber.setText(ContactNumber);
                            mHospitalBio.setText(Bio);
                            Picasso.get().load(ProfileUrl).into(mHospitalImg);

                        }
                    }
                });

        mBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(),EditHospitalProfile.class));
            }
        });

        return view;
    }
}