package com.silentcodder.newhospital.UserRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.HospitalRegister.Fragment.BillFragment;
import com.silentcodder.newhospital.HospitalRegister.Fragment.BillViewFragment;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentDetailsFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    private String AppointmentId,Status;
    private Button btnCompleteAppointment;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_details, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        btnCompleteAppointment = view.findViewById(R.id.btnCompleteAppointment);


        Bundle bundle = this.getArguments();
        if (bundle!=null){
            String UserName = bundle.getString("UserName");
            String Date = bundle.getString("AppointmentDate");
            Status = bundle.getString("Status");
            String DoctorId = bundle.getString("DoctorId");
            String HospitalId = bundle.getString("HospitalId");
            String ProfileUrl = bundle.getString("ProfileUrl");
            AppointmentId = bundle.getString("AppointmentId");

            TextView userName = view.findViewById(R.id.userName);
            TextView date = view.findViewById(R.id.appointmentDate);
            TextView status = view.findViewById(R.id.status);
            CircleImageView profile = view.findViewById(R.id.userImg);
            CircleImageView request = view.findViewById(R.id.requestImg);
            CircleImageView pending = view.findViewById(R.id.pendingImg);
            CircleImageView complete = view.findViewById(R.id.completeImg);

            userName.setText(UserName);
            date.setText(Date);
            Picasso.get().load(ProfileUrl).into(profile);
            if (Status.equals("1")){
                    complete.setVisibility(View.VISIBLE);
                    status.setText("Appointment was complete");
                    status.setTextColor(Color.GREEN);
                    request.setVisibility(View.INVISIBLE);
                    pending.setVisibility(View.INVISIBLE);
                }else if(Status.equals("2") || Status.equals("4")){
                    complete.setVisibility(View.INVISIBLE);
                    request.setVisibility(View.INVISIBLE);
                    pending.setVisibility(View.VISIBLE);
                    status.setTextColor(Color.MAGENTA);
                    status.setText("Appointment is pending");
                }else if(Status.equals("3")){
                    complete.setVisibility(View.INVISIBLE);
                    request.setVisibility(View.VISIBLE);
                    pending.setVisibility(View.INVISIBLE);
                    status.setTextColor(Color.RED);
                    status.setText("Requesting Appointment");
                }

            firebaseFirestore.collection("Doctors").document(DoctorId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String DoctorName = task.getResult().getString("DoctorName");
                                    TextView doctorName = view.findViewById(R.id.doctorName);
                                    doctorName.setText(DoctorName);
                                }
                            }
                        });
                firebaseFirestore.collection("Hospitals").document(HospitalId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String HospitalName = task.getResult().getString("HospitalName");
                                    TextView hospitalName = view.findViewById(R.id.hospitalName);
                                    hospitalName.setText(HospitalName);
                                }
                            }
                        });
        }

        firebaseFirestore.collection("Appointments").document(AppointmentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String Status = task.getResult().getString("Status");
                            if (Status.equals("1")){
                                btnCompleteAppointment.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });


        //for complete Appointment
        if (Status.equals("4")){
            btnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Status","1");

                    firebaseFirestore.collection("Appointments").document(AppointmentId)
                            .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Complete Appointment", Toast.LENGTH_SHORT).show();
                                btnCompleteAppointment.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }else {
            btnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("Status","4");

                    firebaseFirestore.collection("Appointments").document(AppointmentId)
                            .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Request to Complete Appointment", Toast.LENGTH_SHORT).show();
                                btnCompleteAppointment.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            });
        }

        Button btnAttachFile = view.findViewById(R.id.btnAttachFile);
        btnAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.select_attach_file_type);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                RadioButton radioButtonReport = dialog.findViewById(R.id.report);
                radioButtonReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new AttachFileFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Location","Report");
                        bundle1.putString("AppointmentId", AppointmentId);
                        bundle1.putString("Flag","2");
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                });

                RadioButton radioButtonXRay = dialog.findViewById(R.id.xRay);
                radioButtonXRay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new AttachFileFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Location","XRay");
                        bundle1.putString("AppointmentId", AppointmentId);
                        bundle1.putString("Flag","2");
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                });

                RadioButton radioButtonLabReport = dialog.findViewById(R.id.labReport);
                radioButtonLabReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new AttachFileFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Location","LabReport");
                        bundle1.putString("AppointmentId", AppointmentId);
                        bundle1.putString("Flag","2");
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                });

                RadioButton radioButtonPrescription = dialog.findViewById(R.id.prescription);
                radioButtonPrescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new AttachFileFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Location","Prescription");
                        bundle1.putString("AppointmentId", AppointmentId);
                        bundle1.putString("Flag","1");
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                });

                RadioButton radioButtonOther = dialog.findViewById(R.id.other);
                radioButtonOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new AttachFileFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("Location","Other");
                        bundle1.putString("AppointmentId", AppointmentId);
                        bundle1.putString("Flag","2");
                        fragment.setArguments(bundle1);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        dialog.dismiss();
                    }
                });
            }
        });


        TextView btnReportFile = view.findViewById(R.id.btnReportFile);
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
}