package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.ViewFilesAdapter;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;

import java.util.ArrayList;
import java.util.List;

public class ViewFilesFragment extends Fragment {

    private String type,appointmentId;
    private List<AppointmentData> appointmentData;
    private ViewFilesAdapter viewFilesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private TextView textView;
    private ImageView imageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_files, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recycleView);
        imageView = view.findViewById(R.id.search);
        textView = view.findViewById(R.id.notFoundText);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            type = bundle.getString("Type");
            appointmentId = bundle.getString("AppointmentId");
            TextView textView1 = view.findViewById(R.id.text);
            textView1.setText(type);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        loadData();

        return view;
    }

    private void refreshData() {
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        appointmentData = new ArrayList<>();
        viewFilesAdapter = new ViewFilesAdapter(appointmentData);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(viewFilesAdapter);

        firebaseFirestore.collection("Appointments").document(appointmentId).collection(type)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value.isEmpty()){
                            imageView.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                        }
                        for (DocumentChange doc : value.getDocumentChanges()){
                            if (doc.getType() == DocumentChange.Type.ADDED){
                                String AppointmentId = doc.getDocument().getId();
                                AppointmentData mAppointmentData = doc.getDocument().toObject(AppointmentData.class).withId(AppointmentId);
                                appointmentData.add(mAppointmentData);
                                viewFilesAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}