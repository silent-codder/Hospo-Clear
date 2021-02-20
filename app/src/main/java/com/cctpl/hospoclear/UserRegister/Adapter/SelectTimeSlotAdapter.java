package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.SelectUserFragment;
import com.cctpl.hospoclear.UserRegister.Model.TimeSlotData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        String DoctorId = doctorData.get(position).getDoctorId();
        String Id = doctorData.get(position).TimeSlotId;
        String Flag = doctorData.get(position).getFlag();

        holder.mTimeSlot.setText(TimeSlot);
//        holder.mTimeSlotRed.setText(TimeSlot);
//        holder.mTimeSlotGreen.setText(TimeSlot);
//
//        if (Flag.equals("1")){
//            holder.relativeLayoutRed.setVisibility(View.VISIBLE);
//            holder.relativeLayout.setVisibility(View.GONE);
//        }else {
//            holder.relativeLayout.setVisibility(View.VISIBLE);
//        }
//
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("UseCompatLoadingForDrawables")
//            @Override
//            public void onClick(View v) {
//                String timeSlot = doctorData.get(position).getTimeSlot();
//                String flag = "1";
//                AddFlag(DoctorId,timeSlot,Id,flag);
//            }
//        });
//        holder.relativeLayoutGreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.relativeLayoutGreen.setVisibility(View.GONE);
//                String timeSlot = doctorData.get(position).getTimeSlot();
//                String flag = "2";
//                AddFlag(DoctorId,timeSlot,Id,flag);
//            }
//        });

    }

    private void AddFlag(String doctorId,String timeSlot,String Id,String flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppointmentData",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashMap<String, Object> map = new HashMap<>();
        map.put("Flag",flag);

        firebaseFirestore.collection("Doctors").document(doctorId)
                .collection("Morning").document(Id).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            String Date = sharedPreferences.getString("AppointmentDate",null);
                            if (TextUtils.isEmpty(Date)){
                                Toast.makeText(context, "Select Date !!!", Toast.LENGTH_SHORT).show();
                            }else {
                                AppCompatActivity activity = (AppCompatActivity) context;
                                Fragment fragment = new SelectUserFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                            }
                        }
                    }
                });

        editor.putString("DoctorId", doctorId);
        editor.putString("TimeSlot",timeSlot);
        editor.putString("Section","Morning");
        editor.putString("SlotId",Id);
        editor.commit();


    }

    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayoutRed,relativeLayoutGreen;
        CardView relativeLayout;
        TextView mTimeSlot,mTimeSlotRed,mTimeSlotGreen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTimeSlot = itemView.findViewById(R.id.timeSlot);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);

        }
    }
}
