package com.cctpl.hospoclear.UserRegister.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Fragment.CategoryDoctorFragment;
import com.cctpl.hospoclear.UserRegister.Model.AllCategories;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>{

    private AllCategories[] allCategories;

    public AllCategoryAdapter(AllCategories[] allCategories) {
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(allCategories[position].getCategory());
        holder.circleImageView.setImageResource(allCategories[position].getImg());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment fragment = new CategoryDoctorFragment();
                Bundle bundle = new Bundle();
                String category = allCategories[position].getCategory();
                bundle.putString("Category",category.replace("\n",""));
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CircleImageView circleImageView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            circleImageView = itemView.findViewById(R.id.image);
            relativeLayout = itemView.findViewById(R.id.layout);
        }
    }
}
