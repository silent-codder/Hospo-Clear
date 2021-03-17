package com.cctpl.hospoclear.DoctorRegister.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.HospitalRegister.Fragment.BillFragment;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.AppointmentDetailsFragment;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorActiveAppointmentAdapter extends RecyclerView.Adapter<DoctorActiveAppointmentAdapter.ViewHolder> {


    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    String ProfileUrl,AppointmentId;
    Fragment fragment;

    public DoctorActiveAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_active_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String UserId = appointmentData.get(position).getUserId();
        String PatientName = appointmentData.get(position).getPatientName();
        String AppointmentDate = appointmentData.get(position).getAppointmentDate();
        String Problem = appointmentData.get(position).getProblem();
        AppointmentId = appointmentData.get(position).AppointmentId;
        String DoctorId = appointmentData.get(position).getDoctorId();
        String HospitalId = appointmentData.get(position).getHospitalId();
        String AppointmentTime = appointmentData.get(position).getAppointmentTime();
        String Status = appointmentData.get(position).getStatus();
        String Section = appointmentData.get(position).getSection();
        String SlotId = appointmentData.get(position).getSlotId();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ProfileUrl = task.getResult().getString("ProfileImgUrl");
                        if(ProfileUrl != null){
                            Picasso.get().load(ProfileUrl).into(holder.mPatientImg);
                        }
                    }
                });
        holder.mPatientName.setText(PatientName);
        holder.mAppointmentInfo.setText(AppointmentDate + ", " + AppointmentTime);


        holder.mBtnAppointmentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UserName",PatientName);
                bundle.putString("AppointmentDate",AppointmentDate);
                bundle.putString("AppointmentTime",AppointmentTime);
                bundle.putString("Problem",Problem);
                bundle.putString("DoctorId",DoctorId);
                bundle.putString("HospitalId",HospitalId);
                bundle.putString("Status",Status);
                bundle.putString("ProfileUrl",ProfileUrl);
                bundle.putString("AppointmentId",AppointmentId);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new AppointmentDetailsFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.mMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_doctor_active_appointment,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.requestToCompleteAppointment:
                                Dialog dialog = new Dialog(context );
                                dialog.setContentView(R.layout.complete_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
                                Button btnCancel = (Button) dialog.findViewById(R.id.btnNo);
                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("Flag","false");
                                        HashMap<String, Object> map1 = new HashMap<>();
                                        map1.put("Status","Pending");
                                        HashMap<String, Object> status = new HashMap<>();
                                        status.put("DoctorId",DoctorId);
                                        status.put("TimeStamp",System.currentTimeMillis());
                                        status.put("UserId",UserId);
                                        status.put("Description","Appointment on process by");
                                        firebaseFirestore.collection("Appointments").document(AppointmentId)
                                                .collection("Status").add(status);
                                        firebaseFirestore.collection("Doctors").document(DoctorId).collection(Section).document(SlotId)
                                                .update(map);
                                        firebaseFirestore.collection("Appointments").document(AppointmentId)
                                                .update(map1)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(context, "Request for complete appointment", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        appointmentData.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position,appointmentData.size());
                                        dialog.dismiss();
                                    }
                                });

                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                break;
                            case R.id.addBill:
                                fragment = new BillFragment();
                                AppCompatActivity activity = (AppCompatActivity) context;
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("Type","Bill");
                                bundle1.putString("AppointmentId", AppointmentId);
                                fragment.setArguments(bundle1);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mPatientImg;
        TextView mPatientName,mAppointmentInfo;
        RelativeLayout mBtnAppointmentInfo;
        ImageView mMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           mPatientImg = (CircleImageView) itemView.findViewById(R.id.patientImg);
           mPatientName = (TextView) itemView.findViewById(R.id.patientName);
           mAppointmentInfo = (TextView) itemView.findViewById(R.id.appointmentInfo);
           mBtnAppointmentInfo = (RelativeLayout) itemView.findViewById(R.id.btnAppointmentInfo);
           mMenu = (ImageView) itemView.findViewById(R.id.btnMenu);
        }
    }

}
