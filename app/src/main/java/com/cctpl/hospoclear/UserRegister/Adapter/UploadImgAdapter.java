package com.cctpl.hospoclear.UserRegister.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadImgAdapter extends RecyclerView.Adapter<UploadImgAdapter.ViewHolder>{

    List<String> ImageList;

    public UploadImgAdapter(List<String> imageList) {
        ImageList = imageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imgUrl = ImageList.get(position);
        Picasso.get().load(imgUrl).into(holder.imageView);
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public void removeItem(int position) {
        ImageList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,ImageList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,btnCancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
