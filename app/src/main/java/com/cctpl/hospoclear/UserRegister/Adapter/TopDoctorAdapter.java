package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.UserRegister.Fragment.AboutDoctorFragment;
import com.cctpl.hospoclear.UserRegister.Fragment.BookAppointmentFragment;
import com.cctpl.hospoclear.UserRegister.Fragment.RequestAppointmentFragment;
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

public class TopDoctorAdapter extends RecyclerView.Adapter<TopDoctorAdapter.ViewHolder> {

    List<DoctorData> doctorData;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId,HospitalId,HospitalName;
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
        HospitalId = doctorData.get(position).getHospitalId();

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


        holder.mBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.mBtnMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_hospital,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.addSave:
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
                                                Toast.makeText(context, "Doctor save successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            case R.id.removeSave:
                                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Remove to Doctor", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Sorry, Something wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });


        firebaseFirestore.collection("Hospitals").document(HospitalId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    HospitalName = task.getResult().getString("HospitalName");
                }
            }
        });


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
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

        holder.mBtnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new BookAppointmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DoctorId",DoctorId);
                bundle.putString("HospitalName",HospitalName);
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
        ImageView mBtnMenu;
        ProgressBar progressBar;
        CircleImageView mDoctorImg,mDoctor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout2);
            mBtnMenu = itemView.findViewById(R.id.btnMenu);
            progressBar = itemView.findViewById(R.id.ImgLoader);
            mDoctorImg = itemView.findViewById(R.id.doctorImg);
            mDoctor = itemView.findViewById(R.id.doctor);
            mBtnBookAppointment = itemView.findViewById(R.id.btnBookAppointment);
        }
    }
}
