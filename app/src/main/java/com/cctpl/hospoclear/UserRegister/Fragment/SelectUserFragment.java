package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.Notification.APIService;
import com.cctpl.hospoclear.Notification.Client;
import com.cctpl.hospoclear.Notification.Data;
import com.cctpl.hospoclear.Notification.NotificationSender;
import com.cctpl.hospoclear.Notification.Remainder;
import com.cctpl.hospoclear.UserRegister.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class SelectUserFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String UserId;
    String Problem;
    String AppointmentDate;
    String DoctorId;
    String HospitalId;
    String UserName;
    String Token;
    String timeSlot;
    String SlotId;
    String Section;
    String AppointmentTimeStamp;
    String Hr;
    int Hours;
    long TimeInMilliSec;
    long TimeStamp;
    TextView mAppointmentDate,mAppointmentTime,mDoctorFee,mPatientName;

    RelativeLayout mNameLayout,mAnotherPatient;
    EditText mAnotherPatientName,mRelation,mProblem,mProblem1;
    ProgressDialog progressDialog;
    String fcmUrl = "https://fcm.googleapis.com/";
    SharedPreferences sharedPreferences;
    Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_user, container, false);

        mProblem = view.findViewById(R.id.problem);
        mProblem1 = view.findViewById(R.id.problem1);
        mAppointmentDate = view.findViewById(R.id.appointmentDate);
        mAppointmentTime = view.findViewById(R.id.appointmentTime);
        mDoctorFee = view.findViewById(R.id.doctorFee);
        mAnotherPatient = view.findViewById(R.id.anotherPatientLayout);
        mPatientName = view.findViewById(R.id.patientName);
        mAnotherPatientName = view.findViewById(R.id.anotherPatientName);
        mRelation = view.findViewById(R.id.relationship);
        mNameLayout = view.findViewById(R.id.youLayout);
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

        sharedPreferences = getContext().getSharedPreferences("AppointmentData",0);
        editor = sharedPreferences.edit();
        timeSlot = sharedPreferences.getString("TimeSlot",null);
        SlotId = sharedPreferences.getString("SlotId",null);
        Section = sharedPreferences.getString("Section",null);
        AppointmentTimeStamp = sharedPreferences.getString("AppointmentTimeStamp",null);
        Hr = sharedPreferences.getString("Hr",null);
        AppointmentDate = sharedPreferences.getString("AppointmentDate",null);
        DoctorId = sharedPreferences.getString("DoctorId",null);
        mAppointmentDate.setText(AppointmentDate);
        mAppointmentTime.setText(timeSlot);

        Hours = Integer.parseInt(Hr);
        TimeStamp = Long.parseLong(AppointmentTimeStamp);
        TimeInMilliSec = Hours*3600000;
        TimeStamp += TimeInMilliSec;
        Log.d(TAG, "TimeStamp: " + AppointmentTimeStamp);
        Log.d(TAG, "MilliSec: " + TimeInMilliSec);
        Log.d(TAG, "Total: " + TimeStamp);

        firebaseFirestore.collection("Doctors").document(DoctorId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        HospitalId = task.getResult().getString("HospitalId");
                        String DoctorName = task.getResult().getString("DoctorName");
                        String Speciality = task.getResult().getString("Speciality");
                        String Experience = task.getResult().getString("Experience");
                        TextView textView = view.findViewById(R.id.doctorName);
                        TextView textView1 = view.findViewById(R.id.doctorSpeciality);
                        TextView textView2 = view.findViewById(R.id.doctorExperience);
                        textView.setText(DoctorName);
                        textView1.setText(Speciality);
                        textView2.setText("+" + Experience);
                    }
                });

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

                Button btn = view.findViewById(R.id.btnBookAppointment);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String problem = mProblem1.getText().toString();

                        if (TextUtils.isEmpty(problem)){
                            mProblem1.setError("Mention problem");
                        }else {
                            progressDialog.setMessage("Loading..");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            firebaseFirestore.collection("Tokens").document(HospitalId).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                Token = task.getResult().getString("token");
                                                String extra = " has requested for appointment.";
                                                String Data = AppointmentDate + " , " + timeSlot;
                                                sendNotification(Token,UserName + extra , Data);
                                            }
                                        }
                                    });

                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("Flag","true");

                            firebaseFirestore.collection("Doctors").document(DoctorId)
                                    .collection(Section).document(SlotId).update(map2);

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("PatientName",UserName);
                            map.put("AppointmentDate",AppointmentDate);
                            map.put("AppointmentTime",timeSlot);
                            map.put("Problem",problem);
                            map.put("SlotId",SlotId);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("UserId",UserId);
                            map.put("Status","Request");
                            map.put("AppointmentTimeStamp",AppointmentTimeStamp);
                            map.put("Section",Section);
                            //pending status code 2
                            //complete status code 1
                            //request status code 3
                            map.put("Relationship","My Self");
                            map.put("HospitalId",HospitalId);
                            map.put("DoctorId",DoctorId);

                            HashMap<String, Object> AppointmentStatus = new HashMap<>();
                            AppointmentStatus.put("DoctorId",DoctorId);
                            AppointmentStatus.put("UserId",UserId);
                            AppointmentStatus.put("TimeStamp",System.currentTimeMillis());
                            AppointmentStatus.put("Description","Requested for Appointment to");

                            AddData(map,AppointmentStatus);
                        }
                    }
                });
            }
        });

        radioButtonSomeone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnotherPatient.setVisibility(View.VISIBLE);
                mNameLayout.setVisibility(View.INVISIBLE);
                Button btn = view.findViewById(R.id.btnBookAppointment);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String PatientName = mAnotherPatientName.getText().toString();
                        String Relationship = mRelation.getText().toString();
                        String Problem = mProblem.getText().toString();

                        if (TextUtils.isEmpty(PatientName)){
                            mAnotherPatientName.setError("Patient Name");
                        }else if (TextUtils.isEmpty(Relationship)){
                            mRelation.setError("Relationship with you");
                        }else if (TextUtils.isEmpty(Problem)){
                            mProblem.setError("Mention problem");
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
                                                String extra = " has requested.";
                                                String Data = AppointmentDate + " , " + timeSlot;
                                                sendNotification(Token,UserName + extra , Data);
                                            }
                                        }
                                    });

                            HashMap<String, Object> map2 = new HashMap<>();
                            map2.put("Flag","true");

                            firebaseFirestore.collection("Doctors").document(DoctorId)
                                    .collection(Section).document(SlotId).update(map2);

                            HashMap<String,Object> map = new HashMap<>();
                            map.put("PatientName",PatientName);
                            map.put("AppointmentDate",AppointmentDate);
                            map.put("AppointmentTime",timeSlot);
                            map.put("Problem",Problem);
                            map.put("Status","Request");
                            map.put("Section",Section);
                            map.put("SlotId",SlotId);
                            map.put("AppointmentTimeStamp",AppointmentTimeStamp);
                            map.put("TimeStamp",System.currentTimeMillis());
                            map.put("UserId",UserId);
                            map.put("Relationship",Relationship);
                            map.put("HospitalId",HospitalId);
                            map.put("DoctorId",DoctorId);

                            HashMap<String, Object> AppointmentStatus = new HashMap<>();
                            AppointmentStatus.put("DoctorId",DoctorId);
                            AppointmentStatus.put("UserId",UserId);
                            AppointmentStatus.put("TimeStamp",System.currentTimeMillis());
                            AppointmentStatus.put("Description","Requested for Appointment to");

                            AddData(map,AppointmentStatus);
                        }
                    }
                });
            }
        });
        return view;
    }

    private void AddData(HashMap<String, Object> map,HashMap<String, Object> AppointmentStatus) {

        setRemainder();

        String id = firebaseFirestore.collection("Appointments").document().getId();
        Log.d(TAG, "FireStore ID: " + id);



        firebaseFirestore.collection("Appointments").document(id).collection("Status").add(AppointmentStatus);
        firebaseFirestore.collection("Appointments")
                .document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.get_appointment_dialog);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            Button button = dialog.findViewById(R.id.btnGotIt);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    editor.clear();
                                    editor.commit();
                                    Fragment fragment = new UserHomeFragment();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                                    dialog.dismiss();
                                }
                            });
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

    private void setRemainder() {
        Intent intent = new Intent(getContext(), Remainder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeStamp, pendingIntent);
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