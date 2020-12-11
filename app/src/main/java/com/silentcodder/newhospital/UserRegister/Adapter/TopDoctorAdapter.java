package com.silentcodder.newhospital.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;

import java.util.List;

public class TopDoctorAdapter extends RecyclerView.Adapter<TopDoctorAdapter.ViewHolder> {

    List<DoctorData> doctorData;

    public TopDoctorAdapter(List<DoctorData> doctorData) {
        this.doctorData = doctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_doctor_view,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String DoctorName = doctorData.get(position).getDoctorName();
        String Speciality = doctorData.get(position).getSpeciality();
        String Experience = doctorData.get(position).getExperience();
        String Qualification = doctorData.get(position).getQualification();

        holder.mDoctorName.setText(DoctorName);
        holder.mDoctorSpeciality.setText(Speciality + ",");
        holder.mDoctorExperience.setText("Ex : " + Experience);
        holder.mDoctorQualification.setText(Qualification);
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDoctorName,mDoctorSpeciality,mDoctorExperience,mDoctorQualification;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
