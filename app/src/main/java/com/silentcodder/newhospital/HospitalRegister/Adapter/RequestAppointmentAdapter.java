package com.silentcodder.newhospital.HospitalRegister.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.APIService;
import com.silentcodder.newhospital.Client;
import com.silentcodder.newhospital.Data;
import com.silentcodder.newhospital.NotificationSender;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAppointmentAdapter extends RecyclerView.Adapter<RequestAppointmentAdapter.ViewHolder> {

    List<AppointmentData> appointmentData;
    FirebaseFirestore firebaseFirestore;
    Context context;
    String fcmUrl = "https://fcm.googleapis.com/";

    public RequestAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_appointment_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String UserId = appointmentData.get(position).getUserId();
        String PatientName = appointmentData.get(position).getPatientName();
        String AppointmentDate = appointmentData.get(position).getAppointmentDate();
        String Problem = appointmentData.get(position).getProblem();
        String AppointmentId = appointmentData.get(position).AppointmentId;
        String DoctorId = appointmentData.get(position).getDoctorId();

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                        holder.progressBar.setVisibility(View.VISIBLE);
                        if(ProfileUrl != null){
                            holder.mUser.setVisibility(View.INVISIBLE);
                            Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                        }else {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        holder.mUserName.setText(PatientName);
        holder.mAppointmentDate.setText(AppointmentDate);
        holder.mProblem.setText(Problem);

        holder.mBtnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.lottieAnimationView.setVisibility(View.INVISIBLE);

                firebaseFirestore.collection("Tokens").document(DoctorId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String Token = task.getResult().getString("token");
                                    sendNotification(Token,PatientName,Problem);
                                }
                            }
                        });

                HashMap<String,Object> map = new HashMap<>();
                map.put("Status","2");
                firebaseFirestore.collection("Appointments").document(AppointmentId)
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                            holder.mBtnAcceptRequest.setVisibility(View.INVISIBLE);
                            holder.mBtnCancelRequest.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
        });

        holder.mBtnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("Status","3");
                firebaseFirestore.collection("Appointments").document(AppointmentId)
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Cancel request", Toast.LENGTH_SHORT).show();
                            holder.mBtnAcceptRequest.setVisibility(View.VISIBLE);
                            holder.mBtnCancelRequest.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });


    }

    private void sendNotification(String token, String title, String msg) {
        Data data = new Data(title,msg);
        NotificationSender notificationSender = new NotificationSender(data,token);

        APIService apiService = Client.getRetrofit(fcmUrl).create(APIService.class);

        apiService.sendNotification(notificationSender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mUserImg,mUser;
        ProgressBar progressBar;
        TextView mUserName,mAppointmentDate,mProblem;
        Button mBtnAcceptRequest,mBtnCancelRequest;
        LottieAnimationView lottieAnimationView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImg = itemView.findViewById(R.id.userImg);
            mUserName = itemView.findViewById(R.id.userName);
            mAppointmentDate = itemView.findViewById(R.id.appointmentDate);
            mProblem = itemView.findViewById(R.id.problem);
            mUser = itemView.findViewById(R.id.userIm);
            progressBar = itemView.findViewById(R.id.loader);
            mBtnAcceptRequest = itemView.findViewById(R.id.btnRequestAccept);
            mBtnCancelRequest = itemView.findViewById(R.id.btnRequestCancel);
            lottieAnimationView = itemView.findViewById(R.id.newNotification);
        }
    }
}
