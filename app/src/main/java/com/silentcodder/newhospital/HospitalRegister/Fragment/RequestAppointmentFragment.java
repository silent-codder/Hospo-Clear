package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
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
import com.silentcodder.newhospital.HospitalRegister.Adapter.RequestAppointmentAdapter;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;

import java.util.ArrayList;
import java.util.List;

public class RequestAppointmentFragment extends Fragment {

    RecyclerView recyclerView;
    List<AppointmentData> appointmentData;
    RequestAppointmentAdapter requestAppointmentAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    String HospitalId;
    LottieAnimationView lottieAnimationView;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_appointment, container, false);
        recyclerView = view.findViewById(R.id.requestAppointmentRecycleView);
        lottieAnimationView = view.findViewById(R.id.lottie);
        textView = view.findViewById(R.id.notFoundText);
        progressBar = view.findViewById(R.id.progressCircular);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        HospitalId = firebaseAuth.getCurrentUser().getUid();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
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
        requestAppointmentAdapter = new RequestAppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(requestAppointmentAdapter);

        //Status value is "3" means request for appointment
        Query query = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("HospitalId",HospitalId)
                .whereEqualTo("Status","3")
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
                        requestAppointmentAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

    }
}