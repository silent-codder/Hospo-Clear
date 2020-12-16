package com.silentcodder.newhospital.UserRegister.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    Dialog dialog;

    public AppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        dialog = new Dialog(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String PatientName = appointmentData.get(position).getPatientName();
        String Problem = appointmentData.get(position).getProblem();
        String Status = appointmentData.get(position).getStatus();
        String Date = appointmentData.get(position).getAppointmentDate();
        String UserId = appointmentData.get(position).getUserId();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                        }
                    }
                });

        if (Status.equals("1")){
            holder.mComplete.setVisibility(View.VISIBLE);
            holder.mRequest.setVisibility(View.INVISIBLE);
            holder.mPending.setVisibility(View.INVISIBLE);
        }else if(Status.equals("2")){
            holder.mComplete.setVisibility(View.INVISIBLE);
            holder.mRequest.setVisibility(View.INVISIBLE);
            holder.mPending.setVisibility(View.VISIBLE);
        }else if(Status.equals("3")){
            holder.mComplete.setVisibility(View.INVISIBLE);
            holder.mRequest.setVisibility(View.VISIBLE);
            holder.mPending.setVisibility(View.INVISIBLE);
        }

        holder.mPatientName.setText(PatientName);
        holder.mProblem.setText(Problem);
        holder.mDate.setText(Date);
        holder.mMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mPatientName,mProblem,mDate;
        CircleImageView mPending,mRequest,mComplete,mUserImg;
        RelativeLayout mMoreInfo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPatientName = itemView.findViewById(R.id.patientName);
            mProblem = itemView.findViewById(R.id.problem);
            mDate = itemView.findViewById(R.id.date);
            mComplete = itemView.findViewById(R.id.completeImg);
            mPending = itemView.findViewById(R.id.pendingImg);
            mRequest = itemView.findViewById(R.id.requestImg);
            mMoreInfo = itemView.findViewById(R.id.moreInfo);
            mUserImg = itemView.findViewById(R.id.userImg);
        }
    }
}
