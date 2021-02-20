package com.cctpl.hospoclear.UserRegister.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cctpl.hospoclear.UserRegister.Adapter.SelectEveningTimeSlotAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.SelectTimeSlotAdapter;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.EveningTimeSlotData;
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
    List<EveningTimeSlotData> eveningTimeSlotData;
    RecyclerView recyclerViewMorning,recyclerViewEvening;
    SelectTimeSlotAdapter selectTimeSlotAdapter;
    SelectEveningTimeSlotAdapter selectEveningTimeSlotAdapter;
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
        recyclerViewMorning = view.findViewById(R.id.recycleViewMorning);
        recyclerViewEvening = view.findViewById(R.id.recycleViewEvening);
        firebaseFirestore = FirebaseFirestore.getInstance();
        timeSlotData = new ArrayList<>();
        selectTimeSlotAdapter = new SelectTimeSlotAdapter(timeSlotData);
        recyclerViewMorning.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerViewMorning.setAdapter(selectTimeSlotAdapter);
        recyclerViewMorning.setHasFixedSize(true);

        Query query =  firebaseFirestore.collection("Doctors").document(DoctorId).collection("Morning")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String timeSlotId = doc.getDocument().getId();
                        TimeSlotData mAppointmentData = doc.getDocument().toObject(TimeSlotData.class).withId(timeSlotId);
                        timeSlotData.add(mAppointmentData);
                        selectTimeSlotAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        eveningTimeSlotData = new ArrayList<>();
        selectEveningTimeSlotAdapter = new SelectEveningTimeSlotAdapter(eveningTimeSlotData);
        recyclerViewEvening.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerViewEvening.setAdapter(selectEveningTimeSlotAdapter);
        recyclerViewEvening.setHasFixedSize(true);

        Query query2 =  firebaseFirestore.collection("Doctors").document(DoctorId).collection("Evening")
                .orderBy("TimeStamp", Query.Direction.ASCENDING);
        query2.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String timeSlotId = doc.getDocument().getId();
                        EveningTimeSlotData mAppointmentData = doc.getDocument().toObject(EveningTimeSlotData.class).withId(timeSlotId);
                        eveningTimeSlotData.add(mAppointmentData);
                        selectEveningTimeSlotAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppointmentData",0);
        String TimeSlot = sharedPreferences.getString("TimeSlot",null);

        Button button = view.findViewById(R.id.btnNext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(TimeSlot)){
                    Toast toast = Toast.makeText(getContext(), "Select Time Slot", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else {
                    Fragment fragment = new SelectUserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Problem",Problem);
                    bundle.putString("Date",Date);
                    bundle.putString("DoctorId",DoctorId);
                    bundle.putString("HospitalId",HospitalId);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                }
            }
        });
        return view;
    }
}