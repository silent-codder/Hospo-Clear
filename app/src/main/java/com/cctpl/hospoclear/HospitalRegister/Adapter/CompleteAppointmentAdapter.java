package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.AppointmentDetailsFragment;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteAppointmentAdapter extends RecyclerView.Adapter<CompleteAppointmentAdapter.ViewHolder> {

    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    String ProfileUrl;
    public CompleteAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);    }

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

        holder.mBtnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserImg,mUser;
        ProgressBar progressBar;
        TextView mUserName,mAppointmentDate,mProblem,mBtnViewDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImg = itemView.findViewById(R.id.userImg);
            mUserName = itemView.findViewById(R.id.userName);
            mAppointmentDate = itemView.findViewById(R.id.appointmentDate);
            mProblem = itemView.findViewById(R.id.problem);
            progressBar = itemView.findViewById(R.id.loader);
            mUser = itemView.findViewById(R.id.userIm);
            mBtnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }
    }

}

