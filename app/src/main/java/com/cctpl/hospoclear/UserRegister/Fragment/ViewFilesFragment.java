package com.cctpl.hospoclear.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.ViewFilesAdapter;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;

import java.util.ArrayList;
import java.util.List;

public class ViewFilesFragment extends Fragment {

    private String type,appointmentId,doctorId,flag;
    private List<AppointmentData> appointmentData;
    private ViewFilesAdapter viewFilesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private TextView textView;
    private ImageView imageView;
    private Button mBtnAttachFile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_files, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        imageView = view.findViewById(R.id.search);
        mBtnAttachFile = view.findViewById(R.id.btnAttachFile);
        textView = view.findViewById(R.id.notFoundText);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            type = bundle.getString("Type");
            appointmentId = bundle.getString("AppointmentId");
            doctorId = bundle.getString("DoctorId");
            flag = bundle.getString("Flag");
            TextView textView1 = view.findViewById(R.id.text);
            textView1.setText(type);
        }
        mBtnAttachFile.setVisibility(View.VISIBLE);
        mBtnAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AttachFileFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Location",type);
                bundle1.putString("AppointmentId", appointmentId);
                bundle1.putString("DoctorId",doctorId);
                bundle1.putString("Flag",flag);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
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