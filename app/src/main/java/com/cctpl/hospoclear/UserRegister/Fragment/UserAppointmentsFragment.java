package com.cctpl.hospoclear.UserRegister.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.AppointmentAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;

import java.util.ArrayList;
import java.util.List;

public class UserAppointmentsFragment extends Fragment {

    AppointmentAdapter appointmentAdapter;
    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    LottieAnimationView lottieAnimationView;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_appointments, container, false);

        recyclerView = view.findViewById(R.id.appointmentRecycleView);
        lottieAnimationView = view.findViewById(R.id.lottie);
        textView = view.findViewById(R.id.notFoundText);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

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
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        appointmentData = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appointmentAdapter);

        swipeRefreshLayout.setRefreshing(false);
        Query query1 = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("UserId",UserId)
                .orderBy("TimeStamp", Query.Direction.DESCENDING);

        query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.isEmpty()){
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String AppointmentId = doc.getDocument().getId();
                        AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                        appointmentData.add(mAppointmentData);
                        appointmentAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }
}