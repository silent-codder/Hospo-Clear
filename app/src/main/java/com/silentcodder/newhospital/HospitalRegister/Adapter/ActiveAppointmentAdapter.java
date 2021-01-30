package com.silentcodder.newhospital.HospitalRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.HospitalRegister.Fragment.BillFragment;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.AppointmentDetailsFragment;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveAppointmentAdapter extends RecyclerView.Adapter<ActiveAppointmentAdapter.ViewHolder> {


    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    String ProfileUrl;

    public ActiveAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String UserId = appointmentData.get(position).getUserId();
        String PatientName = appointmentData.get(position).getPatientName();
        String AppointmentDate = appointmentData.get(position).getAppointmentDate();
        String Problem = appointmentData.get(position).getProblem();
        String AppointmentId = appointmentData.get(position).AppointmentId;
        String DoctorId = appointmentData.get(position).getDoctorId();
        String HospitalId = appointmentData.get(position).getHospitalId();
        String Status = appointmentData.get(position).getStatus();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ProfileUrl = task.getResult().getString("ProfileImgUrl");
                        holder.progressBar.setVisibility(View.VISIBLE);
                        if(ProfileUrl != null){
                            holder.mUser.setVisibility(View.INVISIBLE);
                            Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                        }else {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        holder.mUserName.setText(PatientName);
        holder.mAppointmentDate.setText(AppointmentDate);
        holder.mProblem.setText(Problem);

//        holder.mBtnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String,Object> map = new HashMap<>();
//                map.put("Status","4");
//
//                firebaseFirestore.collection("Appointments").document(AppointmentId)
//                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            Toast.makeText(context, " Requesting for Complete Appointment", Toast.LENGTH_SHORT).show();
//                            holder.mBtnCompleteAppointment.setVisibility(View.GONE);
//                        }
//                    }
//                });
//            }
//        });

        firebaseFirestore.collection("Appointments").document(AppointmentId).collection("Bill")
                .document(AppointmentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String totalBill = task.getResult().getString("TotalBill");
                    if(totalBill != null){
                        holder.mTotalBill.setText("Bill Amount :  " + totalBill);
                    }else {
                        holder.mTotalBill.setText("Bill Amount :  -");
                    }
                }
            }
        });

        holder.mViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lottieAnimationView.setVisibility(View.INVISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("UserName",PatientName);
                bundle.putString("AppointmentDate",AppointmentDate);
                bundle.putString("DoctorId",DoctorId);
                bundle.putString("HospitalId",HospitalId);
                bundle.putString("Status",Status);
                bundle.putString("ProfileUrl",ProfileUrl);
                bundle.putString("AppointmentId",AppointmentId);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new AppointmentDetailsFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                        .commit();
            }
        });

        //for complete appointment
//        if (Status.equals("4")){
//            holder.mBtnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HashMap<String,Object> map = new HashMap<>();
//                    map.put("Status","1");
//
//                    firebaseFirestore.collection("Appointments").document(AppointmentId)
//                            .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(context, "Complete Appointment", Toast.LENGTH_SHORT).show();
//                                holder.mBtnCompleteAppointment.setVisibility(View.GONE);
//                            }
//                        }
//                    });
////                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
////                    Fragment fragment = new BillFragment();
////                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment)
////                            .addToBackStack(null).commit();
//
//                }
//            });
//        }else {
//            holder.mBtnCompleteAppointment.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    HashMap<String,Object> map = new HashMap<>();
//                    map.put("Status","4");
//
//                    firebaseFirestore.collection("Appointments").document(AppointmentId)
//                            .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(context, "Request to Complete Appointment", Toast.LENGTH_SHORT).show();
//                                holder.mBtnCompleteAppointment.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                }
//            });
//        }
        
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserImg,mUser;
        ProgressBar progressBar;
        TextView mUserName,mAppointmentDate,mProblem,mBtnViewDetails,mTotalBill;
        LottieAnimationView lottieAnimationView;
        RelativeLayout mViewDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImg = itemView.findViewById(R.id.userImg);
            mUserName = itemView.findViewById(R.id.userName);
            mAppointmentDate = itemView.findViewById(R.id.appointmentDate);
            mProblem = itemView.findViewById(R.id.problem);
            mUser = itemView.findViewById(R.id.userIm);
            progressBar = itemView.findViewById(R.id.loader);
            mViewDetails = itemView.findViewById(R.id.viewDetails);
            mBtnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            mTotalBill = itemView.findViewById(R.id.totalBill);
            lottieAnimationView = itemView.findViewById(R.id.newNotification);
        }
    }

}
