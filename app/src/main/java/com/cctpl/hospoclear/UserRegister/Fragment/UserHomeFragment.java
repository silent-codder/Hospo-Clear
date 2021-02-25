package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cctpl.hospoclear.UserRegister.Adapter.BookmarkHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.TodayAppointmentAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.UserRegister.Model.BookmarkHospitalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.CategoryAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.TopHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.cctpl.hospoclear.UserRegister.Model.MyCategories;
import com.google.gson.internal.$Gson$Preconditions;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class UserHomeFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    TopHospitalAdapter topHospitalAdapter;
    List<HospitalData> hospitalData;
    RecyclerView recyclerView;
    String mLocation;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        recyclerView = view.findViewById(R.id.hospitalRecycleView2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String UserId = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore.collection("Users").document(UserId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String UserName = task.getResult().getString("UserName");
                    TextView Name = view.findViewById(R.id.userName);
                    Name.setText(UserName);
                }
            }
        });

        Spinner Location = view.findViewById(R.id.locationSpinner);
        List<String> subjects = new ArrayList<>();
        Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLocation = subjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subjects);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Location.setAdapter(Adapter);


        firebaseFirestore.collection("Locations").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("City");
                        subjects.add(subject);
                    }
                    Adapter.notifyDataSetChanged();
                }
            }
        });


        //Recycle view
        hospitalData = new ArrayList<>();
        topHospitalAdapter = new TopHospitalAdapter(hospitalData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(topHospitalAdapter);

        CollectionReference ref = firebaseFirestore.collection("Hospitals");

        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        HospitalData mHospitalData = doc.getDocument().toObject(HospitalData.class);
                        hospitalData.add(mHospitalData);
                        topHospitalAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        //today appointment recycleView

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.M.yyyy");
        String currentDate = dateFormat.format(date);


        RecyclerView mTodayRecycleView = view.findViewById(R.id.todayAppointmentRecycleView);
        List<AppointmentData> appointmentData = new ArrayList<>();
        TodayAppointmentAdapter todayAppointmentAdapter = new TodayAppointmentAdapter(appointmentData);

        mTodayRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodayRecycleView.setAdapter(todayAppointmentAdapter);


        Query query = firebaseFirestore.collection("Appointments")
                .whereEqualTo("AppointmentDate",currentDate);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (!value.isEmpty()){
                    RelativeLayout relativeLayout = view.findViewById(R.id.todayAppointment);
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class);
                        appointmentData.add(mAppointmentData);
                        todayAppointmentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        MyCategories[] myCategory = new MyCategories[] {
               new MyCategories("General \nPhysician",R.drawable.stethoscope),
               new MyCategories("Heart \nSpecialist",R.drawable.cardiology),
               new MyCategories("Eye \nSpecialist",R.drawable.eye),
               new MyCategories("Dental \nSurgeon",R.drawable.dental),
               new MyCategories("Ayurveda",R.drawable.leaves),
               new MyCategories("Neurologist",R.drawable.neurologist)
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.categoryRecycleView);
        CategoryAdapter adapter = new CategoryAdapter(myCategory);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);

        TextView BtnViewMore = view.findViewById(R.id.btnViewMore);
        BtnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MoreCategoryFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        RelativeLayout BtnDoctor = view.findViewById(R.id.doctor);
        RelativeLayout BtnHospital = view.findViewById(R.id.hospital);
        RelativeLayout BtnMedicine = view.findViewById(R.id.medicine);
        RelativeLayout BtnAppointment = view.findViewById(R.id.appointment);

        BtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BookmarkDoctorFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        BtnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BookMarkHospitalFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        BtnMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(view,"Coming soon..",Snackbar.LENGTH_SHORT).show();
            }
        });

        BtnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserAppointmentsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}