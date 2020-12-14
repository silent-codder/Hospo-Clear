package com.silentcodder.newhospital.UserRegister.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.UserAdapter;
import com.silentcodder.newhospital.UserRegister.Model.UserData;

import java.util.ArrayList;
import java.util.List;

public class SelectUserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String UserId,Problem,AppointmentDate;
    TextView mProblem,mAppointmentDate;

    RelativeLayout mNameLayout,mAnotherPatient;
    EditText mPatientName,mAnotherPatientName,mRelation;
    Button mBtnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_user, container, false);

        mProblem = view.findViewById(R.id.problem);
        mAppointmentDate = view.findViewById(R.id.appointmentDate);
        mAnotherPatient = view.findViewById(R.id.anotherPatient);
        mPatientName = view.findViewById(R.id.patientName);
        mAnotherPatientName = view.findViewById(R.id.anotherPatientName);
        mRelation = view.findViewById(R.id.relation);
        mNameLayout = view.findViewById(R.id.name_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();


        Bundle bundle = this.getArguments();
        if (bundle!=null){
            Problem = bundle.getString("Problem");
            AppointmentDate = bundle.getString("Date");

            mAppointmentDate.setText(AppointmentDate);
            mProblem.setText(Problem);
        }

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String Name = task.getResult().getString("UserName");
                            mPatientName.setText(Name);
                    }
                });

        RadioButton radioButtonYou = view.findViewById(R.id.you);
        RadioButton radioButtonSomeone = view.findViewById(R.id.someoneElse);

        radioButtonYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameLayout.setVisibility(View.VISIBLE);
                mAnotherPatient.setVisibility(View.INVISIBLE);

                Button btn = view.findViewById(R.id.btnSubmit);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "You", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        radioButtonSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnotherPatient.setVisibility(View.VISIBLE);
                mNameLayout.setVisibility(View.INVISIBLE);
                Button btn = view.findViewById(R.id.btnSubmit);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Some One", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}