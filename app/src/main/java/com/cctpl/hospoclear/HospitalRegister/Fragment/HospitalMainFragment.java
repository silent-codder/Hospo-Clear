package com.cctpl.hospoclear.HospitalRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.HospitalRegister.Adapter.RequestAppointmentAdapter;
import com.cctpl.hospoclear.HospitalRegister.Adapter.TabAdapter;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HospitalMainFragment extends Fragment {

    List<AppointmentData> appointmentData;
    RequestAppointmentAdapter requestAppointmentAdapter;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String HospitalId;
    LottieAnimationView lottieAnimationView;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_main, container, false);
        recyclerView = view.findViewById(R.id.recycleViewTodayAppointments);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        HospitalId = firebaseAuth.getCurrentUser().getUid();
//        textView = view.findViewById(R.id.notFoundText);
//        lottieAnimationView = view.findViewById(R.id.lottie);

        loadData();


        firebaseFirestore.collection("Hospitals").document(HospitalId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String HospitalName = task.getResult().getString("HospitalName");
                            TextView textView = view.findViewById(R.id.hospitalName);
                            textView.setText(HospitalName);
                        }
                    }
                });

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Active \nAppointments"));
        tabLayout.addTab(tabLayout.newTab().setText("Complete \nAppointments"));

        TabAdapter adapter = new TabAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.removeOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return view;
    }

    private void loadData() {
        appointmentData = new ArrayList<>();
        requestAppointmentAdapter = new RequestAppointmentAdapter(appointmentData);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(requestAppointmentAdapter);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("d.M.yyyy");
        String currentDate = dateFormat.format(date);

        Query query = firebaseFirestore.collectionGroup("Appointments")
                .whereEqualTo("HospitalId",HospitalId)
                .whereEqualTo("Status","Request")
                .whereEqualTo("AppointmentDate",currentDate)
                .orderBy("TimeStamp", Query.Direction.ASCENDING);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (!value.isEmpty()){
//                    lottieAnimationView.setVisibility(View.GONE);
//                    textView.setVisibility(View.GONE);
                    for (DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            String AppointmentId = doc.getDocument().getId();
                            AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                            appointmentData.add(mAppointmentData);
                            requestAppointmentAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }
}