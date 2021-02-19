package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.HospitalRegister.Adapter.EveningTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.SelectEveningTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.SelectTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.TimeSlotAdapter;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.EveningTimeSlotData;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class RequestAppointmentFragment extends Fragment {

    RelativeLayout DateLayout,TimeLayout,SelectUserLayout,confirmAppointmentLayout;

    Calendar calendar;
    CalendarView calendarView;

    Button mBtnNextDate,mBtnNextTime,mBtnNextSelectUser;

    RecyclerView recyclerViewMorning,recyclerViewEvening;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String CurrentUserId,DoctorId,HospitalId,date,patientName;
    SharedPreferences sharedPreferences;

    List<TimeSlotData> timeSlotData;
    List<EveningTimeSlotData> eveningTimeSlotData;

    SelectTimeSlotAdapter selectTimeSlotAdapter;
    SelectEveningTimeSlotAdapter selectEveningTimeSlotAdapter;

    TextView mPatientName,mRelationship,mAppointmentDate,mAppointmentTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_appointment2, container, false);
        findIds(view);
        Bundle bundle = this.getArguments();
        if (bundle!=null){
            DoctorId = bundle.getString("DoctorId");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        CurrentUserId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Doctors").document(DoctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            HospitalId = task.getResult().getString("HospitalId");
                        }
                    }
                });

        sharedPreferences = getContext().getSharedPreferences("AppointmentData",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        //set calenderView from current date
        long dat = calendarView.getDate()+7;
        calendarView.setMinDate(dat);

        calendar = Calendar.getInstance();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd / MM /yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;
                date = dayOfMonth + " / " +month+ " / " +year;

            }
        });

        mBtnNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (TextUtils.isEmpty(date)){
                   Toast toast = Toast.makeText(getContext(),"Select Date",Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER,0,0);
                   toast.show();
               }else {
                    editor.putString("AppointmentDate",date);
                    editor.commit();
                    DateLayout.setVisibility(View.GONE);
                    TimeLayout.setVisibility(View.VISIBLE);
               }
            }
        });

        // Select timeLayout
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

        mBtnNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeLayout.setVisibility(View.GONE);
                SelectUserLayout.setVisibility(View.VISIBLE);
            }
        });

        //select user layout

        RadioButton radioButton_You = view.findViewById(R.id.you);
        RadioButton radioButton_SomeoneElse = view.findViewById(R.id.someoneElse);

        RelativeLayout You = view.findViewById(R.id.name_layout);
        RelativeLayout SomeoneElse = view.findViewById(R.id.anotherPatient);
        TextView PatientName = view.findViewById(R.id.patientName);
        firebaseFirestore.collection("Users").document(CurrentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            patientName = task.getResult().getString("UserName");
                            PatientName.setText(patientName);
                        }
                    }
                });

        radioButton_You.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SomeoneElse.setVisibility(View.GONE);
                You.setVisibility(View.VISIBLE);
                mBtnNextSelectUser.setVisibility(View.VISIBLE);
                mBtnNextSelectUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.putString("PatientName",patientName);
                        editor.putString("RelationShip","MySelf");
                        editor.commit();
                        confirmAppointmentLayout.setVisibility(View.VISIBLE);
                        SelectUserLayout.setVisibility(View.GONE);
                    }
                });
            }
        });

        EditText anotherPatientName = view.findViewById(R.id.anotherPatientName);
        EditText relation = view.findViewById(R.id.relation);
        radioButton_SomeoneElse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                You.setVisibility(View.GONE);
                SomeoneElse.setVisibility(View.VISIBLE);
                mBtnNextSelectUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PatientName = anotherPatientName.getText().toString();
                        String relationship = relation.getText().toString();

                        if (TextUtils.isEmpty(PatientName)){
                            anotherPatientName.setError("Enter name");
                        }else if (TextUtils.isEmpty(relationship)){
                            relation.setError("Relation");
                        }else {
                            editor.putString("PatientName",PatientName);
                            editor.putString("RelationShip",relationship);
                            editor.commit();
                            confirmAppointmentLayout.setVisibility(View.VISIBLE);
                            SelectUserLayout.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });


        //confirmAppointmentLayout

        String Patient_Name = sharedPreferences.getString("PatientName",null);
        String Relation = sharedPreferences.getString("RelationShip",null);
        String Appointment_Time = sharedPreferences.getString("TimeSlot",null);
        String Appointment_Date = sharedPreferences.getString("AppointmentDate",null);
        String Doctor_Id = sharedPreferences.getString("DoctorId",null);

        mPatientName.setText(Patient_Name);
        mRelationship.setText(Relation);
        mAppointmentDate.setText(Appointment_Date);
        mAppointmentTime.setText(Appointment_Time);


        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calenderView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                String St_Date ;
                int day = date.get(Calendar.DATE);
                int month = date.get(Calendar.MONTH);
                int year = date.get(Calendar.YEAR);
                St_Date = day + " / " + month + " / " + year;
                Toast.makeText(getContext(), St_Date,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void findIds(View view) {
        DateLayout = view.findViewById(R.id.dateLayout);
        TimeLayout = view.findViewById(R.id.timeLayout);
        SelectUserLayout = view.findViewById(R.id.selectUserLayout);
        confirmAppointmentLayout = view.findViewById(R.id.confirmAppointmentLayout);

        recyclerViewMorning = view.findViewById(R.id.recycleViewMorning);
        recyclerViewEvening = view.findViewById(R.id.recycleViewEvening);

        calendarView = view.findViewById(R.id.appointmentDate);
        mBtnNextDate = view.findViewById(R.id.btnNextDate);
        mBtnNextTime = view.findViewById(R.id.btnNextTime);
        mBtnNextSelectUser = view.findViewById(R.id.btnNextSelectUser);

        mPatientName = view.findViewById(R.id.PatientName);
        mRelationship = view.findViewById(R.id.relation);
        mAppointmentDate = view.findViewById(R.id.appointmentDateText);
        mAppointmentTime = view.findViewById(R.id.appointmentTimeText);
    }
}