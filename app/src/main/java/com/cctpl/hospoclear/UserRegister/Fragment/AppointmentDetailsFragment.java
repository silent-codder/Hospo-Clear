package com.cctpl.hospoclear.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cctpl.hospoclear.Notification.APIService;
import com.cctpl.hospoclear.Notification.Client;
import com.cctpl.hospoclear.Notification.Data;
import com.cctpl.hospoclear.Notification.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.HospitalRegister.Fragment.BillFragment;
import com.cctpl.hospoclear.HospitalRegister.Fragment.BillViewFragment;
import com.cctpl.hospoclear.R;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetailsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private String AppointmentId,HospitalName,Status;
    String fcmUrl = "https://fcm.googleapis.com/";

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle!=null){
            String UserName = bundle.getString("UserName");
            String Date = bundle.getString("AppointmentDate");
            String Time = bundle.getString("AppointmentTime");
            String Problem = bundle.getString("Problem");
            Status = bundle.getString("Status");
            String DoctorId = bundle.getString("DoctorId");
            String HospitalId = bundle.getString("HospitalId");
            String ProfileUrl = bundle.getString("ProfileUrl");
            AppointmentId = bundle.getString("AppointmentId");


            TextView userName = view.findViewById(R.id.patientName);
            TextView date = view.findViewById(R.id.appointmentInfo);
            TextView problem = view.findViewById(R.id.problem);
            CircleImageView profile = view.findViewById(R.id.patientImg);

            userName.setText(UserName);
            date.setText(Date + ", " + Time );
            problem.setText(Problem);
            Picasso.get().load(ProfileUrl).into(profile);


            firebaseFirestore.collection("Doctors").document(DoctorId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String DoctorName = task.getResult().getString("DoctorName");
                                    String Profile = task.getResult().getString("ProfileImgUrl");
                                    TextView doctorName = view.findViewById(R.id.doctorName);
                                    CircleImageView DoctorImg = view.findViewById(R.id.doctorImg);
                                    doctorName.setText(DoctorName);
                                    Picasso.get().load(Profile).into(DoctorImg);
                                }
                            }
                        });
                firebaseFirestore.collection("Hospitals").document(HospitalId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    HospitalName = task.getResult().getString("HospitalName");
                                    String City = task.getResult().getString("City");
                                    String HospitalImg = task.getResult().getString("ProfileImgUrl");
                                    TextView hospitalName = view.findViewById(R.id.hospitalName);
                                    CircleImageView hospitalImg = view.findViewById(R.id.hospitalImg);
                                    hospitalName.setText(HospitalName + " ," + City);
                                    Picasso.get().load(HospitalImg).into(hospitalImg);
                                }
                            }
                        });
        }


        TextView btnReportFile = view.findViewById(R.id.btnReportFile);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Report")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int files = value.size();
                            btnReportFile.setText("Report File " + "(" + files + ")");
                        }
                    }
                });

        btnReportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewFilesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","Report");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        TextView btnXRayFile = view.findViewById(R.id.btnXRayFile);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("XRay")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int files = value.size();
                            btnXRayFile.setText("X-ray File " + "(" + files + ")");
                        }
                    }
                });
        btnXRayFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewFilesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","XRay");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        TextView btnLabReport = view.findViewById(R.id.btnLabReport);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("LabReport")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int files = value.size();
                            btnLabReport.setText("Lab Reports " + "(" + files + ")");
                        }
                    }
                });
        btnLabReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewFilesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","LabReport");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        TextView btnPrescription = view.findViewById(R.id.btnPrescription);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Prescription")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int files = value.size();
                            btnPrescription.setText("Prescription " + "(" + files + ")");
                        }
                    }
                });
        btnPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewFilesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","Prescription");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        TextView btnOther = view.findViewById(R.id.btnOther);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Other")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (!value.isEmpty()){
                            int files = value.size();
                            btnOther.setText("Other File " + "(" + files + ")");
                        }
                    }
                });
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewFilesFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","Other");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        TextView btnBill = view.findViewById(R.id.btnBill);
        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Bill")
                .document(AppointmentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String totalBill = task.getResult().getString("TotalBill");
                            btnBill.setText("Bill : " + totalBill);
                        }
                    }
                });
        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BillViewFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("Type","Bill");
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        Button btnCreateBill = view.findViewById(R.id.btnAddBill);
        btnCreateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BillFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("AppointmentId", AppointmentId);
                fragment.setArguments(bundle1);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        return view;
    }

    private void sendNotification(String token, String title, String hospitalName) {
        Data data = new Data(title,hospitalName);
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