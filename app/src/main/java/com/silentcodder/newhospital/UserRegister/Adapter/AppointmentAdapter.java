package com.silentcodder.newhospital.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.SeePrescriptionFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    Dialog dialog;
    String DoctorName,HospitalName,ProfileUrl;
    ProgressDialog progressDialog;

    public AppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        dialog = new Dialog(context);
        progressDialog = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String PatientName = appointmentData.get(position).getPatientName();
        String Problem = appointmentData.get(position).getProblem();
        String Status = appointmentData.get(position).getStatus();
        String Date = appointmentData.get(position).getAppointmentDate();
        String UserId = appointmentData.get(position).getUserId();
        String HospitalId = appointmentData.get(position).getHospitalId();
        String DoctorId = appointmentData.get(position).getDoctorId();
        String PrescriptionImgUrl = appointmentData.get(position).getPrescriptionImgUrl();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                        }
                    }
                });

        if (Status.equals("1")){
            holder.mComplete.setVisibility(View.VISIBLE);
            holder.mRequest.setVisibility(View.INVISIBLE);
            holder.mPending.setVisibility(View.INVISIBLE);
        }else if(Status.equals("2") || Status.equals("4")){
            holder.mComplete.setVisibility(View.INVISIBLE);
            holder.mRequest.setVisibility(View.INVISIBLE);
            holder.mPending.setVisibility(View.VISIBLE);
        }else if(Status.equals("3")){
            holder.mComplete.setVisibility(View.INVISIBLE);
            holder.mRequest.setVisibility(View.VISIBLE);
            holder.mPending.setVisibility(View.INVISIBLE);
        }

        holder.mPatientName.setText(PatientName);
        holder.mProblem.setText(Problem);
        holder.mDate.setText(Date);
        holder.mMoreInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.appointment_more_info);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                CircleImageView userImg = dialog.findViewById(R.id.userImg);
                TextView patientName = dialog.findViewById(R.id.patientName);
                TextView appointmentDate = dialog.findViewById(R.id.appointmentDate);
                TextView hospitalName = dialog.findViewById(R.id.hospitalName);
                TextView doctorName = dialog.findViewById(R.id.doctorName);
                TextView status = dialog.findViewById(R.id.status);
                CircleImageView requestImg = dialog.findViewById(R.id.requestImg);
                CircleImageView pendingImg = dialog.findViewById(R.id.pendingImg);
                CircleImageView completeImg = dialog.findViewById(R.id.completeImg);

                if (PrescriptionImgUrl != null){
                    Button BtnSeePrescription = dialog.findViewById(R.id.btnSeePrescription);
                    BtnSeePrescription.setVisibility(View.VISIBLE);
                    BtnSeePrescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            AppCompatActivity activity = (AppCompatActivity) context;
                            activity.getApplication().getApplicationContext();
                            Fragment fragment = new SeePrescriptionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("ImageUrl" , PrescriptionImgUrl);
                            fragment.setArguments(bundle);
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                        }
                    });
                }

                if (Status.equals("1")){
                    completeImg.setVisibility(View.VISIBLE);
                    status.setText("Appointment was complete");
                    requestImg.setVisibility(View.INVISIBLE);
                    pendingImg.setVisibility(View.INVISIBLE);
                }else if(Status.equals("2")){
                    completeImg.setVisibility(View.INVISIBLE);
                    requestImg.setVisibility(View.INVISIBLE);
                    pendingImg.setVisibility(View.VISIBLE);
                    status.setText("Appointment is pending");
                }else if(Status.equals("3")){
                    completeImg.setVisibility(View.INVISIBLE);
                    requestImg.setVisibility(View.VISIBLE);
                    pendingImg.setVisibility(View.INVISIBLE);
                    status.setText("Requesting Appointment");
                }

                firebaseFirestore.collection("Doctors").document(DoctorId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DoctorName = task.getResult().getString("DoctorName");
                                    doctorName.setText(DoctorName);
                                }
                            }
                        });
                firebaseFirestore.collection("Hospitals").document(HospitalId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    HospitalName = task.getResult().getString("HospitalName");
                                    hospitalName.setText(HospitalName);
                                }
                            }
                        });

                patientName.setText(PatientName);
                appointmentDate.setText(Date);
                Picasso.get().load(ProfileUrl).into(userImg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPatientName,mProblem,mDate;
        CircleImageView mPending,mRequest,mComplete,mUserImg;
        RelativeLayout mMoreInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPatientName = itemView.findViewById(R.id.patientName);
            mProblem = itemView.findViewById(R.id.problem);
            mDate = itemView.findViewById(R.id.date);
            mComplete = itemView.findViewById(R.id.completeImg);
            mPending = itemView.findViewById(R.id.pendingImg);
            mRequest = itemView.findViewById(R.id.requestImg);
            mMoreInfo = itemView.findViewById(R.id.moreInfo);
            mUserImg = itemView.findViewById(R.id.userImg);
        }
    }
}
