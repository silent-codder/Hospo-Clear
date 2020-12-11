package com.silentcodder.newhospital.UserRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.UserAppointmentsFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserHomeFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserProfileFragment;

public class UserMainActivity extends AppCompatActivity{

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    Fragment selectFragment;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navMenu);
        drawerLayout = findViewById(R.id.drawer);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        String UserId = firebaseAuth.getCurrentUser().getUid();


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
                    case R.id.user_home :
                        selectFragment = new UserHomeFragment();
                        break;
                    case R.id.user_appointments :
                        selectFragment = new UserAppointmentsFragment();
                        break;
                    case R.id.user_profile :
                        selectFragment = new UserProfileFragment();
                        break;
                    case R.id.user_logout :
                        logOut();
                        Toast.makeText(UserMainActivity.this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (selectFragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectFragment).commit();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UserHomeFragment()).commit();


    }

    private void logOut() {
    }

    private void removeColor(NavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
}