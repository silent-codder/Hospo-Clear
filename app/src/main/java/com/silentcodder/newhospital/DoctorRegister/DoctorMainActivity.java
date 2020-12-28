package com.silentcodder.newhospital.DoctorRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.HospitalRegister.Fragment.HospitalHomeFragment;
import com.silentcodder.newhospital.HospitalRegister.Fragment.HospitalProfileFragment;
import com.silentcodder.newhospital.HospitalRegister.Fragment.PersonalProfileFragment;
import com.silentcodder.newhospital.HospitalRegister.HospitalMainActivity;
import com.silentcodder.newhospital.MainActivity;
import com.silentcodder.newhospital.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorMainActivity extends AppCompatActivity {


    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    Fragment selectFragment;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String UserId,ProfileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        CircleImageView UserImg = findViewById(R.id.userImg);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.purple_700));
//Header here
//        firebaseFirestore.collection("Hospitals").document(UserId)
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                ProfileUrl = documentSnapshot.getString("ProfileImgUrl");
//                String UserName = documentSnapshot.getString("HospitalName");
//                String MobileNumber = documentSnapshot.getString("MobileNumber");
//                View view = nav.inflateHeaderView(R.layout.header_view);
//                CircleImageView profile = view.findViewById(R.id.profileImg);
//                TextView name = view.findViewById(R.id.userName);
//                TextView mobileNumber = view.findViewById(R.id.mobileNumber);
//                name.setText(UserName);
//                name.setSelected(true);
//                name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//                name .setSingleLine(true);
//                mobileNumber.setText(MobileNumber);
//                Picasso.get().load(ProfileUrl).into(profile);
//                Picasso.get().load(ProfileUrl).into(UserImg);
//            }
//        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                removeColor(nav);

                item.setChecked(true);

                switch (item.getItemId()){
                    case R.id.hospital_home :
                        selectFragment = new HospitalHomeFragment();
                        break;
                    case R.id.hospital_Doctors :
                        Toast.makeText(DoctorMainActivity.this, "Working on it", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.hospital_profile :
                        selectFragment = new HospitalProfileFragment();
                        break;
                    case R.id.hospital_doctor_profile :
                        selectFragment = new PersonalProfileFragment();
                        break;
                    case R.id.hospital_logout :
                        logOut();
                        Toast.makeText(DoctorMainActivity.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HospitalHomeFragment()).commit();
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DoctorMainActivity.this, MainActivity.class));
        finish();
    }

    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
}