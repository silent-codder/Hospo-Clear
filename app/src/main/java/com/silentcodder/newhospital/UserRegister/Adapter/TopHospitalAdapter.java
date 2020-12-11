package com.silentcodder.newhospital.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;

import java.util.List;

import static android.content.ContentValues.TAG;

public class TopHospitalAdapter extends RecyclerView.Adapter<TopHospitalAdapter.ViewHolder> {

    List<HospitalData> hospitalData;

    public TopHospitalAdapter(List<HospitalData> hospitalData) {
        this.hospitalData = hospitalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_hospital_view,parent,false);
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

        Log.d(TAG, "Hospital Id: " + HospitalId);
        
        holder.mHospitalName.setText(HospitalName);
        holder.mCity.setText(City + ",");
        holder.mState.setText(State);
        holder.mContact.setText(Contact);

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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHospitalName = itemView.findViewById(R.id.hospitalName);
            mCity = itemView.findViewById(R.id.city);
            mState = itemView.findViewById(R.id.state);
            mContact = itemView.findViewById(R.id.contact);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
