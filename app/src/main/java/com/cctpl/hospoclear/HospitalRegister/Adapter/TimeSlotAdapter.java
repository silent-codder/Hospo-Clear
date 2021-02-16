package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.DoctorData;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
    List<TimeSlotData> doctorData;
    FirebaseFirestore firebaseFirestore;
    public TimeSlotAdapter(List<TimeSlotData> doctorData) {
        this.doctorData = doctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slots_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String TimeSlot = doctorData.get(position).getTimeSlot();
        String Id = doctorData.get(position).TimeSlotId;
        holder.mTimeSlot.setText(TimeSlot);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.delete_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                Button buttonYes = dialog.findViewById(R.id.btnYes);
                Button buttonNo = dialog.findViewById(R.id.btnNo);
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        firebaseFirestore.collection("Doctors").document(DoctorId)
//                                .collection("TimeSlots").document(Id).delete();
//                        doctorData.remove(position);
//                        notifyItemRemoved(position);
//                        notifyItemRangeChanged(position,doctorData.size());
//                        dialog.dismiss();

                    }
                });
                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView mTimeSlot,mTimeSlotRed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTimeSlot = itemView.findViewById(R.id.timeSlot);
            mTimeSlotRed = itemView.findViewById(R.id.timeSlotRed);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
