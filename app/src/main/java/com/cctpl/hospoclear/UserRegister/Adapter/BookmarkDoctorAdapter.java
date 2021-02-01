package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        //Remove Bookmark
        holder.mBookMarkWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder.mBookMark.setVisibility(View.VISIBLE);
                        holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                        Toast.makeText(context, "Remove to favorite successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //show bookmark hospital or not
        firebaseFirestore.collection("Bookmark-Doctor").document(DoctorId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()){
                    holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                    holder.mBookMark.setVisibility(View.VISIBLE);
                }else {
                    holder.mBookMark.setVisibility(View.INVISIBLE);
                    holder.mBookMarkWhite.setVisibility(View.VISIBLE);
                }
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
        ImageView mBookMark,mBookMarkWhite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDoctorName = itemView.findViewById(R.id.doctorName);
            mDoctorSpeciality = itemView.findViewById(R.id.doctorSpeciality);
            mDoctorExperience = itemView.findViewById(R.id.doctorExperience);
            mDoctorQualification = itemView.findViewById(R.id.doctorQualification);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            mBookMark = itemView.findViewById(R.id.bookmark);
            mBookMarkWhite = itemView.findViewById(R.id.bookmark_white);
        }
    }
}
