package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.UserRegister.Fragment.AboutDoctorFragment;
import com.cctpl.hospoclear.UserRegister.Model.BookmarkDoctorData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cctpl.hospoclear.R;

import java.util.HashMap;
import java.util.List;

public class BookmarkDoctorAdapter extends RecyclerView.Adapter<BookmarkDoctorAdapter.ViewHolder>{

    List<BookmarkDoctorData> bookmarkDoctorData;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String UserId;
    Context context;

    public BookmarkDoctorAdapter(List<BookmarkDoctorData> bookmarkDoctorData) {
        this.bookmarkDoctorData = bookmarkDoctorData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_doctor_view,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String DoctorName = bookmarkDoctorData.get(position).getDoctorName();
        String Speciality = bookmarkDoctorData.get(position).getSpeciality();
        String Experience = bookmarkDoctorData.get(position).getExperience();
        String Qualification = bookmarkDoctorData.get(position).getQualification();
        String DoctorId = bookmarkDoctorData.get(position).getDoctorId();

        holder.mDoctorName.setText(DoctorName);
        holder.mDoctorSpeciality.setText(Speciality + ",");
        holder.mDoctorExperience.setText("Ex : " + Experience);
        holder.mDoctorQualification.setText(Qualification);

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
                                map.put("DoctorName",DoctorName);
                                map.put("Speciality",Speciality);
                                map.put("Experience",Experience);
                                map.put("Qualification",Qualification);
                                map.put("DoctorId",DoctorId);
                                map.put("UserId",UserId);
                                map.put("TimeStamp",System.currentTimeMillis());

                                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                                        .set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Doctor save successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                break;
                            case R.id.removeSave:
                                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Remove to Doctor", Toast.LENGTH_SHORT).show();
                                            bookmarkDoctorData.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position,bookmarkDoctorData.size());
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

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new AboutDoctorFragment();
                Bundle bundle = new Bundle();
                bundle.putString("DoctorId",DoctorId);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmarkDoctorData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDoctorName,mDoctorSpeciality,mDoctorExperience,mDoctorQualification;
        RelativeLayout relativeLayout;
        ImageView mBtnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            mBtnMenu = itemView.findViewById(R.id.btnMenu);
        }
    }
}
