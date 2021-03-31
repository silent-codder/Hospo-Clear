package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.UserRegister.Adapter.TopHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.TopDoctorAdapter;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.cctpl.hospoclear.UserRegister.Model.DoctorId;

import java.util.ArrayList;
import java.util.List;

public class CategoryDoctorFragment extends Fragment {

    String Category;
    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    TopDoctorAdapter topDoctorAdapter;
    EditText mSearch;
    List<DoctorData> doctorData;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_category_doctor, container, false);
        recyclerView = view.findViewById(R.id.categoryDoctorRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = view.findViewById(R.id.loader);
        mSearch = view.findViewById(R.id.searchBar);
        progressBar.setVisibility(View.VISIBLE);
         Bundle bundle = this.getArguments();
         if (bundle != null){
             Category = bundle.getString("Category");
             TextView textView = view.findViewById(R.id.categoryName);
             textView.setText(Category);
         }

         mSearch.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(s.toString().trim().length()<1)
                 {
                     clear();
                 }
                 else {
                     loadData(s.toString());
                 }
             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });

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

    private void loadData(String toUpperCase) {
        doctorData = new ArrayList<>();
        topDoctorAdapter = new TopDoctorAdapter(doctorData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(topDoctorAdapter);

        CollectionReference ref = firebaseFirestore.collection("Doctors");

        Query query = ref.orderBy("DoctorName").startAfter("Dr. "+toUpperCase).endAt("Dr. "+toUpperCase+ "\uf9ff");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        String DoctorId = doc.getDocument().getId();
                        DoctorData mDoctorData = doc.getDocument().toObject(DoctorData.class).withId(DoctorId);
                        doctorData.add(mDoctorData);
                        topDoctorAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        LottieAnimationView lottieAnimationView = getView().findViewById(R.id.lottie);
                        TextView textView = getView().findViewById(R.id.notFoundText);
                        lottieAnimationView.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    public void clear() {
        int size = doctorData.size();
        doctorData.clear();
        topDoctorAdapter.notifyItemRangeRemoved(0,size);
    }
}