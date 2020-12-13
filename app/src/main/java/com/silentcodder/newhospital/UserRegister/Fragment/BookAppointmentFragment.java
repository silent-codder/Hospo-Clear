package com.silentcodder.newhospital.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.UserAdapter;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.silentcodder.newhospital.UserRegister.Model.UserData;

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

public class BookAppointmentFragment extends Fragment {

    CalendarView calendarView;
    TextView textView,mDoctorName,mSpeciality,mHospitalName;
    Calendar calendar;
    Button mBtnNext;
    RelativeLayout mDateLayout,mTimeLayout;
    String DoctorName,UserId;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;

    List<UserData> userData;
    UserAdapter userAdapter;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        calendarView = view.findViewById(R.id.appDate);
        textView = view.findViewById(R.id.appointmentDate);
        mBtnNext = view.findViewById(R.id.btnNext);
        mDateLayout = view.findViewById(R.id.dateLayout);
        mTimeLayout = view.findViewById(R.id.timeLayout);
        mDoctorName = view.findViewById(R.id.doctorName);
        mSpeciality = view.findViewById(R.id.doctorSpeciality);
        mHospitalName = view.findViewById(R.id.hospitalName);
        recyclerView = view.findViewById(R.id.userRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        calendar = Calendar.getInstance();

        //Recycle view

        userData = new ArrayList<>();
        userAdapter = new UserAdapter(userData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);

        firebaseFirestore.collection("Users").whereEqualTo("UserId",UserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                UserData mUserData = doc.getDocument().toObject(UserData.class);
                                userData.add(mUserData);
                                userAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            DoctorName = bundle.getString("DoctorName");
        }

        firebaseFirestore.collection("Doctors").whereEqualTo("DoctorName",DoctorName)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    List<DocumentSnapshot> list = task.getResult().getDocuments();
                    if (list != null && list.size()>0){
                        for (DocumentSnapshot doc : list){
                            DoctorData doctorData = new DoctorData();
                            String DoctorName = doctorData.setDoctorName(doc.getString("DoctorName"));
                            String Specialist = doctorData.setSpeciality(doc.getString("Speciality"));
                            String HospitalId = doctorData.setHospitalId(doc.getString("HospitalId"));

                            mDoctorName.setText(DoctorName);
                            mSpeciality.setText(Specialist);

                            firebaseFirestore.collection("Hospitals").document(HospitalId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        String HospitalName = task.getResult().getString("HospitalName");
                                        mHospitalName.setText(HospitalName);
                                    }
                                }
                            });
                        }
                    }
                }

            }
        });

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd / MM /yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        textView.setText("Appointment Date :" + formattedDate);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + " / " +month+ " / " +year;
                if (date.equals(null)){
                    Toast.makeText(getContext(), "Please select date", Toast.LENGTH_SHORT).show();
                }else {
                    textView.setText("Appointment Date : " + date);
                    mBtnNext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mTimeLayout.setVisibility(View.VISIBLE);
                            mDateLayout.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        return view;
    }

}