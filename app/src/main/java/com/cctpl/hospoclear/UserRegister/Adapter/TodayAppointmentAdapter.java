package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class TodayAppointmentAdapter extends RecyclerView.Adapter<TodayAppointmentAdapter.ViewHolder> {

    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;

    public TodayAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_appointment_view,parent, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("h:mm aa");
        String time = dateFormat.format(date);
        String currentTime = time.toUpperCase();
        Log.d(TAG, "Time: " + currentTime );

        String Date = appointmentData.get(position).getAppointmentDate();
        String Time = appointmentData.get(position).getAppointmentTime();
        String DoctorId = appointmentData.get(position).getDoctorId();
        String HospitalId = appointmentData.get(position).getHospitalId();
        holder.mDate.setText(Date);
        holder.mTime.setText(Time);



        firebaseFirestore.collection("Doctors").document(DoctorId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String DoctorName = task.getResult().getString("DoctorName");
                    String Speciality = task.getResult().getString("Speciality");
                    String DoctorImg = task.getResult().getString("ProfileImgUrl");
                    holder.mDoctorName.setText(DoctorName);
                    holder.mDoctorSpeciality.setText(Speciality);
                    Picasso.get().load(DoctorImg).into(holder.mDoctorImg);
                }
            }
        });

        firebaseFirestore.collection("Hospitals").document(HospitalId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String HospitalName = task.getResult().getString("HospitalName");
                    String City = task.getResult().getString("City");
                    holder.mHospitalName.setText(HospitalName+", " + City);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDate,mTime,mDoctorName,mDoctorSpeciality,mHospitalName;
        CircleImageView mDoctorImg;
        LottieAnimationView lottieAnimationView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.appointmentDate);
            mTime = itemView.findViewById(R.id.appointmentTime);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = (TextView) itemView.findViewById(R.id.doctorSpeciality);
            mHospitalName = (TextView) itemView.findViewById(R.id.hospitalName);
            mDoctorImg = (CircleImageView) itemView.findViewById(R.id.doctorImg);
            lottieAnimationView = itemView.findViewById(R.id.lottie);
            textView = itemView.findViewById(R.id.expireText);
        }
    }
}
