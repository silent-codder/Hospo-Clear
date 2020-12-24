package com.silentcodder.newhospital.HospitalRegister.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.silentcodder.newhospital.HospitalRegister.Fragment.AppointmentFragment;
import com.silentcodder.newhospital.HospitalRegister.Fragment.RequestAppointmentFragment;

public class TabAdapter extends FragmentPagerAdapter {
    int totalTabs;

    public TabAdapter(@NonNull FragmentManager fm, int behavior, int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return new RequestAppointmentFragment();
            case 1 :
                return new AppointmentFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}