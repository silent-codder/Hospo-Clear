package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.AppointmentTrackAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.BookmarkDoctorAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.UserRegister.Model.BookmarkDoctorData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class TrackAppointmentFragment extends Fragment {


    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    String UserId,AppointmentId;

    List<AppointmentData> appointmentData;
    AppointmentTrackAdapter appointmentTrackAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_appointment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            AppointmentId = bundle.getString("AppointmentId");
        }

        recyclerView = view.findViewById(R.id.recycleViewAppointmentTrack);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        appointmentData = new ArrayList<>();
        appointmentTrackAdapter = new AppointmentTrackAdapter(appointmentData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(appointmentTrackAdapter);


        firebaseFirestore.collection("Appointments").document(AppointmentId)
                .collection("Status").orderBy("TimeStamp", Query.Direction.DESCENDING)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        AppointmentData mDoctorData = doc.getDocument().toObject(AppointmentData.class);
                        appointmentData.add(mDoctorData);
                        appointmentTrackAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }
}