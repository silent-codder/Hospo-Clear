package com.cctpl.hospoclear.HospitalRegister.Fragment;

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
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.cctpl.hospoclear.HospitalRegister.Adapter.ActiveAppointmentAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.RequestAppointmentAdapter;
import com.cctpl.hospoclear.R;

import java.util.ArrayList;
import java.util.List;

public class AppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    List<AppointmentData> appointmentData;
    ActiveAppointmentAdapter activeAppointmentAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String UserId;
    LottieAnimationView lottieAnimationView;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        recyclerView = view.findViewById(R.id.requestAppointmentRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        lottieAnimationView = view.findViewById(R.id.lottie);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        textView = view.findViewById(R.id.notFoundText);
        progressBar = view.findViewById(R.id.progressCircular);


        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();
        return view;
    }
    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        appointmentData = new ArrayList<>();
        activeAppointmentAdapter = new ActiveAppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(activeAppointmentAdapter);

        Query query = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("HospitalId", UserId)
                .whereEqualTo("Status","Pending")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (!value.isEmpty()){
                    lottieAnimationView.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);

                    for (DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            String AppointmentId = doc.getDocument().getId();
                            AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                            appointmentData.add(mAppointmentData);
                            activeAppointmentAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }
}