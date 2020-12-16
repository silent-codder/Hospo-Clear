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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silentcodder.newhospital.R;
import com.silentcodder.newhospital.UserRegister.Fragment.UserAppointmentsFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserHomeFragment;
import com.silentcodder.newhospital.UserRegister.Fragment.UserProfileFragment;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserMainActivity extends AppCompatActivity{

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    Fragment selectFragment;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String ProfileUrl;

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
        CircleImageView UserImg = findViewById(R.id.userImg);

        UserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

        String UserId = firebaseAuth.getCurrentUser().getUid();


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.purple_700));

        firebaseFirestore.collection("Users").document(UserId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                ProfileUrl = documentSnapshot.getString("ProfileImgUrl");
                String UserName = documentSnapshot.getString("UserName");
                String MobileNumber = documentSnapshot.getString("MobileNumber");
                View view = nav.inflateHeaderView(R.layout.header_view);
                CircleImageView profile = view.findViewById(R.id.profileImg);
                TextView name = view.findViewById(R.id.userName);
                TextView mobileNumber = view.findViewById(R.id.mobileNumber);
                name.setText(UserName);
                mobileNumber.setText(MobileNumber);
                Picasso.get().load(ProfileUrl).into(profile);
                Picasso.get().load(ProfileUrl).into(UserImg);
            }
        });

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