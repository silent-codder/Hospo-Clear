package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.TopDoctorAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.TopHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopHospitalDoctorFragment extends Fragment {

    RecyclerView recyclerView;
    List<DoctorData> doctorData;
    TopDoctorAdapter topDoctorAdapter;
    FirebaseFirestore firebaseFirestore;
    String HospitalId;

    TextView mHospitalName;
    CircleImageView mHospitalImg,mHospital;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_hospital_doctor, container, false);

        recyclerView = view.findViewById(R.id.topDoctorRecycleView);
        mHospitalName = view.findViewById(R.id.hospitalName);
        mHospitalImg = view.findViewById(R.id.hospitalImg);
        mHospital = view.findViewById(R.id.hospital);
        progressBar = view.findViewById(R.id.loader);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            HospitalId = bundle.getString("HospitalId");
        }

        getHospitalInfo(HospitalId);

        doctorData = new ArrayList<>();
        topDoctorAdapter = new TopDoctorAdapter(doctorData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(topDoctorAdapter);

        CollectionReference ref = firebaseFirestore.collection("Doctors");

        Query query = ref.whereEqualTo("HospitalId",HospitalId);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String DoctorId = doc.getDocument().getId();
                        DoctorData mDoctorData = doc.getDocument().toObject(DoctorData.class).withId(DoctorId);
                        doctorData.add(mDoctorData);
                        topDoctorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        CardView button = view.findViewById(R.id.btnViewDetails);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("HospitalId",HospitalId);
                Fragment fragment = new HospitalDetailsFragment();
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void getHospitalInfo(String hospitalId) {
        firebaseFirestore.collection("Hospitals").document(hospitalId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String HospitalName = task.getResult().getString("HospitalName");
                            String City = task.getResult().getString("City");
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            mHospitalName.setText(HospitalName + ", " +City);
                            mHospitalName.setSelected(true);
                            mHospitalName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            mHospitalName.setSingleLine(true);

                            if (!TextUtils.isEmpty(ProfileUrl)){
                                mHospital.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                mHospitalImg.setVisibility(View.VISIBLE);
                                Picasso.get().load(ProfileUrl).into(mHospitalImg);
                            }else {
                                mHospital.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}