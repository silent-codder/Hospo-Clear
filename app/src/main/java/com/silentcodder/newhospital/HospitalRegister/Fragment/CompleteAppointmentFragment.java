package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.HospitalRegister.Adapter.ActiveAppointmentAdapter;
import com.silentcodder.newhospital.HospitalRegister.Adapter.CompleteAppointmentAdapter;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;

import java.util.ArrayList;
import java.util.List;

public class CompleteAppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    List<AppointmentData> appointmentData;
    CompleteAppointmentAdapter completeAppointmentAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_appointment, container, false);

        recyclerView = view.findViewById(R.id.CompleteAppointmentRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String HospitalId = firebaseAuth.getCurrentUser().getUid();
        appointmentData = new ArrayList<>();
        completeAppointmentAdapter = new CompleteAppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(completeAppointmentAdapter);

        progressBar = view.findViewById(R.id.progressCircular);

        Query query = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("HospitalId",HospitalId)
                .whereEqualTo("Status","1")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.isEmpty()){
                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie);
                    TextView textView = view.findViewById(R.id.notFoundText);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String AppointmentId = doc.getDocument().getId();
                        AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                        appointmentData.add(mAppointmentData);
                        completeAppointmentAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        return view;
    }
}