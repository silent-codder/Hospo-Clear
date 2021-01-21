package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.HospitalRegister.Adapter.AllDoctorAdapter;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.TopDoctorAdapter;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;

import java.util.ArrayList;
import java.util.List;


public class AllDoctorFragment extends Fragment {
    RecyclerView recyclerView;
    List<DoctorData> doctorData;
    AllDoctorAdapter allDoctorAdapter;
    FirebaseFirestore firebaseFirestore;
    String HospitalId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_doctor, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HospitalId = firebaseAuth.getCurrentUser().getUid();
        doctorData = new ArrayList<>();
        allDoctorAdapter = new AllDoctorAdapter(doctorData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(allDoctorAdapter);

        CollectionReference ref = firebaseFirestore.collection("Doctors");

        Query query = ref.whereEqualTo("HospitalId",HospitalId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String DoctorId = doc.getDocument().getId();
                        DoctorData mDoctorData = doc.getDocument().toObject(DoctorData.class).withId(DoctorId);
                        doctorData.add(mDoctorData);
                        allDoctorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }
}