package com.silentcodder.newhospital.UserRegister.Adapter;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Model.BookmarkHospitalData;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.HashMap;
import java.util.List;

public class BookmarkHospitalAdapter extends RecyclerView.Adapter<BookmarkHospitalAdapter.ViewHolder>{

    List<BookmarkHospitalData> bookmarkHospitalData;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;
    Context context;

    public BookmarkHospitalAdapter(List<BookmarkHospitalData> bookmarkHospitalData) {
        this.bookmarkHospitalData = bookmarkHospitalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_hospital_view,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String HospitalName = bookmarkHospitalData.get(position).getHospitalName();
        String City = bookmarkHospitalData.get(position).getCity();
        String State = bookmarkHospitalData.get(position).getState();
        String Contact = bookmarkHospitalData.get(position).getContactNumber();
        String HospitalId = bookmarkHospitalData.get(position).getHospitalId();


        holder.mHospitalName.setText(HospitalName);
        holder.mCity.setText(City + ",");
        holder.mState.setText(State);
        holder.mContact.setText(Contact);

        holder.mBookMarkWhite.setVisibility(View.VISIBLE);
        //remove bookmark
        holder.mBookMarkWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Remove successfully..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //show bookmark or not
        firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()){
                            holder.mBookMarkWhite.setVisibility(View.VISIBLE);
                            holder.mBookMark.setVisibility(View.INVISIBLE);
                        }else {
                            holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                            holder.mBookMark.setVisibility(View.VISIBLE);
                        }
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
        return bookmarkHospitalData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHospitalName,mCity,mState,mContact;
        ImageView mBookMark,mBookMarkWhite;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHospitalName = itemView.findViewById(R.id.hospitalName);
            mCity = itemView.findViewById(R.id.city);
            mState = itemView.findViewById(R.id.state);
            mContact = itemView.findViewById(R.id.contact);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            mBookMark = itemView.findViewById(R.id.bookmark);
            mBookMarkWhite = itemView.findViewById(R.id.bookmark_white);
        }
    }
}
