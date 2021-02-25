package com.cctpl.hospoclear.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.HospitalRegister.Adapter.EveningTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.TimeSlotAdapter;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.cctpl.hospoclear.UserRegister.Model.DoctorId;
import com.cctpl.hospoclear.UserRegister.Model.EveningTimeSlotData;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TimeSlotFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    EditText mHr,mMin,mToHr,mToMin;
    RadioButton radioButtonAM,radioButtonPM,radioButtonToAM,radioButtonToPM;
    Button mAddSlot;
    String AmOrPm,ToAmOrPm,DoctorId,MIN,Section;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView,recyclerView2;
    List<TimeSlotData> doctorData;
    List<EveningTimeSlotData> eveningTimeSlotData;
    TimeSlotAdapter timeSlotAdapter;
    EveningTimeSlotAdapter eveningTimeSlotAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_slot, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView2 = view.findViewById(R.id.recycleView2);
        DoctorId = firebaseAuth.getCurrentUser().getUid();

        RelativeLayout relativeLayoutFrom = view.findViewById(R.id.layoutFrom);
        RelativeLayout relativeLayoutTo = view.findViewById(R.id.layoutTo);
        RelativeLayout relativeLayoutTime = view.findViewById(R.id.layoutTime);

        mHr = view.findViewById(R.id.hr);
        mMin = view.findViewById(R.id.min);
        radioButtonAM = view.findViewById(R.id.am);
        radioButtonPM = view.findViewById(R.id.pm);
        mToHr = view.findViewById(R.id.toHr);
        mToMin = view.findViewById(R.id.toMin);
        radioButtonToAM = view.findViewById(R.id.toAm);
        radioButtonToPM = view.findViewById(R.id.toPm);
        mAddSlot = view.findViewById(R.id.btnAddSlot);
        TextView textView = view.findViewById(R.id.text2);
        RadioGroup relativeLayout = view.findViewById(R.id.radioGroup1);
        Button mBtnCancel = view.findViewById(R.id.btnCancel);
        textView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.VISIBLE);
                textView.setText("Set Time");
            }
        });

        RadioButton radioButtonMorningSec = view.findViewById(R.id.morningSection);
        RadioButton radioButtonEveningSec = view.findViewById(R.id.eveningSection);

        radioButtonEveningSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Section = "Evening";
                relativeLayoutFrom.setVisibility(View.VISIBLE);
            }
        });
        radioButtonMorningSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Section = "Morning";
                relativeLayoutFrom.setVisibility(View.VISIBLE);
            }
        });


        radioButtonPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmOrPm = "PM";
                relativeLayoutTo.setVisibility(View.VISIBLE);
            }
        });
        radioButtonAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmOrPm = "AM";
                relativeLayoutTo.setVisibility(View.VISIBLE);
            }
        });
        radioButtonToPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToAmOrPm = "PM";
                relativeLayoutTime.setVisibility(View.VISIBLE);
            }
        });
        radioButtonToAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToAmOrPm = "AM";
                relativeLayoutTime.setVisibility(View.VISIBLE);
            }
        });


        RadioButton MIN_10 = view.findViewById(R.id.Min10);
        RadioButton MIN_15 = view.findViewById(R.id.Min15);
        RadioButton MIN_20 = view.findViewById(R.id.Min20);
        RadioButton MIN_30 = view.findViewById(R.id.Min30);


        MIN_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MIN = "15";
                mAddSlot.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
            }
        });
        MIN_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MIN = "30";
                mAddSlot.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
            }
        });
        MIN_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MIN = "20";
                mAddSlot.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
            }
        });
        MIN_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MIN = "10";
                mAddSlot.setVisibility(View.VISIBLE);
                mBtnCancel.setVisibility(View.VISIBLE);
            }
        });

        mAddSlot.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String FromHr = mHr.getText().toString();
                String FromMin = mMin.getText().toString();
                String ToHr = mToHr.getText().toString();
                String ToMin = mToMin.getText().toString();

                if (TextUtils.isEmpty(FromHr)){
                    mHr.setError("Empty");
                }else if (TextUtils.isEmpty(FromMin)){
                    mMin.setError("Empty");
                }else if (TextUtils.isEmpty(ToHr)){
                    mToHr.setError("Empty");
                }else if (TextUtils.isEmpty(ToMin)){
                    mToMin.setError("Empty");
                }else if (TextUtils.isEmpty(AmOrPm)){
                    Toast.makeText(getContext(), "Set AM Or PM", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ToAmOrPm)){
                    Toast.makeText(getContext(), "Set AM Or PM", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(MIN)){
                    Toast.makeText(getContext(), "Set appointment time", Toast.LENGTH_SHORT).show();
                }else {
                    CreateTimeSlot(FromHr,FromMin,ToHr,ToMin,AmOrPm,ToAmOrPm,MIN);
                    relativeLayoutFrom.setVisibility(View.GONE);
                    relativeLayoutTo.setVisibility(View.GONE);
                    relativeLayoutTime.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                    mBtnCancel.setVisibility(View.GONE);
                    mAddSlot.setVisibility(View.GONE);
                    textView.setText("Create Slots");
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                relativeLayoutFrom.setVisibility(View.GONE);
                relativeLayoutTo.setVisibility(View.GONE);
                relativeLayoutTime.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                mBtnCancel.setVisibility(View.GONE);
                mAddSlot.setVisibility(View.GONE);
                textView.setText("Create Slots");
            }
        });

        doctorData = new ArrayList<>();
        timeSlotAdapter = new TimeSlotAdapter(doctorData);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(timeSlotAdapter);
        recyclerView.setHasFixedSize(true);

       Query query =  firebaseFirestore.collection("Doctors").document(DoctorId).collection("Morning")
               .orderBy("TimeStamp", Query.Direction.ASCENDING);
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String timeSlotId = doc.getDocument().getId();
                                TimeSlotData mAppointmentData = doc.getDocument().toObject(TimeSlotData.class).withId(timeSlotId);
                                doctorData.add(mAppointmentData);
                                timeSlotAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        eveningTimeSlotData = new ArrayList<>();
        eveningTimeSlotAdapter = new EveningTimeSlotAdapter(eveningTimeSlotData);
        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView2.setAdapter(eveningTimeSlotAdapter);
        recyclerView2.setHasFixedSize(true);

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
                        eveningTimeSlotAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

    private void CreateTimeSlot(String fromHr, String fromMin, String toHr, String toMin, String amOrPm, String toAmOrPm,String Min) {
        int start_ms = Integer.parseInt(fromHr); int start_min = Integer.parseInt(fromMin);
        int end_ms = Integer.parseInt(toHr);  int end_min = Integer.parseInt(toMin);

        int SlotTime = Integer.parseInt(Min);
        int startMs = 0;

        int rmin =0 ;

        String start_timezon = amOrPm;
        String End_timezon = toAmOrPm;

        int Morinng_totalCountome =00 ; int  Morning_total_min = 00;
        int evening_totalCount =00;    int  evening_total_min =00;



        if(start_timezon == "AM" && End_timezon =="PM" ){

            Morinng_totalCountome = 12 - start_ms-1  ;
            evening_totalCount = end_ms ;
            Morning_total_min = 60-start_min ;
            evening_total_min = end_min ;
        }
        else if(start_timezon  == End_timezon  ){
            if(start_timezon == "AM"){
                Morinng_totalCountome = end_ms - start_ms;
                evening_totalCount = 0 ;
                Morning_total_min = 60-start_min ;
                evening_total_min = end_min ;
            }else{

                Morinng_totalCountome = 0;
                evening_totalCount = end_ms - start_ms ;
                Morning_total_min = 60-start_min ;
                evening_total_min = end_min ;
            }

        }

        int totalMin = Morning_total_min+evening_total_min ;
        int totalHr =  Morinng_totalCountome  + evening_totalCount ;

        if(start_min == end_min ){
            totalMin =0;
            totalHr ++ ;
        }
        else{
            if(totalMin >60)
            {
                for(int i =0 ;i<totalMin;i++)
                {
                    if(i%60==0)
                    {
                        totalHr= totalHr+1;
                        rmin = 0 ;
                    }
                    rmin ++;
                }
                totalMin = rmin ;
                totalHr = totalHr -1 ;
            }

        }
       // System.out.println( "Total Time "+totalHr +"." + totalMin);

        int finalTotalMin = totalHr*60+totalMin ;
        int numOfSlot = finalTotalMin/SlotTime ;
        numOfSlot += 4;
        for(int i =0 ;i< numOfSlot;i++){
            if(start_min<60){
                int min = start_min + SlotTime;
                if(start_ms < 12 && start_timezon!=End_timezon){
                    if(start_min == 0){
//                        System.out.println( start_ms +" : " + "00"  + " AM" );
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("TimeSlot",start_ms + " : 00 AM" );
                        map.put("DoctorId",DoctorId);
                        map.put("Hr",start_ms);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("Flag","false");
                        AddData(map);
                    }else{
//                        System.out.println( start_ms +" : " + start_min  + " AM" );
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("TimeSlot",start_ms + " : " + start_min + " AM" );
                        map.put("DoctorId",DoctorId);
                        map.put("Hr",start_ms);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("Flag","false");
                        AddData(map);
                    }

                }else if(start_timezon == End_timezon){
                    if(start_min == 0){
//                        System.out.println( start_ms +" : " + "00"  + " " + start_timezon );
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("TimeSlot",start_ms + " : 00 " + start_timezon );
                        map.put("DoctorId",DoctorId);
                        map.put("Hr",start_ms);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("Flag","false");
                        AddData(map);
                    }else{
//                        System.out.println( start_ms +" : " + start_min  + " " + start_timezon );
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("TimeSlot",start_ms + " : " + start_min + " " + start_timezon );
                        map.put("DoctorId",DoctorId);
                        map.put("Hr",start_ms);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("Flag","false");
                        AddData(map);
                    }
                }else{

                    if(start_ms == 12){
                        if(start_min == 0){
//                            System.out.println( start_ms +" : " + "00"  + " PM" );
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("TimeSlot",start_ms + " : 00 PM");
                            map.put("DoctorId",DoctorId);
                            map.put("Hr",start_ms);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("Flag","false");
                            AddData(map);
                        }else{
//                            System.out.println( start_ms +" : " + start_min  + " PM" );
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("TimeSlot",start_ms + " : " + start_min + " PM" );
                            map.put("DoctorId",DoctorId);
                            map.put("Hr",start_ms);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("Flag","false");
                            AddData(map);
                        }
                    }

                    if(start_ms != 12 ){
                        startMs = start_ms;
                        startMs = 1;
                        if(start_min == 0){
//                            System.out.println( startMs +" : " + "00"  + " PM" );
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("TimeSlot",startMs + " : 00 PM");
                            map.put("DoctorId",DoctorId);
                            map.put("Hr",start_ms);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("Flag","false");
                            AddData(map);
                        }else{
//                            System.out.println( startMs +" : " + start_min  + " PM" );
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("TimeSlot",startMs + " : " + start_min + " PM" );
                            map.put("DoctorId",DoctorId);
                            map.put("Hr",start_ms);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("Flag","false");
                            AddData(map);
                        }
                    }

                }
                start_min = start_min+ SlotTime ;
            }
            else {
                start_ms++;
                start_min =00;
            }
        }
    }

    private void AddData(HashMap<String, Object> map) {
        firebaseFirestore.collection("Doctors").document(DoctorId).collection(Section)
                .add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){

                }
            }
        });
    }
}