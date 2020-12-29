package com.silentcodder.newhospital.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.HashMap;
import java.util.List;

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


        holder.mHospitalName.setText(HospitalName);
        holder.mCity.setText(City + ",");
        holder.mState.setText(State);
        holder.mContact.setText(Contact);

        //Bookmark hospital
       holder.mBookMark.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
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
                                   Toast.makeText(context, "Add to favorite", Toast.LENGTH_SHORT).show();
                                   holder.mBookMarkWhite.setVisibility(View.VISIBLE);
                                   holder.mBookMark.setVisibility(View.INVISIBLE);
                               }
                           }
                       });
              }
       });
       //Remove Bookmark
       holder.mBookMarkWhite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId)
                       .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       holder.mBookMark.setVisibility(View.VISIBLE);
                       holder.mBookMarkWhite.setVisibility(View.INVISIBLE);
                       Toast.makeText(context, "Remove to favorite", Toast.LENGTH_SHORT).show();
                   }
               });
           }
       });
//
       //show bookmark hospital or not
        firebaseFirestore.collection("Bookmark-Hospital").document(HospitalId)
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
        ImageView mBookMark,mBookMarkWhite;
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
