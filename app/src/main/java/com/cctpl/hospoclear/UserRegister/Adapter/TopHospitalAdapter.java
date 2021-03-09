package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.cctpl.hospoclear.UserRegister.Fragment.TrackAppointmentFragment;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class TopHospitalAdapter extends RecyclerView.Adapter<TopHospitalAdapter.ViewHolder> {

    List<HospitalData> hospitalData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    Context context;
    public TopHospitalAdapter(List<HospitalData> hospitalData) {
        this.hospitalData = hospitalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_hospital_view,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId =firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String HospitalName = hospitalData.get(position).getHospitalName();
        String City = hospitalData.get(position).getCity();
        String State = hospitalData.get(position).getState();
        String Contact = hospitalData.get(position).getContactNumber();
        String HospitalId = hospitalData.get(position).getUserId();
        String ProfileUrl = hospitalData.get(position).getProfileImgUrl();

        holder.mHospitalName.setText(HospitalName);
        holder.mCity.setText(City + ",");
        holder.mState.setText(State);
        holder.mContact.setText(Contact);
        holder.progressBar.setVisibility(View.VISIBLE);
        if(ProfileUrl != null){
            holder.mImg.setVisibility(View.INVISIBLE);
            Picasso.get().load(ProfileUrl).into(holder.mHospitalProfileImg);
        }else {
            holder.progressBar.setVisibility(View.INVISIBLE);
        }

        holder.mBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.mBtnMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_hospital,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.addSave:
                                HashMap<String , Object> map = new HashMap<>();
                                map.put("HospitalId",HospitalId);
                                map.put("HospitalName",HospitalName);
                                map.put("ContactNumber",Contact);
                                map.put("City",City);
                                map.put("State",State);
                                map.put("UserId",UserId);
                                map.put("TimeStamp",System.currentTimeMillis());

                                firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId).set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(context, "Save Hospital", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                break;
                            case R.id.removeSave:
                                firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId)
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Remove to Hospital", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, "Sorry, Something wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        //button for view hospital doctors
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new TopHospitalDoctorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("HospitalId" , HospitalId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHospitalName,mCity,mState,mContact;
        RelativeLayout relativeLayout;
        ImageView mBtnMenu;
        CircleImageView mHospitalProfileImg,mImg;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHospitalName = itemView.findViewById(R.id.hospitalName);
            mCity = itemView.findViewById(R.id.city);
            mState = itemView.findViewById(R.id.state);
            mContact = itemView.findViewById(R.id.hospitalContactNumber);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
//            mBookMark = itemView.findViewById(R.id.bookmark);
//            mBookMarkWhite = itemView.findViewById(R.id.bookmark_white);
            mBtnMenu = itemView.findViewById(R.id.btnMenu);
            mHospitalProfileImg = itemView.findViewById(R.id.hospitalImg);
            mImg = itemView.findViewById(R.id.hospital);
            progressBar = itemView.findViewById(R.id.ImgLoader);
        }
    }
}
