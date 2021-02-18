package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.DoctorId;
import com.cctpl.hospoclear.UserRegister.Model.EveningTimeSlotData;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class SelectEveningTimeSlotAdapter extends RecyclerView.Adapter<SelectEveningTimeSlotAdapter.ViewHolder> {
    List<EveningTimeSlotData> doctorData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    String Flag;

    public SelectEveningTimeSlotAdapter(List<EveningTimeSlotData> doctorData) {
        this.doctorData = doctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slots_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String TimeSlot = doctorData.get(position).getTimeSlot();
        String DoctorId = doctorData.get(position).getDoctorId();
        String Id = doctorData.get(position).TimeSlotId;
        String Flag = doctorData.get(position).getFlag();

        holder.mTimeSlot.setText(TimeSlot);
        holder.mTimeSlotRed.setText(TimeSlot);
        holder.mTimeSlotGreen.setText(TimeSlot);

        if (Flag.equals("1")){
            holder.relativeLayoutRed.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.GONE);
        }else {
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                holder.relativeLayoutGreen.setVisibility(View.VISIBLE);
                String timeSlot = doctorData.get(position).getTimeSlot();
                String flag = "3";
                AddFlag(DoctorId,timeSlot,Id,flag);
            }
        });
        holder.relativeLayoutGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.relativeLayoutGreen.setVisibility(View.GONE);
                String timeSlot = doctorData.get(position).getTimeSlot();
                String flag = "2";
                AddFlag(DoctorId,timeSlot,Id,flag);
            }
        });

    }

    private void AddFlag(String doctorId,String timeSlot,String Id,String flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppointmentData",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashMap<String, Object> map = new HashMap<>();
        map.put("Flag",flag);

        firebaseFirestore.collection("Doctors").document(doctorId)
                .collection("Evening").document(Id).update(map);

        editor.putString("DoctorId", doctorId);
        editor.putString("TimeSlot",timeSlot);
        editor.putString("Section","Evening");
        editor.putString("SlotId",Id);
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout,relativeLayoutRed,relativeLayoutGreen;
        TextView mTimeSlot,mTimeSlotRed,mTimeSlotGreen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTimeSlot = itemView.findViewById(R.id.timeSlot);
            mTimeSlotRed = itemView.findViewById(R.id.timeSlotRed);
            mTimeSlotGreen = itemView.findViewById(R.id.timeSlotGreen);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayoutRed = itemView.findViewById(R.id.relativeLayoutRed);
            relativeLayoutGreen = itemView.findViewById(R.id.relativeLayoutGreen);
        }
    }
}
