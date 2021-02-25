package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
        int hr = doctorData.get(position).getHr();
        String Hr = String.valueOf(hr-2);

        holder.mTimeSlot.setText(TimeSlot);
        holder.mTimeSlotRed.setText(TimeSlot);

        if (Flag.equals("true")){
            holder.mTimeSlotRed.setVisibility(View.VISIBLE);
            holder.mTimeSlot.setVisibility(View.GONE);
        }

        holder.mTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeSlot = doctorData.get(position).getTimeSlot();
                SharedPreferences sharedPreferences = context.getSharedPreferences("AppointmentData", 0);
                Editor editor = sharedPreferences.edit();
                editor.putString("Flag","true");
                editor.putString("TimeSlot",timeSlot);
                editor.putString("SlotId",Id);
                editor.putString("DoctorId",DoctorId);
                editor.putString("Section","Morning");
                editor.putString("Hr",Hr);
                editor.commit();

                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment fragment = new SelectUserFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

    }
    @Override
    public int getItemCount() {
        return doctorData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mBtnSelect;
        TextView mTimeSlot,mTimeSlotRed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTimeSlot = itemView.findViewById(R.id.timeSlot);
            mTimeSlotRed = itemView.findViewById(R.id.timeSlotRed);
            mBtnSelect = itemView.findViewById(R.id.relativeLayout);

        }
    }
}
