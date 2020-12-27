package com.silentcodder.newhospital.HospitalRegister.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Model.DoctorData;
import com.silentcodder.newhospital.UserRegister.Model.HospitalData;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class EditPersonalProfileFragment extends Fragment {

    ImageView mBtnClose,mBtnDone;
    CircleImageView mDoctorImg;
    EditText mDoctorName,mExperience,mQualification,mDoctorBio;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String HospitalId,DoctorId;

    ProgressDialog progressDialog;
    Uri profileImgUri;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_edit_personal_profile, container, false);

        mBtnClose = view.findViewById(R.id.btnCancel);
        mBtnDone = view.findViewById(R.id.btnDone);
        mDoctorName = view.findViewById(R.id.doctorName);
        mExperience = view.findViewById(R.id.doctorExperience);
        mQualification = view.findViewById(R.id.doctorQualification);
        mDoctorBio = view.findViewById(R.id.doctorBio);
        mDoctorImg = view.findViewById(R.id.doctorImg);

        progressDialog = new ProgressDialog(getContext());

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        HospitalId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Doctors").whereEqualTo("HospitalId", HospitalId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> list = task.getResult().getDocuments();
                if (list!=null && list.size()>0){
                    for (DocumentSnapshot doc : list) {
                        DoctorData doctorData = new DoctorData();
                        DoctorId = doc.getId();
                        String DoctorName = doctorData.setDoctorName(doc.getString("DoctorName"));
                        String Qualification = doctorData.setQualification(doc.getString("Qualification"));
                        String Experience = doctorData.setExperience(doc.getString("Experience"));
                        String Bio = doctorData.setDoctorBio(doc.getString("DoctorBio"));
                        mDoctorName.setText(DoctorName);
                        mExperience.setText(Experience);
                        mQualification.setText(Qualification);
                        mDoctorBio.setText(Bio);
                        progressDialog.dismiss();
                    }
                }
            }
        });

        mDoctorImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImg();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DoctorName = mDoctorName.getText().toString();
                String Experience = mExperience.getText().toString();
                String Qualification = mQualification.getText().toString();
                String DoctorBio = mDoctorBio.getText().toString();
                HashMap<String,Object> map = new HashMap<>();
                map.put("DoctorName",DoctorName);
                map.put("Experience",Experience);
                map.put("Qualification",Qualification);
                map.put("DoctorBio",DoctorBio);
                firebaseFirestore.collection("Doctors").document(DoctorId).update(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Update", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new PersonalProfileFragment();
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PersonalProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
        return view;
    }

    private void UploadImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(10)
                .setAspectRatio(1,1)
                .start(getContext(),this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profileImgUri = result.getUri();
                mDoctorImg.setImageURI(profileImgUri);
                AddImg();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), "Error : " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void AddImg() {

        StorageReference profileImgPath = storageReference.child("Profile").child(DoctorId + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String ProfileUri = task.getResult().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ProfileImgUrl" , ProfileUri);

                        firebaseFirestore.collection("Doctors").document(DoctorId).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Update profile image", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}