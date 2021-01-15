package com.silentcodder.newhospital.UserRegister.Adapter;

import android.annotation.SuppressLint;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.AboutDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopDoctorAdapter extends RecyclerView.Adapter<TopDoctorAdapter.ViewHolder> {

    List<DoctorData> doctorData;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;
    Context context;
    public TopDoctorAdapter(List<DoctorData> doctorData) {
        this.doctorData = doctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_doctor_view,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
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

        //Bookmark hospital
        holder.mBookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String , Object> map = new HashMap<>();
                map.put("DoctorName",DoctorName);
                map.put("Speciality",Speciality);
                map.put("Experience",Experience);
                map.put("Qualification",Qualification);
                map.put("DoctorId",DoctorId);
                map.put("UserId",UserId);
                map.put("TimeStamp",System.currentTimeMillis());

                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                        .set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Add to favorite successfully", Toast.LENGTH_SHORT).show();
                                holder.mBookMarkWhite.setVisibility(View.VISIBLE);
                                holder.mBookMark.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });
        //Remove Bookmark
        holder.mBookMarkWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder.mBookMark.setVisibility(View.VISIBLE);
                        holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Remove to favorite successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //show bookmark hospital or not
        firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                    holder.mBookMark.setVisibility(View.VISIBLE);
                }else {
                    holder.mBookMark.setVisibility(View.INVISIBLE);
                    holder.mBookMarkWhite.setVisibility(View.VISIBLE);
                }
            }
        });

        holder.mBtnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new AboutDoctorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DoctorId",DoctorId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDoctorName,mDoctorSpeciality,mDoctorExperience,mDoctorQualification,mBtnBookAppointment;
        RelativeLayout relativeLayout;
        ImageView mBookMark,mBookMarkWhite;
        ProgressBar progressBar;
        CircleImageView mDoctorImg,mDoctor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            mBookMark = itemView.findViewById(R.id.bookmark);
            mBookMarkWhite = itemView.findViewById(R.id.bookmark_white);
            progressBar = itemView.findViewById(R.id.ImgLoader);
            mDoctorImg = itemView.findViewById(R.id.doctorImg);
            mDoctor = itemView.findViewById(R.id.doctor);
            mBtnBookAppointment = itemView.findViewById(R.id.btnBookAppointment);
        }
    }
}
