package com.cctpl.hospoclear.UserRegister.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.APIService;
import com.cctpl.hospoclear.Client;
import com.cctpl.hospoclear.Data;
import com.cctpl.hospoclear.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.UserRegister.Adapter.UserAdapter;
import com.cctpl.hospoclear.UserRegister.Model.DoctorId;
import com.cctpl.hospoclear.UserRegister.Model.UserData;
import com.cctpl.hospoclear.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectUserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String UserId,Problem,AppointmentDate,DoctorId,HospitalId,UserName,Token;
    TextView mProblem,mAppointmentDate,mPatientName;

    RelativeLayout mNameLayout,mAnotherPatient;
    EditText mAnotherPatientName,mRelation;
    ProgressDialog progressDialog;
    String fcmUrl = "https://fcm.googleapis.com/";

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
        progressDialog = new ProgressDialog(getContext());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();


        Bundle bundle = this.getArguments();
        if (bundle!=null){
            Problem = bundle.getString("Problem");
            AppointmentDate = bundle.getString("Date");
            DoctorId = bundle.getString("DoctorId");
            HospitalId = bundle.getString("HospitalId");
            mAppointmentDate.setText(AppointmentDate);
            mProblem.setText(Problem);
        }

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            UserName = task.getResult().getString("UserName");
                            mPatientName.setText(UserName);
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

                        progressDialog.setMessage("Loading..");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        firebaseFirestore.collection("Tokens").document(HospitalId).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Token = task.getResult().getString("token");
                                            sendNotification(Token,UserName,Problem);
                                        }
                                    }
                                });

                        HashMap<String,Object> map = new HashMap<>();
                        map.put("PatientName",UserName);
                        map.put("AppointmentDate",AppointmentDate);
                        map.put("Problem",Problem);
                        map.put("TimeStamp",System.currentTimeMillis());
                        map.put("UserId",UserId);
                        map.put("Status","3");
                        //pending status code 2
                        //complete status code 1
                        //request status code 3
                        map.put("Relationship","My Self");
                        map.put("HospitalId",HospitalId);
                        map.put("DoctorId",DoctorId);

                        firebaseFirestore.collection("Appointments").add(map)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Requesting appointment", Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new UserAppointmentsFragment();
                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
                        String PatientName = mAnotherPatientName.getText().toString();
                        String Relationship = mRelation.getText().toString();

                        if (TextUtils.isEmpty(PatientName)){
                            mAnotherPatientName.setError("Patient Name");
                        }else if (TextUtils.isEmpty(Relationship)){
                            mRelation.setError("Relationship with you");
                        }else{

                            progressDialog.setMessage("Loading..");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            firebaseFirestore.collection("Tokens").document(HospitalId).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                Token = task.getResult().getString("token");
                                                sendNotification(Token,UserName,Problem);
                                            }
                                        }
                                    });

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("PatientName",PatientName);
                            map.put("AppointmentDate",AppointmentDate);
                            map.put("Problem",Problem);
                            map.put("Status","3");
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("UserId",UserId);
                            map.put("Relationship",Relationship);
                            map.put("HospitalId",HospitalId);
                            map.put("DoctorId",DoctorId);
                            firebaseFirestore.collection("Appointments").add(map)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(getContext(), "Requesting appointment", Toast.LENGTH_SHORT).show();
                                                Fragment fragment = new UserAppointmentsFragment();
                                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
        return view;
    }

    private void sendNotification(String token, String title, String msg) {
        Data data = new Data(title,msg);
        NotificationSender notificationSender = new NotificationSender(data,token);

        APIService apiService = Client.getRetrofit(fcmUrl).create(APIService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}