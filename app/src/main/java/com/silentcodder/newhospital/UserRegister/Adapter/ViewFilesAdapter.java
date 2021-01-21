package com.silentcodder.newhospital.UserRegister.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.SeePrescriptionFragment;
import com.silentcodder.newhospital.UserRegister.Model.AppointmentData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewFilesAdapter extends RecyclerView.Adapter<ViewFilesAdapter.ViewHolder> {
    List<AppointmentData> appointmentData;

    public ViewFilesAdapter(List<AppointmentData> appointmentData) {
        this.appointmentData = appointmentData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_img_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ImgUrl = appointmentData.get(position).getImgUrl();
        String Text = appointmentData.get(position).getPrescription();
        holder.progressBar.setVisibility(View.VISIBLE);
        if (Text!=null){
            holder.text.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment fragment = new SeePrescriptionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Text",Text);
                    fragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                            .commit();
                }
            });
        }
        Picasso.get().load(ImgUrl).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment fragment = new SeePrescriptionFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ImageUrl",ImgUrl);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView text;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.btnImage);
            progressBar = itemView.findViewById(R.id.loader);
            text = itemView.findViewById(R.id.btnText);
        }
    }
}
