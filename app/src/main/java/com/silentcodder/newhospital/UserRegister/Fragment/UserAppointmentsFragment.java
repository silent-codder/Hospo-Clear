package com.silentcodder.newhospital.UserRegister.Fragment;

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
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.AppointmentAdapter;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;

import java.util.ArrayList;
import java.util.List;

public class UserAppointmentsFragment extends Fragment {

    AppointmentAdapter appointmentAdapter;
    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_appointments, container, false);

        recyclerView = view.findViewById(R.id.appointmentRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        appointmentData = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appointmentAdapter);


        Query query1 = firebaseFirestore.collectionGroup("Appointments").whereEqualTo("UserId",UserId)
                .orderBy("TimeStamp", Query.Direction.DESCENDING);

                query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String AppointmentId = doc.getDocument().getId();
                                AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                                appointmentData.add(mAppointmentData);
                                appointmentAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


        return view;
    }
}