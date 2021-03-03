package com.cctpl.hospoclear.HospitalRegister.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.Notification.APIService;
import com.cctpl.hospoclear.Notification.Client;
import com.cctpl.hospoclear.Notification.Data;
import com.cctpl.hospoclear.Notification.NotificationSender;
import com.cctpl.hospoclear.UserRegister.Fragment.AppointmentDetailsFragment;
import com.cctpl.hospoclear.UserRegister.Model.AppointmentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
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
    FirebaseAuth firebaseAuth;
    Context context;
    String fcmUrl = "https://fcm.googleapis.com/";
    String CurrentUserId;
    String ProfileUrl;

    public RequestAppointmentAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_appointment_request_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        CurrentUserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String UserId = appointmentData.get(position).getUserId();
        String PatientName = appointmentData.get(position).getPatientName();
        String AppointmentDate = appointmentData.get(position).getAppointmentDate();
        String AppointmentTime = appointmentData.get(position).getAppointmentTime();
        String AppointmentId = appointmentData.get(position).AppointmentId;
        String DoctorId = appointmentData.get(position).getDoctorId();
        String Problem = appointmentData.get(position).getProblem();
        String HospitalId = appointmentData.get(position).getHospitalId();
        String Status = appointmentData.get(position).getStatus();

        if (TextUtils.isEmpty(AppointmentId)){
           holder.mNoAppointment.setVisibility(View.VISIBLE);
        }

        firebaseFirestore.collection("Users").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ProfileUrl = task.getResult().getString("ProfileImgUrl");
                        if(ProfileUrl != null){
                            Picasso.get().load(ProfileUrl).into(holder.mUserImg);
                        }
                    }
                });
        holder.mUserName.setText(PatientName);
        holder.mAppointmentDate.setText(AppointmentDate);
        holder.mAppointmentTime.setText(AppointmentTime);

        holder.mBtnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Tokens").document(DoctorId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String Token = task.getResult().getString("token");
                                    sendNotification(Token,PatientName,AppointmentTime);
                                }
                            }
                        });
                firebaseFirestore.collection("Tokens").document(UserId).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    String Token = task.getResult().getString("token");
                                    String title = "Appointment Accepted";
                                    firebaseFirestore.collection("Doctors").document(DoctorId).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        String DoctorName = task.getResult().getString("DoctorName");
                                                        sendNotification(Token,title,DoctorName);
                                                    }
                                                }
                                            });
                                }
                            }
                        });


               if (CurrentUserId.equals(DoctorId)){
                   HashMap<String,Object> map = new HashMap<>();
                   map.put("Status","Accept");
                   firebaseFirestore.collection("Appointments").document(AppointmentId)
                           .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                               appointmentData.remove(position);
                               notifyItemRemoved(position);
                               notifyItemRangeChanged(position,appointmentData.size());
                           }
                       }
                   });
               }else {
                   HashMap<String,Object> map1 = new HashMap<>();
                   map1.put("Status","Accept");
                   firebaseFirestore.collection("Appointments").document(AppointmentId)
                           .update(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                               appointmentData.remove(position);
                               notifyItemRemoved(position);
                               notifyItemRangeChanged(position,appointmentData.size());
                           }
                       }
                   });
               }
            }
        });

        holder.mBtnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("Status","Decline");
                firebaseFirestore.collection("Appointments").document(AppointmentId)
                        .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Cancel request", Toast.LENGTH_SHORT).show();
                            appointmentData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,appointmentData.size());
                        }
                    }
                });
            }
        });


        holder.mBtnAppointmentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("UserName",PatientName);
                bundle.putString("AppointmentDate",AppointmentDate);
                bundle.putString("AppointmentTime",AppointmentTime);
                bundle.putString("Problem",Problem);
                bundle.putString("DoctorId",DoctorId);
                bundle.putString("HospitalId",HospitalId);
                bundle.putString("Status",Status);
                bundle.putString("ProfileUrl",ProfileUrl);
                bundle.putString("AppointmentId",AppointmentId);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new AppointmentDetailsFragment();
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
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
        CircleImageView mUserImg;
        TextView mUserName,mAppointmentDate,mAppointmentTime;
        CardView mBtnAcceptRequest,mBtnCancelRequest;
        RelativeLayout mBtnAppointmentInfo;
        RelativeLayout mNoAppointment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserImg = itemView.findViewById(R.id.patientImg);
            mUserName = itemView.findViewById(R.id.patientName);
            mAppointmentDate = itemView.findViewById(R.id.appointmentDate);
            mAppointmentTime = itemView.findViewById(R.id.appointmentTime);
            mBtnAcceptRequest = itemView.findViewById(R.id.btnRequestAccept);
            mBtnCancelRequest = itemView.findViewById(R.id.btnRequestCancel);
            mBtnAppointmentInfo = itemView.findViewById(R.id.btnAppointmentInfo);

            mNoAppointment = itemView.findViewById(R.id.noAppointment);
        }
    }
}
