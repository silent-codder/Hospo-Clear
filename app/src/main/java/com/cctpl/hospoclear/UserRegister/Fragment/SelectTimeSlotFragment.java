package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.hospoclear.HospitalRegister.Adapter.SelectTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.TimeSlotAdapter;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectTimeSlotFragment extends Fragment {

    List<TimeSlotData> timeSlotData;
    RecyclerView recyclerView;
    SelectTimeSlotAdapter timeSlotAdapter;
    FirebaseFirestore firebaseFirestore;
    String DoctorId,HospitalId,Problem,Date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_time_slot, container, false);

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            Problem = bundle.getString("Problem");
            Date = bundle.getString("Date");
            DoctorId = bundle.getString("DoctorId");
            HospitalId = bundle.getString("HospitalId");
        }
        recyclerView = view.findViewById(R.id.recycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        timeSlotData = new ArrayList<>();
        timeSlotAdapter = new SelectTimeSlotAdapter(timeSlotData);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(timeSlotAdapter);

        Query query =  firebaseFirestore.collection("Doctors").document(DoctorId).collection("TimeSlots")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String timeSlotId = doc.getDocument().getId();
                        TimeSlotData mAppointmentData = doc.getDocument().toObject(TimeSlotData.class).withId(timeSlotId);
                        timeSlotData.add(mAppointmentData);
                        timeSlotAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}