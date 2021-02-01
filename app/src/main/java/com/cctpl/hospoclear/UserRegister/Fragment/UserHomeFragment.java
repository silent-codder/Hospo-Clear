package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.CategoryAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.TopHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.cctpl.hospoclear.UserRegister.Model.MyCategories;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    TopHospitalAdapter topHospitalAdapter;
    List<HospitalData> hospitalData;
    RecyclerView recyclerView1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        recyclerView1 = view.findViewById(R.id.hospitalRecycleView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Recycle view
        hospitalData = new ArrayList<>();
        topHospitalAdapter = new TopHospitalAdapter(hospitalData);

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setAdapter(topHospitalAdapter);

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

        MyCategories[] myCategory = new MyCategories[] {
               new MyCategories("General \nPhysician",R.drawable.stethoscope),
               new MyCategories("Heart \nSpecialist",R.drawable.cardiology),
               new MyCategories("Eye \nSpecialist",R.drawable.eye),
               new MyCategories("Dental \nSurgeon",R.drawable.dental),
               new MyCategories("Ayurveda",R.drawable.leaves)
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.categoryRecycleView);
        CategoryAdapter adapter = new CategoryAdapter(myCategory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);

        return view;
    }
}