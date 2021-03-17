package com.cctpl.hospoclear.DoctorRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.cctpl.hospoclear.HospitalRegister.Adapter.CompleteAppointmentAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorCompleteAppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    List<AppointmentData> appointmentData;
    CompleteAppointmentAdapter completeAppointmentAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    String UserId;
    LottieAnimationView lottieAnimationView;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_complete_appointment, container, false);
        recyclerView = view.findViewById(R.id.CompleteAppointmentRecycleView);
        lottieAnimationView = view.findViewById(R.id.lottie);
        textView = view.findViewById(R.id.notFoundText);
        progressBar = view.findViewById(R.id.progressCircular);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();
        return view;
    }


    private void refreshData() {
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        appointmentData = new ArrayList<>();
        completeAppointmentAdapter = new CompleteAppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(completeAppointmentAdapter);

        Query query = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("DoctorId",UserId)
                .whereEqualTo("Status","Complete")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.isEmpty()){
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
    }
}