package com.cctpl.hospoclear.HospitalRegister.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.cctpl.hospoclear.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditHospitalProfile extends AppCompatActivity {

    ImageView mBtnClose,mBtnDone;
    CircleImageView mHospitalImg;
    EditText mHospitalName,mCityName,mStateName,mHospitalContactNumber,mHospitalBio,mEmail;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId;

    ProgressDialog progressDialog;
    Uri profileImgUri;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hospital_profile);

        mBtnClose = findViewById(R.id.btnCancel);
        mBtnDone = findViewById(R.id.btnDone);
        mHospitalName = findViewById(R.id.hospitalName);
        mCityName = findViewById(R.id.city);
        mStateName = findViewById(R.id.state);
        mHospitalContactNumber = findViewById(R.id.hospitalContactNumber);
        mHospitalBio = findViewById(R.id.hospitalBio);
        mHospitalImg = findViewById(R.id.hospitalImg);
        mEmail = findViewById(R.id.hospitalMail);

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore.collection("Hospitals").document(UserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String HospitalName = task.getResult().getString("HospitalName");
                            String City = task.getResult().getString("City");
                            String State = task.getResult().getString("State");
                            String ContactNumber = task.getResult().getString("ContactNumber");
                            String Bio = task.getResult().getString("HospitalBio");
                            String Mail = task.getResult().getString("Email");

                            mHospitalName.setText(HospitalName);
                            mCityName.setText(City);
                            mStateName.setText(State);
                            mHospitalBio.setText(Bio);
                            mHospitalContactNumber.setText(ContactNumber);
                            mEmail.setText(Mail);
                        }
                    }
                });

        //edit profile image

        mHospitalImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImg();
            }
        });

        //Done button code
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String HospitalName = mHospitalName.getText().toString();
                String City = mCityName.getText().toString();
                String State = mStateName.getText().toString();
                String ContactNumber = mHospitalContactNumber.getText().toString();
                String Bio = mHospitalBio.getText().toString();
                String Email = mEmail.getText().toString();

                HashMap<String ,Object> map = new HashMap<>();
                map.put("HospitalName",HospitalName);
                map.put("City",City);
                map.put("State",State);
                map.put("ContactNumber",ContactNumber);
                map.put("HospitalBio",Bio);
                map.put("Email",Email);

                firebaseFirestore.collection("Hospitals").document(UserId).update(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(EditHospitalProfile.this, "Update data", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditHospitalProfile.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }

    private void UploadImg() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setOutputCompressQuality(10)
                .setAspectRatio(1,1)
                .start(this);
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
                mHospitalImg.setImageURI(profileImgUri);
                AddImg();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error : " + error, Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void AddImg() {

        StorageReference profileImgPath = storageReference.child("Profile").child(UserId + ".jpg");

        profileImgPath.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgPath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        String ProfileUri = task.getResult().toString();
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("ProfileImgUrl" , ProfileUri);

                        firebaseFirestore.collection("Hospitals").document(UserId).update(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Toast.makeText(EditHospitalProfile.this, "Update profile image", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditHospitalProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditHospitalProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}