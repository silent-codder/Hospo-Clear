package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.UserRegister.Fragment.AppointmentDetailsFragment;
import com.cctpl.hospoclear.UserRegister.Fragment.TrackAppointmentFragment;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        dialog = new Dialog(context);
        progressDialog = new ProgressDialog(context);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String PatientName = appointmentData.get(position).getPatientName();
        String Problem = appointmentData.get(position).getProblem();
        String Status = appointmentData.get(position).getStatus();
        String Date = appointmentData.get(position).getAppointmentDate();
        String UserId = appointmentData.get(position).getUserId();
        String HospitalId = appointmentData.get(position).getHospitalId();
        String DoctorId = appointmentData.get(position).getDoctorId();
        String AppointmentId = appointmentData.get(position).AppointmentId;
        String SlotId = appointmentData.get(position).getSlotId();
        String Time = appointmentData.get(position).getAppointmentTime();
        String Section = appointmentData.get(position).getSection();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            Picasso.get().load(ProfileUrl).into(holder.mPatientImg);
                        }
                    }
                });
        holder.mDate.setText(Date+", " +Time);
        holder.mPatientName.setText(PatientName);

        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.mMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.trackAppointment:
                                Fragment fragment = new TrackAppointmentFragment();
                                AppCompatActivity activity = (AppCompatActivity) context;
                                Bundle bundle = new Bundle();
                                bundle.putString("AppointmentId",AppointmentId);
                                fragment.setArguments(bundle);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                                break;
                            case R.id.deleteAppointment:
                                Dialog dialog = new Dialog(context );
                                dialog.setContentView(R.layout.delete_dialog);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.show();

                                Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
                                Button btnCancel = (Button) dialog.findViewById(R.id.btnNo);
                                btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("Flag","false");
                                        firebaseFirestore.collection("Doctors").document(DoctorId).collection(Section).document(SlotId)
                                                .update(map);
                                        firebaseFirestore.collection("Appointments").document(AppointmentId).delete();
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
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        holder.mBtnAppointmentInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UserName",PatientName);
                bundle.putString("AppointmentDate",Date);
                bundle.putString("AppointmentTime",Time);
                bundle.putString("Problem",Problem);
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
        TextView mPatientName,mProblem,mDate,mTime;
        CircleImageView mPending,mRequest,mComplete,mPatientImg;
        ImageView mMenu;
        RelativeLayout mBtnAppointmentInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mBtnAppointmentInfo = itemView.findViewById(R.id.btnAppointmentInfo);
            mPatientName = itemView.findViewById(R.id.patientName);
            mPatientImg = itemView.findViewById(R.id.patientImg);
            mDate = itemView.findViewById(R.id.appointmentDate);
            mMenu  = itemView.findViewById(R.id.btnMenu);
        }
    }
}
