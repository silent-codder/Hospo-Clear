package com.cctpl.hospoclear.UserRegister.Adapter;

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

import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.SeePrescriptionFragment;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalProfileImagesAdapter extends RecyclerView.Adapter<HospitalProfileImagesAdapter.ViewHolder>{

    List<HospitalData>hospitalData;

    public HospitalProfileImagesAdapter(List<HospitalData> hospitalData) {
        this.hospitalData = hospitalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_img_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ImgUrl = hospitalData.get(position).getImgUrl();
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
        return hospitalData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.btnImage);
            progressBar = itemView.findViewById(R.id.loader);
        }
    }
}
