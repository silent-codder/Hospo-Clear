package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.HospitalRegister.HospitalLogin;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.TopHospitalAdapter;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MoreHospitalFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    TopHospitalAdapter topHospitalAdapter;
    List<HospitalData> hospitalData;
    RecyclerView recyclerView;
    EditText searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_hospital, container, false);

        recyclerView = view.findViewById(R.id.hospitalRecycleView2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        searchView = view.findViewById(R.id.search);

        searchView.addTextChangedListener(new TextWatcher() {
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

        //Recycle view
        hospitalData = new ArrayList<>();
        topHospitalAdapter = new TopHospitalAdapter(hospitalData);

//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), topHospitalAdapter.getItemCount());
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

        return view;
    }

    private void loadData(String toUpperCase) {
        hospitalData = new ArrayList<>();
        topHospitalAdapter = new TopHospitalAdapter(hospitalData);

//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.HORIZONTAL,false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(), topHospitalAdapter.getItemCount());
        recyclerView.setAdapter(topHospitalAdapter);

        Query query = FirebaseFirestore.getInstance().collection("Hospitals").orderBy("HospitalName").startAt(toUpperCase ).endAt(toUpperCase+"\uf9ff" );

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    TextView textView = getView().findViewById(R.id.notFoundText);
                    textView.setVisibility(View.VISIBLE);
                }else {
                    for (DocumentChange doc : value.getDocumentChanges()){
                        if (doc.getType() == DocumentChange.Type.ADDED){
                            HospitalData mHospitalData = doc.getDocument().toObject(HospitalData.class);
                            hospitalData.add(mHospitalData);
                            topHospitalAdapter.notifyDataSetChanged();
                            TextView textView = getView().findViewById(R.id.notFoundText);
                            textView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }


    public void clear() {
        int size = hospitalData.size();
        hospitalData.clear();
        topHospitalAdapter.notifyItemRangeRemoved(0,size);
    }
}