package com.silentcodder.newhospital.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.TopDoctorAdapter;
import com.silentcodder.newhospital.UserRegister.Adapter.TopHospitalAdapter;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.ArrayList;
import java.util.List;

public class TopHospitalDoctorFragment extends Fragment {

    RecyclerView recyclerView;
    List<DoctorData> doctorData;
    TopDoctorAdapter topDoctorAdapter;
    FirebaseFirestore firebaseFirestore;
    String HospitalId;

    TextView mHospitalName,mCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_hospital_doctor, container, false);

        recyclerView = view.findViewById(R.id.topDoctorRecycleView);
        mHospitalName = view.findViewById(R.id.hospitalName);
        mCity = view.findViewById(R.id.city);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            HospitalId = bundle.getString("HospitalId");
        }

        getHospitalInfo(HospitalId);

        doctorData = new ArrayList<>();
        topDoctorAdapter = new TopDoctorAdapter(doctorData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(topDoctorAdapter);

        CollectionReference ref = firebaseFirestore.collection("Doctors");

        Query query = ref.whereEqualTo("HospitalId",HospitalId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        DoctorData mDoctorData = doc.getDocument().toObject(DoctorData.class);
                        doctorData.add(mDoctorData);
                        topDoctorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }

    private void getHospitalInfo(String hospitalId) {
        firebaseFirestore.collection("Hospitals").document(hospitalId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String HospitalName = task.getResult().getString("HospitalName");
                            String City = task.getResult().getString("City");
                            mHospitalName.setText(HospitalName);
                            mCity.setText(", "+City);
                        }
                    }
                });
    }
}