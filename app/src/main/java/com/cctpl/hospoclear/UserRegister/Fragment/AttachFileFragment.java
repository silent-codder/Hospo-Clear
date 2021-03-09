package com.cctpl.hospoclear.UserRegister.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajithvgiri.canvaslibrary.CanvasView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.cctpl.hospoclear.HospitalRegister.Fragment.AppointmentFragment;
import com.cctpl.hospoclear.R;
import com.cctpl.hospoclear.UserRegister.Adapter.UploadImgAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AttachFileFragment extends Fragment {


    private static final int PICK_IMG = 1;
    private List<String> ImageList;
    private UploadImgAdapter uploadImgAdapter;
    private Button btnUpload;
    private EditText prescription;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private String location,appointmentId,flag,doctorId;
    private ProgressBar progressBar;
    private TextView textView,mDate,mDoctorName;
    private CanvasView canvasView;
    private FloatingActionButton drawBtn,addImgBtn,cancelBtn;
    private RelativeLayout relativeLayoutCanvas;
    private BottomNavigationView navigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_attach_file, container, false);
       btnUpload = view.findViewById(R.id.btnUpload);
       progressBar = view.findViewById(R.id.loader);
       prescription = view.findViewById(R.id.prescription);
       relativeLayoutCanvas = view.findViewById(R.id.canvas);
       drawBtn = view.findViewById(R.id.drawPrescription);
       navigationView = view.findViewById(R.id.navBar);
       addImgBtn = view.findViewById(R.id.addImg);
       cancelBtn = view.findViewById(R.id.cancelBtn);
       mDate = view.findViewById(R.id.date);
       mDoctorName = view.findViewById(R.id.doctorName);
       textView = view.findViewById(R.id.text);
       firebaseFirestore = FirebaseFirestore.getInstance();
       firebaseAuth = FirebaseAuth.getInstance();
       String CurrentUserId = firebaseAuth.getCurrentUser().getUid();
       storageReference = FirebaseStorage.getInstance().getReference();
       ImageList = new ArrayList<>();
       Bundle bundle = this.getArguments();
       if (bundle!=null){
           location  = bundle.getString("Location");
           appointmentId = bundle.getString("AppointmentId");
           doctorId = bundle.getString("DoctorId");
           flag = bundle.getString("Flag");
           if (flag.equals("1") && doctorId.equals(CurrentUserId)){
               drawBtn.setVisibility(View.VISIBLE);
           }
       }

       drawBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               recyclerView.setVisibility(View.INVISIBLE);
               relativeLayoutCanvas.setVisibility(View.VISIBLE);
               navigationView.setVisibility(View.VISIBLE);
               canvasView = new CanvasView(getContext());
               relativeLayoutCanvas.addView(canvasView);
               btnUpload.setVisibility(View.INVISIBLE);
           }
       });

       cancelBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
           }
       });

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh : mm a");
        String currentDate = dateFormat.format(date);
        mDate.setText(currentDate);

        firebaseFirestore.collection("Doctors").document(doctorId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String doctorName = task.getResult().getString("DoctorName");
                    mDoctorName.setText(doctorName);
                }
            }
        });

       btnUpload.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               uploadImg();
               btnUpload.setVisibility(View.INVISIBLE);
               progressBar.setVisibility(View.VISIBLE);
               textView.setVisibility(View.VISIBLE);
           }
       });

       recyclerView = view.findViewById(R.id.recycleView);
       uploadImgAdapter = new UploadImgAdapter(ImageList);
       recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
       recyclerView.setHasFixedSize(false);
       recyclerView.setAdapter(uploadImgAdapter);

       addImgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               relativeLayoutCanvas.setVisibility(View.INVISIBLE);
               navigationView.setVisibility(View.GONE);
               Intent intent = new Intent();
               intent.setType("image/*");
               intent.setAction(Intent.ACTION_GET_CONTENT);
               intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
               startActivityForResult(intent,PICK_IMG);

               recyclerView.setVisibility(View.VISIBLE);
           }
       });


       navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()){
                   case R.id.navigation_delete :
                        canvasView.clearCanvas();
                        break;
                   case R.id.navigation_save:
                       progressBar.setVisibility(View.VISIBLE);
                       textView.setVisibility(View.VISIBLE);
                       saveCanvas();
                       break;
               }

               return true;
           }
       });
       return view;
    }

    private void saveCanvas() {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

//        Bitmap bitmap = relativeLayoutCanvas.getDrawingCache();
        relativeLayoutCanvas.setDrawingCacheEnabled(true);
        relativeLayoutCanvas.buildDrawingCache();
        Bitmap bitmap = relativeLayoutCanvas.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference file = storageReference.child("PatientReports").child("canvas-" + System.currentTimeMillis()+".jpg");
        file.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            String Url = task.getResult().toString();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("ImgUrl",Url);

                            HashMap<String, Object> Status = new HashMap<>();
                            Status.put("TimeStamp",System.currentTimeMillis());
                            Status.put("DoctorId",doctorId);
                            Status.put("Description",location + " attached by");

                            firebaseFirestore.collection("Appointments").document(appointmentId)
                                    .collection("Status").add(Status);

                            firebaseFirestore.collection("Appointments").document(appointmentId)
                                    .collection(location).add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btnUpload.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
                                        canvasView.clearCanvas();
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
        });

        Log.d(TAG, "saveCanvas: " + data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMG && resultCode == RESULT_OK){
            if (data.getClipData()!=null){
                int imgCount = data.getClipData().getItemCount();
                if (imgCount > 0){
                    btnUpload.setVisibility(View.VISIBLE);
                }
                for (int i = 0 ; i < imgCount ; i++){
                    Uri ImgUri = data.getClipData().getItemAt(i).getUri();
                    String ImgUrl = String.valueOf(ImgUri);
                    ImageList.add(ImgUrl);
                    uploadImgAdapter.notifyDataSetChanged();
                }

            }else if(data.getData()!=null){
                Uri ImgUri = data.getData();
                ImageList.add(String.valueOf(ImgUri));
                uploadImgAdapter.notifyDataSetChanged();
                if (ImgUri!=null){
                    btnUpload.setVisibility(View.VISIBLE);
                }
//                Snackbar.make(getView(),"Please select at least 2 images", Snackbar.LENGTH_LONG).show();
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

                                HashMap<String, Object> Status = new HashMap<>();
                                Status.put("TimeStamp",System.currentTimeMillis());
                                Status.put("DoctorId",doctorId);
                                Status.put("Description",location + " attached by");

                                firebaseFirestore.collection("Appointments").document(appointmentId)
                                        .collection("Status").add(Status);

                                firebaseFirestore.collection("Appointments").document(appointmentId)
                                        .collection(location).add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnUpload.setVisibility(View.VISIBLE);
                                            textView.setVisibility(View.INVISIBLE);
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
                    progressBar.setVisibility(View.INVISIBLE);
                    btnUpload.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}