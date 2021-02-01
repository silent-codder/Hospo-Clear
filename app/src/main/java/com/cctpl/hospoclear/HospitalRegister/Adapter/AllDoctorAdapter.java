package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllDoctorAdapter extends RecyclerView.Adapter<AllDoctorAdapter.ViewHolder>{

    List<DoctorData> doctorData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    Context context;
    public AllDoctorAdapter(List<DoctorData> doctorData) {
        this.doctorData = doctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_doctor_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String DoctorName = doctorData.get(position).getDoctorName();
        String Speciality = doctorData.get(position).getSpeciality();
        String Experience = doctorData.get(position).getExperience();
        String Qualification = doctorData.get(position).getQualification();
        String ProfileUrl = doctorData.get(position).getProfileImgUrl();
        String DoctorId = doctorData.get(position).DoctorId;

        holder.mDoctorName.setText(DoctorName);
        holder.mDoctorSpeciality.setText(Speciality + ",");
        holder.mDoctorExperience.setText("Ex : " + Experience);
        holder.mDoctorQualification.setText(Qualification);

        holder.progressBar.setVisibility(View.VISIBLE);
        if(ProfileUrl != null){
            holder.mDoctor.setVisibility(View.INVISIBLE);
            Picasso.get().load(ProfileUrl).into(holder.mDoctorImg);
        }else {
            holder.progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDoctorName,mDoctorSpeciality,mDoctorExperience,mDoctorQualification;
        RelativeLayout relativeLayout;
        ProgressBar progressBar;
        CircleImageView mDoctorImg,mDoctor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            progressBar = itemView.findViewById(R.id.ImgLoader);
            mDoctorImg = itemView.findViewById(R.id.doctorImg);
            mDoctor = itemView.findViewById(R.id.doctor);
        }
    }
}
