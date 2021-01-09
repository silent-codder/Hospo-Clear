package com.silentcodder.newhospital.UserRegister.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.silentcodder.newhospital.HospitalRegister.Fragment.AppointmentFragment;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Adapter.UploadImgAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AttachFileFragment extends Fragment {


    private static final int PICK_IMG = 1;
    private List<String> ImageList;
    private UploadImgAdapter uploadImgAdapter;
    private Button btnUpload;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private String location,appointmentId;
    private ProgressBar progressBar;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_attach_file, container, false);
       btnUpload = view.findViewById(R.id.btnUpload);
       progressBar = view.findViewById(R.id.loader);
       textView = view.findViewById(R.id.text);
       firebaseFirestore = FirebaseFirestore.getInstance();
       storageReference = FirebaseStorage.getInstance().getReference();
       ImageList = new ArrayList<>();
       Bundle bundle = this.getArguments();
       if (bundle!=null){
           location  = bundle.getString("Location");
           appointmentId = bundle.getString("AppointmentId");
       }

       btnUpload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadImg();
               btnUpload.setVisibility(View.INVISIBLE);
               progressBar.setVisibility(View.VISIBLE);
               textView.setVisibility(View.VISIBLE);
           }
       });

       RecyclerView recyclerView = view.findViewById(R.id.recycleView);
       uploadImgAdapter = new UploadImgAdapter(ImageList);
       recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
       recyclerView.setHasFixedSize(false);
       recyclerView.setAdapter(uploadImgAdapter);
        ExtendedFloatingActionButton floatingActionButton = view.findViewById(R.id.floatingBtn);
       floatingActionButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
               startActivityForResult(intent,PICK_IMG);
           }
       });
       return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK){
            if (data.getClipData()!=null){
                int imgCount = data.getClipData().getItemCount();
                for (int i = 0 ; i < imgCount ; i++){
                    Uri ImgUri = data.getClipData().getItemAt(i).getUri();
                    String ImgUrl = String.valueOf(ImgUri);
                    ImageList.add(ImgUrl);
                    uploadImgAdapter.notifyDataSetChanged();
                }

            }else {
                Snackbar.make(getView(),"Please select at least 2 images", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImg() {
        for (int i = 0 ; i < ImageList.size() ; i++) {
            String uri = ImageList.get(i);
            StorageReference file = storageReference.child("PatientReports").child(System.currentTimeMillis()+".jpg");
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
                                firebaseFirestore.collection("Appointments").document(appointmentId)
                                        .collection(location).add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnUpload.setVisibility(View.VISIBLE);
                                            textView.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getContext(), "Upload successfully !!", Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new UserAppointmentsFragment();
                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null)
                                                    .commit();
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
                    progressBar.setVisibility(View.INVISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}