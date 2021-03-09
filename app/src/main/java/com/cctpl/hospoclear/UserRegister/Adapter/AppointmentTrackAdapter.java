package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AppointmentTrackAdapter extends RecyclerView.Adapter<AppointmentTrackAdapter.ViewHolder> {

    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    String DoctorName;
    public AppointmentTrackAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_track_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        long timeStamp = appointmentData.get(position).getTimeStamp();
        String DoctorId = appointmentData.get(position).getDoctorId();
        String Description =appointmentData.get(position).getDescription();

        firebaseFirestore.collection("Doctors").document(DoctorId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DoctorName = task.getResult().getString("DoctorName");
                    holder.mDetails.setText(Description + " " + DoctorName);
                }
            }
        });



        Date d = new Date(timeStamp);

        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        String currentDate = dateFormat.format(d.getTime());
        DateFormat dateFormat1 = new SimpleDateFormat("MMM dd");
        String Date = dateFormat1.format(d.getTime());

        holder.mTime.setText(currentDate);
        holder.mDate.setText(Date);
        Log.d(TAG, "CurrentDate: " + currentDate);
        Log.d(TAG, "Doctor iD: " + DoctorId);
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDate,mTime,mDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mDate = itemView.findViewById(R.id.date);
            mDetails = itemView.findViewById(R.id.details);
            mTime = itemView.findViewById(R.id.time);
        }
    }
}
