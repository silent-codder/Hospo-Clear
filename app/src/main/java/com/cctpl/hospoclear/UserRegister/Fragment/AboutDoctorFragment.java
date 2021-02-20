package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutDoctorFragment extends Fragment {

    String HospitalName,DoctorId;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    TextView mDoctorName,mSpeciality,mExperience,mAboutDoctor;
    CircleImageView mDoctorImg,mDoctor;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_doctor, container, false);
        Button btnRequestAppointment = view.findViewById(R.id.requestAppointment);

        mDoctorName = view.findViewById(R.id.doctorName);
        mSpeciality = view.findViewById(R.id.doctorSpeciality);
        mAboutDoctor = view.findViewById(R.id.aboutDoctor);
        mDoctorImg = view.findViewById(R.id.doctorImg);
        mDoctor = view.findViewById(R.id.doctor);
        progressBar = view.findViewById(R.id.ImgLoader);
        mExperience = view.findViewById(R.id.doctorExperience);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            DoctorId = bundle.getString("DoctorId");
        }

        firebaseFirestore.collection("Doctors").document(DoctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String DoctorName = task.getResult().getString("DoctorName");
                            String Specialist = task.getResult().getString("Speciality");
                            String HospitalId = task.getResult().getString("HospitalId");
                            String Experience = task.getResult().getString("Experience");
                            String About = task.getResult().getString("DoctorBio");
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            progressBar.setVisibility(View.VISIBLE);

                            if (!TextUtils.isEmpty(About)){
                                mAboutDoctor.setText(About);
                            }

                            if(ProfileUrl != null){
                                mDoctor.setVisibility(View.INVISIBLE);
                                Picasso.get().load(ProfileUrl).into(mDoctorImg);
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            mDoctorName.setText(DoctorName);
                            mSpeciality.setText(Specialist);
                            mExperience.setText("+"+Experience);

                            firebaseFirestore.collection("Hospitals").document(HospitalId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        HospitalName = task.getResult().getString("HospitalName");
                                    }
                                }
                            });
                        }
                    }
                });

        btnRequestAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new BookAppointmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DoctorId" ,DoctorId);
                bundle.putString("HospitalName",HospitalName);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}