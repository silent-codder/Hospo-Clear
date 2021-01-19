package com.silentcodder.newhospital.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.EditText;
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

public class BookAppointmentFragment extends Fragment {

    CalendarView calendarView;
    TextView mDoctorName,mSpeciality,mHospitalName;
    EditText mProblem;
    Calendar calendar;
    Button mBtnNext;
    RelativeLayout mDateLayout;
    String DoctorId,UserId,date,HospitalName,HospitalId;
    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_appointment, container, false);
        calendarView = view.findViewById(R.id.appDate);
        long dat = calendarView.getDate()+7;
        calendarView.setMinDate(dat);
        mBtnNext = view.findViewById(R.id.btnNext);
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
        calendar = Calendar.getInstance();

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
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problem = mProblem.getText().toString();
                if (TextUtils.isEmpty(problem)){
                    mProblem.setError("Please mention problem");
                }else if (formattedDate == null || date == null){
                    Toast.makeText(getContext(), "Choose appointment date", Toast.LENGTH_SHORT).show();
                }else {
                    Fragment fragment = new SelectUserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Problem",problem);
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