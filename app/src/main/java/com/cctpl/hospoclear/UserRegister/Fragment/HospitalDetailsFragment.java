package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.AppointmentAdapter;
import com.cctpl.hospoclear.UserRegister.Adapter.HospitalImageAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.cctpl.hospoclear.UserRegister.UserLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HospitalDetailsFragment extends Fragment {

    List<HospitalData> hospitalData;
    HospitalImageAdapter hospitalImageAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId,HospitalId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_details, container, false);


        hospitalData = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            HospitalId = bundle.getString("HospitalId");
        }

        firebaseFirestore.collection("Hospitals").document(HospitalId)
                .collection("Images")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()){
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        HospitalData mHospitalData = doc.getDocument().toObject(HospitalData.class);
                        hospitalData.add(mHospitalData);
                        hospitalImageAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // initializing the slider view.
        SliderView sliderView = view.findViewById(R.id.slider);
        hospitalImageAdapter = new HospitalImageAdapter(hospitalData);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(hospitalImageAdapter);
        sliderView.setScrollTimeInSec(2);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        return view;
    }
}