package com.silentcodder.newhospital.HospitalRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.silentcodder.newhospital.HospitalRegister.Fragment.HospitalHomeFragment;
import com.silentcodder.newhospital.HospitalRegister.Fragment.HospitalProfileFragment;
import com.silentcodder.newhospital.MainActivity;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.UserAppointmentsFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserHomeFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserProfileFragment;
import com.silentcodder.newhospital.UserRegister.UserMainActivity;

public class HospitalMainActivity extends AppCompatActivity {


    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    Fragment selectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.purple_700));

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                removeColor(nav);

                item.setChecked(true);

                switch (item.getItemId()){
                    case R.id.hospital_home :
                        selectFragment = new HospitalHomeFragment();
                        break;
                    case R.id.hospital_appointments :
//                        selectFragment = new UserAppointmentsFragment();
                        Toast.makeText(HospitalMainActivity.this, "Appointment", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.hospital_profile :
                        selectFragment = new HospitalProfileFragment();
                        break;
                    case R.id.hospital_logout :
//                        logOut();
                        Toast.makeText(HospitalMainActivity.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(HospitalMainActivity.this, MainActivity.class));
        finish();
    }

    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
}