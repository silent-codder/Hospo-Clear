package com.cctpl.hospoclear.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cctpl.hospoclear.UserRegister.Adapter.UploadImgAdapter;
import com.cctpl.hospoclear.UserRegister.Fragment.HospitalDetailsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.cctpl.hospoclear.R;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class HospitalProfileFragment extends Fragment {

    private static final int PICK_IMG = 100;
    private List<String> ImageList;
    CircleImageView mHospitalImg;
    TextView mHospitalName,mHospitalAddress,mHospitalContactNumber,mHospitalBio;
    Button mBtnEditProfile,mBtnAddHospitalImages,mBtnUpload;
    RecyclerView recyclerView;
    UploadImgAdapter uploadImgAdapter;

    FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    String UserId;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hospital_profile, container, false);

        mHospitalImg = view.findViewById(R.id.hospitalImg);
        mHospitalName = view.findViewById(R.id.hospitalName);
        mHospitalAddress = view.findViewById(R.id.hospitalAddress);
        mHospitalContactNumber = view.findViewById(R.id.hospitalContactNumber);
        mHospitalBio = view.findViewById(R.id.hospitalBio);
        mBtnEditProfile = view.findViewById(R.id.btnEditProfile);
        mBtnAddHospitalImages = view.findViewById(R.id.btnAddHospitalImages);
        recyclerView = view.findViewById(R.id.recycleView);
        mBtnUpload = view.findViewById(R.id.btnUpload);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching data..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        ImageList = new ArrayList<>();

        Button button = view.findViewById(R.id.btnShowFile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new HospitalDetailsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        firebaseFirestore.collection("Hospitals").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            String HospitalName = task.getResult().getString("HospitalName");
                            String City = task.getResult().getString("City");
                            String State = task.getResult().getString("State");
                            String ContactNumber = task.getResult().getString("ContactNumber");
                            String Bio = task.getResult().getString("HospitalBio");
                            String ProfileUrl =  task.getResult().getString("ProfileImgUrl");

                            mHospitalName.setText(HospitalName);
                            mHospitalAddress.setText(City+", "+State);
                            mHospitalContactNumber.setText(ContactNumber);
                            mHospitalBio.setText(Bio);
                            Picasso.get().load(ProfileUrl).into(mHospitalImg);

                        }
                    }
                });

        mBtnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(),EditHospitalProfile.class));
            }
        });

        //Add Hospital images
        mBtnAddHospitalImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(intent,PICK_IMG);

                recyclerView.setVisibility(View.VISIBLE);
                mBtnUpload.setVisibility(View.VISIBLE);
            }
        });

        mBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
                progressDialog.setMessage("Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        });
        recyclerView = view.findViewById(R.id.recycleView);
        uploadImgAdapter = new UploadImgAdapter(ImageList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(uploadImgAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int imgCount = data.getClipData().getItemCount();

                for (int i = 0; i < imgCount; i++) {
                    Uri ImgUri = data.getClipData().getItemAt(i).getUri();
                    String ImgUrl = String.valueOf(ImgUri);
                    ImageList.add(ImgUrl);
                    uploadImgAdapter.notifyDataSetChanged();
                }
            }else {
                Snackbar.make(getView(),"Select minimum 2 images..",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImg() {
        for (int i = 0 ; i < ImageList.size() ; i++) {
            String uri = ImageList.get(i);
            StorageReference file = storageReference.child("HospitalImages").child(System.currentTimeMillis()+".jpg");
            file.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    file.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                String Url = task.getResult().toString();
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("ImgUrl",Url);
                                firebaseFirestore.collection("Hospitals").document(UserId)
                                        .collection("Images").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            try {
                                                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                                                Snackbar.make(getView(),"Upload Successfully !!",Snackbar.LENGTH_LONG).show();
                                            }catch (Exception ignored){

                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snackbar.make(getView(),"Time out please try again !!",Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        }
    }
}