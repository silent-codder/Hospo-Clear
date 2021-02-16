package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class SelectTimeSlotAdapter extends RecyclerView.Adapter<SelectTimeSlotAdapter.ViewHolder> {
    List<TimeSlotData> doctorData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    public SelectTimeSlotAdapter(List<TimeSlotData> doctorData) {
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

        holder.mTimeSlot.setText(TimeSlot);
        holder.mTimeSlotRed.setText(TimeSlot);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {

//                holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.select_time_layout_bg));
                holder.relativeLayoutRed.setVisibility(View.VISIBLE);

            }
        });
        holder.relativeLayoutRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.relativeLayoutRed.setVisibility(View.GONE);
//                AddFlag(DoctorId,Id);
            }
        });

    }

    private void AddFlag(String doctorId,String id) {
        HashMap<String ,Object> map = new HashMap<>();
        map.put("Flag","1");
        //1 for select slot
        firebaseFirestore.collection("Doctors").document(doctorId)
                .collection("TimeSlots").document(id).update(map);
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout,relativeLayoutRed;
        TextView mTimeSlot,mTimeSlotRed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTimeSlot = itemView.findViewById(R.id.timeSlot);
            mTimeSlotRed = itemView.findViewById(R.id.timeSlotRed);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            relativeLayoutRed = itemView.findViewById(R.id.relativeLayoutRed);
        }
    }
}
