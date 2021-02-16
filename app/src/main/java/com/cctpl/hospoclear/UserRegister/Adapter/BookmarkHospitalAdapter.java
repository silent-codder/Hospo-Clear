package com.cctpl.hospoclear.UserRegister.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.cctpl.hospoclear.UserRegister.Fragment.TopHospitalDoctorFragment;
import com.cctpl.hospoclear.UserRegister.Model.BookmarkHospitalData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.cctpl.hospoclear.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;


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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_hospital,parent,false);
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

        firebaseFirestore.collection("Hospitals").document(HospitalId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String ProfileUrl = task.getResult().getString("ProfileImgUrl");
                            holder.progressBar.setVisibility(View.VISIBLE);
                            if(ProfileUrl != null){
                                holder.mHospital.setVisibility(View.INVISIBLE);
                                Picasso.get().load(ProfileUrl).into(holder.mHospitalProfileImg);
                            }else {
                                holder.progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });


        holder.mHospitalName.setText(HospitalName + "," +City);
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
                            bookmarkHospitalData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,bookmarkHospitalData.size());
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
        holder.mBtnView.setOnClickListener(new View.OnClickListener() {
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
        TextView mHospitalName,mContact;
        ImageView mBookMark,mBookMarkWhite;
        TextView mBtnView;
        ProgressBar progressBar;
        CircleImageView mHospitalProfileImg,mHospital;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mHospitalName = itemView.findViewById(R.id.hospitalName);
            mContact = itemView.findViewById(R.id.hospitalContactNumber);
            mBtnView = itemView.findViewById(R.id.btnView);
            mBookMark = itemView.findViewById(R.id.bookmark);
            mBookMarkWhite = itemView.findViewById(R.id.bookmark_white);
            mHospitalProfileImg = itemView.findViewById(R.id.hospitalImg);
            progressBar = itemView.findViewById(R.id.ImgLoader);
            mHospital = itemView.findViewById(R.id.hospital);
        }
    }
}
