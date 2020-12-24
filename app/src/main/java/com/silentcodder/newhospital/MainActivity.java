package com.silentcodder.newhospital;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.silentcodder.newhospital.HospitalRegister.DoctorRegister;
import com.silentcodder.newhospital.HospitalRegister.HospitalLogin;
import com.silentcodder.newhospital.HospitalRegister.HospitalMainActivity;
import com.silentcodder.newhospital.HospitalRegister.HospitalRegister;
import com.silentcodder.newhospital.UserRegister.UserLogin;
import com.silentcodder.newhospital.UserRegister.UserMainActivity;
import com.silentcodder.newhospital.UserRegister.UserRegister;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    Button mBtnStart;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStart = findViewById(R.id.btnStart);
        dialog = new Dialog(this);


        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.select_user_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                RadioButton user = dialog.findViewById(R.id.user);
                user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.userImg);
                        circleImageView.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.doctorImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.hospitalImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);

                        LinearLayout linearLayout = dialog.findViewById(R.id.linear);
                        linearLayout.setVisibility(View.VISIBLE);
                        TextView register = dialog.findViewById(R.id.register);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, UserRegister.class));
                                finish();
                            }
                        });

                        Button btnNext = dialog.findViewById(R.id.btnNext);
                        btnNext.setVisibility(View.VISIBLE);
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, UserLogin.class));
                                finish();
                            }
                        });
                    }
                });

                RadioButton doctor = dialog.findViewById(R.id.doctor);
                doctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.userImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.doctorImg);
                        circleImageView1.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.hospitalImg);
                        circleImageView2.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);

                        LinearLayout linearLayout = dialog.findViewById(R.id.linear);
                        linearLayout.setVisibility(View.INVISIBLE);


                        Button btnNext = dialog.findViewById(R.id.btnNext);
                        btnNext.setVisibility(View.VISIBLE);
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Doctor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                RadioButton hospital = dialog.findViewById(R.id.hospital);
                hospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CircleImageView circleImageView = dialog.findViewById(R.id.userImg);
                        circleImageView.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView1 = dialog.findViewById(R.id.doctorImg);
                        circleImageView1.setVisibility(View.INVISIBLE);
                        CircleImageView circleImageView2 = dialog.findViewById(R.id.hospitalImg);
                        circleImageView2.setVisibility(View.VISIBLE);
                        CircleImageView circleImageView3 = dialog.findViewById(R.id.question);
                        circleImageView3.setVisibility(View.INVISIBLE);

                        LinearLayout linearLayout = dialog.findViewById(R.id.linear);
                        linearLayout.setVisibility(View.VISIBLE);
                        TextView register = dialog.findViewById(R.id.register);
                        register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this,HospitalRegister.class));
                                finish();
                            }
                        });

                        Button btnNext = dialog.findViewById(R.id.btnNext);
                        btnNext.setVisibility(View.VISIBLE);
                        btnNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, HospitalLogin.class));
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            startActivity(new Intent(MainActivity.this, HospitalMainActivity.class));
        }
    }
}