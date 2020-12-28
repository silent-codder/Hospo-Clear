package com.silentcodder.newhospital.HospitalRegister.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteAppointmentAdapter extends RecyclerView.Adapter<CompleteAppointmentAdapter.ViewHolder> {

    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
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

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                        Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                    }
                });
        holder.mUserName.setText(PatientName);
        holder.mAppointmentDate.setText(AppointmentDate);
        holder.mProblem.setText(Problem);
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserImg;
        TextView mUserName,mAppointmentDate,mProblem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImg = itemView.findViewById(R.id.userImg);
            mUserName = itemView.findViewById(R.id.userName);
            mAppointmentDate = itemView.findViewById(R.id.appointmentDate);
            mProblem = itemView.findViewById(R.id.problem);
        }
    }

}

