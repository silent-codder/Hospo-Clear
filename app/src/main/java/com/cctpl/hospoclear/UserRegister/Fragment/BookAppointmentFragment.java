package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.UserRegister.Adapter.SelectEveningTimeSlotAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.SelectTimeSlotAdapter;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static android.content.ContentValues.TAG;

public class BookAppointmentFragment extends Fragment {

    TextView mDoctorName,mSpeciality,mExperience;
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
        mDoctorName = view.findViewById(R.id.doctorName);
        mSpeciality = view.findViewById(R.id.doctorSpeciality);
        mExperience = view.findViewById(R.id.doctorExperience);
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
        }

        firebaseFirestore.collection("Doctors").document(DoctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String DoctorName = task.getResult().getString("DoctorName");
                            String Specialist = task.getResult().getString("Speciality");
                            String Experience = task.getResult().getString("Experience");
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            HospitalId = task.getResult().getString("HospitalId");

                            if (!TextUtils.isEmpty(ProfileUrl)){
                                CircleImageView circleImageView = view.findViewById(R.id.doctorImg);
                                Picasso.get().load(ProfileUrl).into(circleImageView);
                            }

                            mDoctorName.setText(DoctorName);
                            mSpeciality.setText(Specialist);
                            mExperience.setText("+"+Experience);
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
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                date = day + "." + (month+1) + "." + year;

                long timeStamp = calendar.getTimeInMillis();
                Log.d(TAG, "onDateSelected: " + timeStamp );

                TextView textView = view.findViewById(R.id.appointmentDate);
                textView.setText("(" + date + ")");
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("AppointmentData",0);
                Editor editor = sharedPreferences.edit();
                editor.putString("AppointmentDate",date);
                editor.putString("AppointmentTimeStamp", String.valueOf(timeStamp));
                editor.commit();

            }
        });

        CardView btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(date)){
                    Fragment fragment = new SelectTimeSlotFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Date",date);
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