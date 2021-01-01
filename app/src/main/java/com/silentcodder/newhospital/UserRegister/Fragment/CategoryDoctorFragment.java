package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.TopDoctorAdapter;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.silentcodder.newhospital.UserRegister.Model.DoctorId;

import java.util.ArrayList;
import java.util.List;

public class CategoryDoctorFragment extends Fragment {

    String Category;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    TopDoctorAdapter topDoctorAdapter;
    List<DoctorData> doctorData;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category_doctor, container, false);
        recyclerView = view.findViewById(R.id.categoryDoctorRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.loader);
        progressBar.setVisibility(View.VISIBLE);
         Bundle bundle = this.getArguments();
         if (bundle != null){
             Category = bundle.getString("Category");
             TextView textView = view.findViewById(R.id.categoryName);
             textView.setText(Category);
         }

        doctorData = new ArrayList<>();
        topDoctorAdapter = new TopDoctorAdapter(doctorData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(topDoctorAdapter);

        CollectionReference ref = firebaseFirestore.collection("Doctors");

        Query query = ref.whereEqualTo("Speciality",Category);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value.isEmpty()){
                    LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie);
                    TextView textView = view.findViewById(R.id.notFoundText);
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }

                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String DoctorId = doc.getDocument().getId();
                        DoctorData mDoctorData = doc.getDocument().toObject(DoctorData.class).withId(DoctorId);
                        doctorData.add(mDoctorData);
                        topDoctorAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        return view;
    }
}