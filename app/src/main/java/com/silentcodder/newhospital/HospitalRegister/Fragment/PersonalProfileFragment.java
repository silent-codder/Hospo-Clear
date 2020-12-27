package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalProfileFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    TextView mDoctorName,mDoctorSpeciality,mDoctorQualification,mDoctorBio;
    Button mBtnEditProfile;
    CircleImageView mDoctorImg;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_profile, container, false);
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

        firebaseFirestore.collection("Doctors").whereEqualTo("HospitalId",UserId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                if (list!=null && list.size()>0){
                    for (DocumentSnapshot doc : list) {
                        DoctorData doctorData = new DoctorData();
                        String DoctorName = doctorData.setDoctorName(doc.getString("DoctorName"));
                        String Speciality = doctorData.setSpeciality(doc.getString("Speciality"));
                        String Qualification = doctorData.setQualification(doc.getString("Qualification"));
                        String Experience = doctorData.setExperience(doc.getString("Experience"));
                        String ProfileUrl = doctorData.setProfileImgUrl(doc.getString("ProfileImgUrl"));
                        String DoctorBio = doctorData.setDoctorBio(doc.getString("DoctorBio"));
                        mDoctorName.setText(DoctorName);
                        mDoctorSpeciality.setText(Speciality+ ", " + Experience);
                        mDoctorQualification.setText(Qualification);
                        if (DoctorBio.isEmpty()){
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
            }
        });

        mBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditPersonalProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
        return view;
    }
}