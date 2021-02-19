package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.HospitalRegister.Adapter.SelectEveningTimeSlotAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.SelectTimeSlotAdapter;
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
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.UserAdapter;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.cctpl.hospoclear.UserRegister.Model.UserData;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class BookAppointmentFragment extends Fragment {

    TextView mDoctorName,mSpeciality,mHospitalName;
    EditText mProblem;
    RelativeLayout mDateLayout;
    String DoctorId,UserId,date,HospitalName,HospitalId;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    List<TimeSlotData> timeSlotData;
    List<EveningTimeSlotData> eveningTimeSlotData;
    RecyclerView recyclerViewMorning,recyclerViewEvening;
    SelectTimeSlotAdapter selectTimeSlotAdapter;
    SelectEveningTimeSlotAdapter selectEveningTimeSlotAdapter;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        mProblem = view.findViewById(R.id.problem);
        mDateLayout = view.findViewById(R.id.dateLayout);
        mDoctorName = view.findViewById(R.id.doctorName);
        mSpeciality = view.findViewById(R.id.doctorSpeciality);
        mHospitalName = view.findViewById(R.id.hospitalName);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            DoctorId = bundle.getString("DoctorId");
            HospitalName = bundle.getString("HospitalName");
            mHospitalName.setText(HospitalName);
        }

        firebaseFirestore.collection("Doctors").document(DoctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String DoctorName = task.getResult().getString("DoctorName");
                            String Specialist = task.getResult().getString("Speciality");
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            HospitalId = task.getResult().getString("HospitalId");

                            if (ProfileUrl != null){
                                CircleImageView circleImageView = view.findViewById(R.id.doctorImg);
                                Picasso.get().load(ProfileUrl).into(circleImageView);
                            }

                            mDoctorName.setText(DoctorName);
                            mSpeciality.setText(Specialist);
                            progressDialog.dismiss();
                        }
                    }
                });

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
            public void onDateSelected(Calendar calendar, int position) {
                String St_Date ;
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                date = day + " / " + month + " / " + year;
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppointmentData",0);
                Editor editor = sharedPreferences.edit();
                editor.putString("AppointmentDate",date);
                editor.commit();
            }
        });

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



//        mBtnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String problem = mProblem.getText().toString();
//                if (TextUtils.isEmpty(problem)){
//                    mProblem.setError("Please mention problem");
//                }else if (date == null){
//                    Toast.makeText(getContext(), "Choose appointment date", Toast.LENGTH_SHORT).show();
//                }else {
//                    Fragment fragment = new SelectTimeSlotFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Problem",problem);
//                    bundle.putString("Date",date);
//                    bundle.putString("DoctorId",DoctorId);
//                    bundle.putString("HospitalId",HospitalId);
//                    fragment.setArguments(bundle);
//                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
//                }
//            }
//        });
        return view;
    }

}