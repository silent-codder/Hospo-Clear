package com.silentcodder.newhospital.UserRegister.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;

import java.util.List;

public class AboutDoctorFragment extends Fragment {

    String DoctorName;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    TextView mDoctorName,mSpeciality,mHospitalName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_doctor, container, false);
        Button btnRequestAppointment = view.findViewById(R.id.requestAppointment);

        mDoctorName = view.findViewById(R.id.doctorName);
        mSpeciality = view.findViewById(R.id.doctorSpeciality);
        mHospitalName = view.findViewById(R.id.hospitalName);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            DoctorName = bundle.getString("DoctorName");
        }

        firebaseFirestore.collection("Doctors").whereEqualTo("DoctorName",DoctorName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    if (list != null && list.size()>0){
                        for (DocumentSnapshot doc : list){
                            DoctorData doctorData = new DoctorData();
                            String DoctorName = doctorData.setDoctorName(doc.getString("DoctorName"));
                            String Specialist = doctorData.setSpeciality(doc.getString("Speciality"));
                            String HospitalId = doctorData.setHospitalId(doc.getString("HospitalId"));

                            mDoctorName.setText(DoctorName);
                            mSpeciality.setText(Specialist);

                            firebaseFirestore.collection("Hospitals").document(HospitalId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        String HospitalName = task.getResult().getString("HospitalName");
                                        mHospitalName.setText(HospitalName);
                                    }
                                }
                            });
                        }
                    }
                }

            }
        });

        btnRequestAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new BookAppointmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DoctorName" , DoctorName);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}