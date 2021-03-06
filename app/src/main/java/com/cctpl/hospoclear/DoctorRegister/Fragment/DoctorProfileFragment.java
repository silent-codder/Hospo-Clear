package com.cctpl.hospoclear.DoctorRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cctpl.hospoclear.HospitalRegister.Fragment.EditPersonalProfileFragment;
import com.cctpl.hospoclear.HospitalRegister.Fragment.TimeSlotFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class DoctorProfileFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    TextView mDoctorName,mDoctorSpeciality,mDoctorQualification,mDoctorBio;
    Button mBtnEditProfile;
    CircleImageView mDoctorImg;
    ProgressDialog progressDialog;
    String HospitalId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mDoctorName = view.findViewById(R.id.doctorName);
        mDoctorSpeciality = view.findViewById(R.id.doctorSpeciality);
        mDoctorBio = view.findViewById(R.id.doctorBio);
        mDoctorQualification = view.findViewById(R.id.doctorQualification);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mDoctorImg = view.findViewById(R.id.doctorImg);
        mBtnEditProfile = view.findViewById(R.id.btnEditProfile);


        Log.d(TAG, "UserId: " + UserId);

        firebaseFirestore.collection("Doctors").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String DoctorName = task.getResult().getString("DoctorName");
                            String Speciality = task.getResult().getString("Speciality");
                            String Qualification = task.getResult().getString("Qualification");
                            HospitalId = task.getResult().getString("HospitalId");
                            Log.d(TAG, "onCreateView: " + HospitalId);
                            String Experience = task.getResult().getString("Experience");
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            String DoctorBio = task.getResult().getString("DoctorBio");
                            mDoctorName.setText(DoctorName);
                            mDoctorSpeciality.setText(Speciality+ ", " + Experience);
                            mDoctorQualification.setText(Qualification);
                            if (TextUtils.isEmpty(DoctorBio)){
                                mDoctorBio.setText("Nothing about you..");
                            }else {
                                mDoctorBio.setText(DoctorBio);
                            }
                            if (ProfileUrl!=null){
                                Picasso.get().load(ProfileUrl).into(mDoctorImg);
                            }

                            progressDialog.dismiss();
                        }
                    }
                });



        Log.d(TAG, "onCreateView: " + HospitalId);

//

        mBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditPersonalProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        Button button = view.findViewById(R.id.btnTimeSlots);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TimeSlotFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });


        return view;
    }
}