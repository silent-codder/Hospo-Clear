package com.cctpl.hospoclear.UserRegister.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.HospitalDetailsFragment;
import com.cctpl.hospoclear.UserRegister.Model.HospitalData;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalImageAdapter extends  SliderViewAdapter<HospitalImageAdapter.SliderAdapterVH> {

    List<HospitalData> hospitalData;

    public HospitalImageAdapter(List<HospitalData> hospitalData) {
        this.hospitalData = hospitalData;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.silder_layout, parent,false);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        String ImgUrl = hospitalData.get(position).getImgUrl();
        Glide.with(viewHolder.itemView)
                .load(ImgUrl)
                .fitCenter()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return hospitalData.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageView;
        ProgressBar progressBar;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myimage);
            progressBar = itemView.findViewById(R.id.loader);
            this.itemView = itemView;
        }
    }
}
